package net.lele.flighttracker.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import net.lele.flighttracker.Flight;
import net.lele.flighttracker.FlightDAO;
import net.lele.flighttracker.FlightDatabase;

import java.util.List;




public class FlightViewModel extends AndroidViewModel {
    private FlightDAO flightDAO;
    private LiveData<List<Flight>> allFlights;

    public FlightViewModel(Application application) {
        super(application);
        FlightDatabase db = FlightDatabase.getDatabase(application);
        flightDAO = db.flightDAO();
        allFlights = flightDAO.getAllFlights();
    }

    public LiveData<List<Flight>> getAllFlights() {
        return allFlights;
    }

    public void insertFlight(Flight flight) {
        FlightDatabase.databaseWriteExecutor.execute(() -> {
            flightDAO.insertFlight(flight);
        });
    }

    public void deleteFlight(Flight flight) {
        FlightDatabase.databaseWriteExecutor.execute(() -> {
            flightDAO.deleteFlight(flight);
        });
    }
}