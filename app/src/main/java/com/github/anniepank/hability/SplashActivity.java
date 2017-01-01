package com.github.anniepank.hability;

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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
