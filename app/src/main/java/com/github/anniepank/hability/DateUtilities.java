package com.github.anniepank.hability;

import java.util.Date;

/**
 * Created by anya on 1/7/17.
 */

public class DateUtilities {
    public static long getToday() {
        return dateToNumber(new Date());
    }

    public static long dateToNumber(Date date) {
        return date.getTime() / (24 * 60 * 60 * 1000);
    }
}
