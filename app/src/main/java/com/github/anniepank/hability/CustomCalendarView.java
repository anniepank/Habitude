package com.github.anniepank.hability;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.samsistemas.calendarview.widget.CalendarView;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by anya on 12/26/16.
 */

public class CustomCalendarView extends CalendarView {

    public static final String ON_DAY_OF_MONTH_CLICK_LISTENER = "onDayOfMonthClickListener";

    public void setOnDayOfMonthClickListener(View.OnClickListener onDayOfMonthClickListener) {
        try {
            Field field = CalendarView.class.getDeclaredField(ON_DAY_OF_MONTH_CLICK_LISTENER);
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
        setOnDayOfMonthClickListener(null);
    }

    @Override
    public void setDateAsSelected(Date currentDate) {

    }

    @Override
    public void setCurrentDay(@NonNull Date todayDate) {
    }
}
