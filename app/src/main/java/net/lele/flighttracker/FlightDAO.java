package net.lele.flighttracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
/**
 * Data Access Object (DAO) for managing Flight entities.
 * This interface provides methods to interact with the underlying
 * database for performing operations such as inserting, querying,
 * and deleting flights.
 */
@Dao
public interface FlightDAO {
    /**
     * Inserts a flight into the database. If a flight with the same primary key
     * already exists, it will be replaced.
     *
     * @param flight The flight to be inserted or replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFlight(Flight flight);

    /**
     * Retrieves a live list of all flights from the database.
     * This LiveData will update automatically when the data changes.
     *
     * @return LiveData containing a list of all flights.
     */
    @Query("SELECT * FROM flight")
    LiveData<List<Flight>> getAllFlights();

    /**
     * Deletes a specific flight from the database.
     *
     * @param flight The flight to be deleted.
     */
    @Delete
    void deleteFlight(Flight flight);
}
