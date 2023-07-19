package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ConversionQueryDao {

    @Insert
    void insert(ConversionQuery conversionQuery);

    @Delete
    void delete(ConversionQuery conversionQuery);

    @Query("SELECT * FROM conversion_query_table")
    List<ConversionQuery> getAllQueries();
}

