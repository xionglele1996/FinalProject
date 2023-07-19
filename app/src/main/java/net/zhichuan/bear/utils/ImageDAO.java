package net.zhichuan.bear.utils;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDAO {
    @Insert
    void insert(ImageEntity image);

    @Query("SELECT * FROM ImageEntity")
    List<ImageEntity> getAllImages();

    @Delete
    void delete(ImageEntity image);
}
