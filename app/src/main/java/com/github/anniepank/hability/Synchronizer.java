package com.github.anniepank.hability;

import android.content.Context;

import com.github.anniepank.hability.data.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Synchronizer {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void sync(Context context) {
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resStr = response.body().string();
            }
        });
    }
}
