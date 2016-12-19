package com.example.android.habittracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static void uploadImage(final ImageView image) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String str = new String(response, "UTF-8");
                    Log.i("", str);
                    String imageRegex = ".+\"url\":\"([^\"]*+)\".+";
                    Pattern pattern = Pattern.compile(imageRegex);
                    Matcher matcher = pattern.matcher(str);
                    matcher.matches();
                    String urlImage = matcher.group(1);
                    loadImageFromUrl(urlImage, image);
                    Log.i("", urlImage);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // called when response HTTP status is "200 OK"
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
}
