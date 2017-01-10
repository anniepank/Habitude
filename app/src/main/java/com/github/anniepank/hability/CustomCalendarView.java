package com.github.anniepank.hability;

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
