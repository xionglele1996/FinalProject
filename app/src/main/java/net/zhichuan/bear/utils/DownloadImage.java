package net.zhichuan.bear.utils;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class DownloadImage {
    public static void downloadImage(int width, int height, @NonNull ImageView imageView) {
        String URL = "https://placebear.com/" + width + "/" + height;

        ImageRequest request = new ImageRequest(URL,
                                                imageView::setImageBitmap,
                                                width, height,
                                                ImageView.ScaleType.CENTER_INSIDE,
                                                null,
                                                Throwable::printStackTrace);

        Volley.newRequestQueue(imageView.getContext()).add(request);
    }
}