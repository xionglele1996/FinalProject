package net.lanfei.trivia.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TriviaScore")
public class TriviaScore {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "username")
    protected String username;

    @ColumnInfo(name = "score")
    protected int score;

    public TriviaScore(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}
