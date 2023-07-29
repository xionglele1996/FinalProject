package net.zhichuan.bear.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class ImageViewModel {
    @NonNull
    public MutableLiveData<ArrayList<ImageEntity>> image = new MutableLiveData<>();
}
