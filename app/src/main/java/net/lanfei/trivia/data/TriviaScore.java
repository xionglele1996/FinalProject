package net.lanfei.trivia.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a single trivia score entry in the database.
 *
 * <p>This class is annotated with {@link Entity} to indicate that it represents a table in the database.
 * The table name is set to "TriviaScore" using the {@link Entity#tableName()} attribute.
 */
@Entity(tableName = "TriviaScore")
public class TriviaScore {

    /**
     * The primary key for the TriviaScore entry.
     * The id is automatically generated using the {@link PrimaryKey#autoGenerate()} attribute.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    /**
     * The username associated with the TriviaScore entry.
     * This is a protected field, and its value is accessed through the getter method {@link #getUsername()}.
     */
    @ColumnInfo(name = "username")
    protected String username;

    /**
     * The score associated with the TriviaScore entry.
     * This is a protected field, and its value is accessed through the getter method {@link #getScore()}.
     */
    @ColumnInfo(name = "score")
    protected int score;

    /**
     * Constructor for creating a new TriviaScore entry.
     *
     * @param username The username associated with the TriviaScore entry.
     * @param score    The score associated with the TriviaScore entry.
     */
    public TriviaScore(String username, int score) {
        this.username = username;
        this.score = score;
    }

    /**
     * Gets the username associated with the TriviaScore entry.
     *
     * @return The username associated with the TriviaScore entry.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the score associated with the TriviaScore entry.
     *
     * @return The score associated with the TriviaScore entry.
     */
    public int getScore() {
        return score;
    }
}
