package net.lanfei.trivia.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import net.matthew.converter.ConversionQuery;

import java.util.List;

@Dao
public interface TriviaScoreDao {

    @Insert
    void insert(TriviaScore triviaScore);

    @Delete
    void delete(TriviaScore triviaScore);

    @Query("SELECT * FROM TriviaScore ORDER BY score DESC")
    List<TriviaScore> getAllScores();
}

