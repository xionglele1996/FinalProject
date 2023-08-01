package net.zhichuan.bear.utils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class is used to represent an image in the database.
 * It uses the Room library to create the database.
 */
@Entity
public class ImageEntity {
    /**
     * The id of the image.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    /**
     * The width of the image.
     */
    @ColumnInfo(name = "width")
    protected int width;

    /**
     * The height of the image.
     */
    @ColumnInfo(name = "height")
    protected int height;

    /**
     * The time when the image was generated.
     */
    @ColumnInfo(name = "time")
    protected long time;

    /**
     * Create an ImageEntity object.
     * @param width The width of the image.
     * @param height The height of the image.
     * @param time The time when the image was generated.
     */
    public ImageEntity(int width, int height, long time) {
        this.width = width;
        this.height = height;
        this.time = time;
    }

    /**
     * Get the id of the image.
     * @return The id of the image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the width of the image.
     * @return The width of the image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the time when the image was generated.
     * @return The time when the image was generated.
     */
    public long getTime() {
        return time;
    }
}
