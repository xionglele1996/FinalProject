package net.zhichuan.bear.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<Integer, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")
    ImageView image;

    public DownloadImageTask(ImageView image) {
        this.image = image;
    }

    protected Bitmap doInBackground(Integer... data) {
        int width = data[0];
        int height = data[1];

        Bitmap image = null;
        try {
            InputStream in = new java.net.URL("https://placebear.com/" + width + "/" + height).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        try {
            image.setImageBitmap(result);
        } catch (RuntimeException ignored) {
        }
    }
}