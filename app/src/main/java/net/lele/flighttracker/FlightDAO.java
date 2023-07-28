package net.lele.flighttracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FlightDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFlight(Flight flight);

    @Query("SELECT * FROM flight")
    LiveData<List<Flight>> getAllFlights();

    @Delete
    void deleteFlight(Flight flight);
}
