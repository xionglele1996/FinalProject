package net.zhichuan.bear.utils;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDAO {
    @Insert
    void insert(@NonNull ImageEntity image);

    @NonNull
    @Query("SELECT * FROM ImageEntity")
    List<ImageEntity> getAllImages();

    @Delete
    void delete(@NonNull ImageEntity image);
}
