package net.lanfei.trivia.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TriviaScore.class}, version = 1, exportSchema = false)
public abstract class TriviaDatabase extends RoomDatabase {
    public abstract TriviaScoreDao triviaScoreDAO();
}
