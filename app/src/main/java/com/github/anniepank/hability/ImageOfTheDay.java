package com.github.anniepank.hability;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.anniepank.hability.data.Settings;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anya on 12/19/16.
 */

public class ImageOfTheDay {

    public static final String BING_IMAGE_OF_THE_DAY_URL = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US";
    public static final String BING_PREFIX = "http://bing.com";

    public static void loadImage(final ImageView imageView) {
        if (!isConnected(imageView.getContext())) {
            loadImageIfOffline(imageView);
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BING_IMAGE_OF_THE_DAY_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    Gson gson = new Gson();
                    String responseString = new String(response, "UTF-8");
                    BingImage bingImage = gson.fromJson(responseString, BingImage.class);
                    String imageUrl = bingImage.images[0].url;
                    imageUrl = BING_PREFIX + imageUrl;
                    Settings.get(imageView.getContext()).cachedImageOfTheDayUrl = imageUrl;
                    Settings.get(imageView.getContext()).save(imageView.getContext());
                    imageView.setColorFilter(Color.argb(128, 0, 0, 0));

                    if (isContextDestroyed(imageView.getContext())) return;
                    try {
                        Glide.with(imageView.getContext()).load(imageUrl)
                                .placeholder(R.drawable.placeholder)
                                .crossFade()
                                .into(imageView);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                loadImageIfOffline(imageView);
            }
        });
    }

    private static void loadImageIfOffline(ImageView imageView) {
        if (Settings.get(imageView.getContext()).cachedImageOfTheDayUrl == null) {
            return;
        }
        if (isContextDestroyed(imageView.getContext())) return;
        Glide.with(imageView.getContext())
                .load(Settings.get(imageView.getContext()).cachedImageOfTheDayUrl)
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(imageView);
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected()));
    }

    private class BingImage {
        BingImageImage[] images;
    }

    private class BingImageImage {
        String url;
    }

    private static boolean isContextDestroyed(Context context) {
        if (context instanceof Activity) {
            return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed());
        }
        return false;
    }
}

