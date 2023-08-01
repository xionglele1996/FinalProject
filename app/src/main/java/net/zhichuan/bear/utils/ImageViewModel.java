package net.zhichuan.bear.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

/**
 * This class is used to store the images.
 * It uses the LiveData library to store the images.
 */
public class ImageViewModel {
    /**
     * The list of images.
     */
    @NonNull
    public MutableLiveData<ArrayList<ImageEntity>> image = new MutableLiveData<>();
}
