package com.example.android.habittracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anya on 12/19/16.
 */

public class ImageOfTheDay {
    private static void loadImageFromUrl(String urlImage, final ImageView imageView) {
        urlImage = "http://bing.com" + urlImage;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlImage, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                imageView.setColorFilter(Color.argb(128, 0, 0, 0));
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public static void loadImage(final ImageView imageView) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    Gson gson = new Gson();
                    String str = new String(response, "UTF-8");
                    BingImage bingImage = gson.fromJson(str, BingImage.class);
                    String imageUrl = bingImage.images[0].url;

                    loadImageFromUrl(imageUrl, imageView);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private class BingImage {
        public BingImageImage[] images;
    }

    private class BingImageImage {
        public String url;
    }
}
