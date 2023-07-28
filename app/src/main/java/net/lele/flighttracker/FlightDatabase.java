package net.lele.flighttracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Flight.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {
    public abstract FlightDAO flightDAO();

    private static volatile FlightDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FlightDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FlightDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FlightDatabase.class, "flight_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}