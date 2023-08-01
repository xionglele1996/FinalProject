package net.zhichuan.bear.utils;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This class is used to access the database.
 * It uses the Room library to access the database.
 */
@Dao
public interface ImageDAO {
    /**
     * Insert an image into the database.
     * @param image The image to be inserted.
     */
    @Insert
    void insert(@NonNull ImageEntity image);

    /**
     * Get all the images from the database.
     * @return A list of all the images in the database.
     */
    @NonNull
    @Query("SELECT * FROM ImageEntity")
    List<ImageEntity> getAllImages();

    /**
     * Delete an image from the database.
     * @param image The image to be deleted.
     */
    @Delete
    void delete(@NonNull ImageEntity image);
}
