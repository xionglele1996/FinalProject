package net.zhichuan.bear.utils;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ImageEntity.class}, version = 1, exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase {
    @NonNull
    public abstract ImageDAO imageDAO();
}
