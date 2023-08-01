package net.lanfei.trivia.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TriviaScore {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "username")
    protected int username;

    @ColumnInfo(name = "score")
    protected int score;

    public TriviaScore(int username, int score) {
        this.username = username;
        this.score = score;
    }

    public int getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}
