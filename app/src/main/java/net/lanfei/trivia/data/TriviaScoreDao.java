package net.lanfei.trivia.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import net.matthew.converter.ConversionQuery;

import java.util.List;

/**
 * Data Access Object (DAO) interface for interacting with the "TriviaScore" table in the database.
 *
 * <p>This interface is annotated with {@link Dao}, indicating that it provides methods for accessing
 * and manipulating the "TriviaScore" table.
 */
@Dao
public interface TriviaScoreDao {

    /**
     * Inserts a new TriviaScore entry into the "TriviaScore" table.
     *
     * @param triviaScore The TriviaScore object to be inserted into the table.
     */
    @Insert
    void insert(TriviaScore triviaScore);

    /**
     * Deletes a TriviaScore entry from the "TriviaScore" table.
     *
     * @param triviaScore The TriviaScore object to be deleted from the table.
     */
    @Delete
    void delete(TriviaScore triviaScore);

    /**
     * Retrieves the top 10 TriviaScore entries from the "TriviaScore" table, ordered by the score in descending order.
     *
     * @return A list of TriviaScore objects representing the top 10 scores.
     */
    @Query("SELECT * FROM TriviaScore ORDER BY score DESC LIMIT 10")
    List<TriviaScore> getAllScores();
}

