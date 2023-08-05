package net.lanfei.trivia.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TriviaScore.class}, version = 1, exportSchema = false)
public abstract class TriviaDatabase extends RoomDatabase {

    static private TriviaDatabase instance;

    public abstract TriviaScoreDao triviaScoreDAO();

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
