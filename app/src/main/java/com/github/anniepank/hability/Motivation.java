package com.github.anniepank.hability;

import android.content.Context;

import java.util.Random;

/**
 * Created by anya on 1/10/17.
 */

public class Motivation {
    public static String getShortMotivation(Context context) {
        String[] strings = context.getResources().getStringArray(R.array.short_quotes);
        return strings[new Random().nextInt(strings.length)];
    }
}
