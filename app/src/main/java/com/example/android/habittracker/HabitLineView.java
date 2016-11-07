package com.example.android.habittracker;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.android.habittracker.R.layout.habit_line_view;

/**
 * Created by anya on 11/2/16.
 */

public class HabitLineView extends LinearLayout {
    public HabitLineView(Context context) {
        super(context);
        loadView(); // Input == load
    }

    public HabitLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadView();
    }

    public HabitLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadView();
    }

    private TextView textView;

    public void loadView() { //InputView
        LayoutInflater.from(getContext()).inflate(habit_line_view, this, true);
        this.setPadding(0, 0, 0, 10);
        textView = (TextView)findViewById(R.id.name);
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditHabitActivity.class);
               // EditText editText = (EditText) findViewById(R.id.edit_message);
               // String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);
                getContext().startActivity(intent);
            }
        });

    }

    //update habit in Habit Line View
    public void update(Habit habit) {
        textView.setText(habit.habitName);
    }



}


