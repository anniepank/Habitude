package com.example.android.habittracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.samsistemas.calendarview.widget.CalendarView;

import java.lang.reflect.Field;

/**
 * Created by anya on 12/26/16.
 */

public class CustomCalendarView extends CalendarView {
    public void setOnDayOfMonthClickListener(View.OnClickListener onDayOfMonthClickListener) {
        try {
            Field field = CalendarView.class.getDeclaredField("onDayOfMonthClickListener");
            field.setAccessible(true);
            field.set(this, onDayOfMonthClickListener);

        } catch (Exception e) {
        }
    }

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            Field field = CalendarView.class.getDeclaredField("onDayOfMonthClickListener");
            field.setAccessible(true);
            field.set(this, null);
        } catch (Exception e) {
        }
    }


}

/*
interface Voice {
    void say(String str);
}

class Animal {
    Voice voice;
    void greet() {
        this.voice.say("Hello");
    }
}


class DogVoice implements Voice {
    void say(String str) { console..log("Woof" + str); }
}

Animal a = new Animal();
a.voice = new Voice() { // Animal$Voice1
    void say(String str)
    {
        console.log("Meow Meow" + str + "meow");
    }
}

a.greet()
a.voice = new DogVoice()
a.greet()*/