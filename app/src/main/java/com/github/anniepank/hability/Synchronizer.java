package com.github.anniepank.hability;

import android.content.Context;
import android.util.Log;

import com.github.anniepank.hability.data.Habit;
import com.github.anniepank.hability.data.HabitDate;
import com.github.anniepank.hability.data.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Synchronizer {
    private static boolean syncRequested = false;
    private static boolean syncRunning = false;

    public interface ISyncCallback {
        void onFinished();
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void sync(final Context context, final ISyncCallback callback) {
        if (syncRunning) {
            syncRequested = true;
        } else {
            _sync(context, callback);
        }
    }


    private static void _sync(final Context context, final ISyncCallback callback) {
        if (Settings.get(context).syncKey == null) return;

        syncRunning = true;

        Log.i("LOGGING", "start");

        String key = Settings.get(context).syncKey;
        JsonArray habits = (JsonArray) new Gson().toJsonTree(Settings.get(context).habits);
        JsonObject data = new JsonObject();
        data.add("habits", habits);
        data.addProperty("key", key);
        String payload = new Gson().toJson(data);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, payload);
        Request request = new Request.Builder()
                .url("http://habitude.by:9000/api/synchronize")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) callback.onFinished();
                syncEnd(context);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resStr = response.body().string();

                JsonParser parser = new JsonParser();
                JsonArray jsonObject = parser.parse(resStr).getAsJsonObject().get("habits").getAsJsonArray();

                for (int i = 0; i < jsonObject.size(); i++) {
                    JsonObject jsonHabit = jsonObject.get(i).getAsJsonObject().get("habit").getAsJsonObject();
                    if (!jsonObject.get(i).getAsJsonObject().get("inApp").getAsBoolean()) {
                        Habit habit = new Habit(jsonHabit.get("id").getAsString(), jsonHabit.get("name").getAsString(), jsonHabit.get("deleted").getAsBoolean());
                        Settings.get(context).habits.add(habit);
                    } else {
                        for (Habit habitInSettings : Settings.get(context).habits) {
                            if (habitInSettings.id.equals(jsonHabit.get("id").getAsString())) {
                                habitInSettings.name = jsonHabit.get("name").getAsString();
                                habitInSettings.deleted = jsonHabit.get("deleted").getAsBoolean();
                            }
                        }
                    }
                }

                jsonObject = parser.parse(resStr).getAsJsonObject().get("dates").getAsJsonArray();

                for (int i = 0; i < jsonObject.size(); i++) {
                    JsonObject jsonDate = jsonObject.get(i).getAsJsonObject().get("date").getAsJsonObject();
                    if (jsonObject.get(i).getAsJsonObject().get("inApp").getAsBoolean()) {

                        for (Habit habit : Settings.get(context).habits) {
                            if (habit.id.equals(jsonDate.get("habitId").getAsString())) {
                                for (HabitDate habitDate : habit.days) {
                                    if (habitDate.id.equals(jsonDate.get("id").getAsString())) {
                                        habitDate.deleted = jsonDate.get("deleted").getAsBoolean();
                                    }
                                }
                            }
                        }
                    } else {
                        for (Habit habit : Settings.get(context).habits) {
                            if (habit.id.equals(jsonDate.get("habitId").getAsString())) {
                                String[] dateStr = jsonDate.get("date").getAsString().split("-");
                                HabitDate habitDate = new HabitDate();
                                habitDate.id = jsonDate.get("id").getAsString();
                                habitDate.date = new Date(Integer.valueOf(dateStr[0]) - 1900, Integer.valueOf(dateStr[1]) - 1, Integer.valueOf(dateStr[2]) + 1).getTime() / 1000 / 60 / 60 / 24;
                                habitDate.deleted = jsonDate.get("deleted").getAsBoolean();
                                habit.days.add(habitDate);

                            }
                        }
                    }
                }

                Settings.get(context).save(context);
                if (callback != null) {
                    callback.onFinished();
                }

                syncEnd(context);
                Log.i("LOGGING", "end");
            }
        });
    }

    private static void syncEnd(Context context) {
        syncRunning = false;
        if (syncRequested) {
            syncRequested = false;
            _sync(context, null);
        }
    }
}
