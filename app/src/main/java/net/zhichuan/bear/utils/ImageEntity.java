package net.zhichuan.bear.utils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ImageEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "width")
    protected int width;

    @ColumnInfo(name = "height")
    protected int height;

    @ColumnInfo(name = "time")
    protected long time;

    public ImageEntity(int width, int height, long time) {
        this.width = width;
        this.height = height;
        this.time = time;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getTime() {
        return time;
    }
}
