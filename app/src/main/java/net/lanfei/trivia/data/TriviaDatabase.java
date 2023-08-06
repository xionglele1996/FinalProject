package net.lanfei.trivia.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * A Room database class that manages the database for storing TriviaScore entities.
 *
 * <p>This class is annotated with {@link Database} to define the database configuration.
 * It specifies the entity classes to be stored in the database, the database version,
 * and whether to export the database schema.
 */
@Database(entities = {TriviaScore.class}, version = 1, exportSchema = false)
public abstract class TriviaDatabase extends RoomDatabase {

    /**
     * Singleton instance of the TriviaDatabase.
     */
    static private TriviaDatabase instance;

    /**
     * Provides access to the Data Access Object (DAO) for TriviaScore entities.
     *
     * @return The Data Access Object (DAO) for TriviaScore entities.
     */
    public abstract TriviaScoreDao triviaScoreDAO();

    /**
     * Gets the singleton instance of the TriviaDatabase. If the instance is not already created,
     * it creates a new instance using Room's databaseBuilder() method and returns it.
     *
     * <p>The database is built based on the specified configuration using the given context.
     * The database name is "TriviaDatabase" and the database version is 1. If a database migration
     * is not possible, it falls back to a destructive migration, which means the existing data
     * will be deleted and the new database will be created.
     *
     * @param context The application context to be used for building the database.
     * @return The singleton instance of the TriviaDatabase.
     */
    public static  TriviaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TriviaDatabase.class, "TriviaDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
