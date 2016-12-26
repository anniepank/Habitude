package com.example.android.habittracker;

import android.content.Context;
import android.util.AttributeSet;

import com.samsistemas.calendarview.widget.CalendarView;

import java.lang.reflect.Field;

/**
 * Created by anya on 12/26/16.
 */

public class UnselectableCalendar extends CalendarView {
    public UnselectableCalendar(Context context) {
        super(context);
    }

    public UnselectableCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            Field field = CalendarView.class.getDeclaredField("onDayOfMonthClickListener");
            field.setAccessible(true);
            field.set(this, null);
        } catch (Exception e) {
        }
        ;
    }
}
