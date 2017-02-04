package com.github.anniepank.hability.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.anniepank.hability.R;
import com.github.anniepank.hability.SampleSlide;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by anya on 2/3/17.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(SampleSlide.newInstance(R.layout.slide1));
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_2_title), getString(R.string.slide_2_descr), R.drawable.slide2,
                getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide3_title), getString(R.string.slide3_desc), R.drawable.slide3, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_4_title), getString(R.string.slide_4_desc), R.drawable.slide4, getResources().getColor(R.color.colorPrimary)));

        setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed() {
        super.onDonePressed();
        finish();
    }
}
