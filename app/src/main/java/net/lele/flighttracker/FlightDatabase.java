package net.lele.flighttracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Room database for managing Flight entities.
 * This abstract class serves as the main access point for interacting
 * with the underlying SQLite database for flight data. It provides
 * methods to retrieve the DAO and execute operations on a background thread.
 *
 * The singleton pattern ensures that only one instance of the database
 * is created and shared throughout the app.
 */
@Database(entities = {Flight.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {
    /**
     * Provides access to the FlightDAO for managing Flight entities.
     *
     * @return The FlightDAO interface implementation.
     */
    public abstract FlightDAO flightDAO();

    private static volatile FlightDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    /**
     * Executor service with a fixed thread pool that you can use to run
     * database operations asynchronously on a background thread.
     */
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Retrieves the singleton instance of the FlightDatabase,
     * creating it if necessary.
     *
     * @param context The application context.
     * @return The singleton instance of FlightDatabase.
     */
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