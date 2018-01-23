package com.github.anniepank.hability.data;

import java.util.UUID;

/**
 * Created by anya on 11/25/17.
 */

public class HabitDate {
    public String id;
    public long date;
    public long updatedAt;
    public boolean deleted;

    public HabitDate(long day) {
        id = UUID.randomUUID().toString();
        this.date = day;
        deleted = false;
        bump();
    }

    public HabitDate() {
        id = UUID.randomUUID().toString();
    }

    public void bump() {
        updatedAt = System.currentTimeMillis();
    }
}
