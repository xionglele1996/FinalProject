package net.matthew.converter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ConversionQuery.class}, version = 2, exportSchema = false)

/**
 * This class is used to create the database.
 * It uses the Room library to create the database.
 */
public abstract class AppDatabase extends RoomDatabase {

    /**
     * The name of the database.
     */
    public abstract ConversionQueryDao conversionQueryDao();

}

