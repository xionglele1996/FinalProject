package net.zhichuan.bear.utils;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * This class is used to download images from the internet.
 * It uses the Volley library to download the image.
 * The image is downloaded from <a href="https://placebear.com">https://placebear.com</a>
 */
public class DownloadImage {
    /**
     * The URL of the website from which the images are downloaded.
     */
    public static final String DOWNLOAD_URL = "https://placebear.com";

    /**
     * Download an image from the internet and sets it as the image of the given ImageView.
     * @param width The width of the image.
     * @param height The height of the image.
     * @param imageView The ImageView in which the image will be set.
     */
    public static void downloadImage(int width, int height, @NonNull ImageView imageView) {
        String URL = DOWNLOAD_URL + "/" + width + "/" + height;

        ImageRequest request = new ImageRequest(URL,
                                                imageView::setImageBitmap,
                                                width, height,
                                                ImageView.ScaleType.CENTER_INSIDE,
                                                null,
                                                Throwable::printStackTrace);

        Volley.newRequestQueue(imageView.getContext()).add(request);
    }
}