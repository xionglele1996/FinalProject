package net.zhichuan.bear.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class DownloadImageTask {
    public static void downloadImage(int width, int height, ImageView imageView) {
        AtomicReference<Bitmap> image = new AtomicReference<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                InputStream in = new java.net.URL("https://placebear.com/" + width + "/" + height).openStream();
                image.set(BitmapFactory.decodeStream(in));
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                imageView.setImageBitmap(image.get());
            });
        });
    }
}