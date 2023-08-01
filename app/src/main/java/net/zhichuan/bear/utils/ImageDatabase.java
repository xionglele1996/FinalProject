package net.zhichuan.bear.utils;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * This class is used to create the database.
 * It uses the Room library to create the database.
 */
@Database(entities = {ImageEntity.class}, version = 2, exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase {
    /**
     * Get the ImageDAO object.
     * @return The ImageDAO object.
     */
    @NonNull
    public abstract ImageDAO imageDAO();
}
