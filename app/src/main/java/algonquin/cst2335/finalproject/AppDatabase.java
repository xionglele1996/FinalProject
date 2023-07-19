package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ConversionQuery.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ConversionQueryDao conversionQueryDao();

}

