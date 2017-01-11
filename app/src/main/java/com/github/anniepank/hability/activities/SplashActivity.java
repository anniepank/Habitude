package com.github.anniepank.hability.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by anya on 1/1/17.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
