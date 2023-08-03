package net.matthew.converter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO (Data Access Object) interface for interacting with the ConversionQuery table.
 * It provides methods to insert, delete, and query conversion queries.
 */
@Dao
public interface ConversionQueryDao {

    /**
     * Inserts a new conversion query into the database.
     *
     * @param conversionQuery The conversion query to be inserted.
     */
    @Insert
    void insert(ConversionQuery conversionQuery);

    /**
     * Deletes a specific conversion query from the database.
     *
     * @param conversionQuery The conversion query to be deleted.
     */
    @Delete
    void delete(ConversionQuery conversionQuery);

    /**
     * Retrieves all conversion queries from the conversion_query_table.
     *
     * @return A list of all conversion queries in the table.
     */
    @Query("SELECT * FROM conversion_query_table")
    List<ConversionQuery> getAllQueries();
}

