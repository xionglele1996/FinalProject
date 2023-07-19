package net.zhichuan.bear.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ImageEntity.class}, version = 1, exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase {
    public abstract ImageDAO imageDAO();
}
