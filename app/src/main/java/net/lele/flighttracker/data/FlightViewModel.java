package net.lele.flighttracker.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import net.lele.flighttracker.Flight;
import net.lele.flighttracker.FlightDAO;
import net.lele.flighttracker.FlightDatabase;

import java.util.List;



/**
 * ViewModel class to interact with the FlightDatabase.
 * It provides methods to insert, delete, and retrieve flight data, encapsulating the underlying
 * implementation of the database interactions. By using this ViewModel, the UI can observe changes
 * to the flight data and react accordingly.
 */
public class FlightViewModel extends AndroidViewModel {
    private FlightDAO flightDAO;
    private LiveData<List<Flight>> allFlights;

    /**
     * Constructs the FlightViewModel.
     *
     * @param application The application context, used to get access to the FlightDatabase.
     */
    public FlightViewModel(Application application) {
        super(application);
        FlightDatabase db = FlightDatabase.getDatabase(application);
        flightDAO = db.flightDAO();
        allFlights = flightDAO.getAllFlights();
    }

    /**
     * Gets a LiveData object that represents all the flights in the database.
     * When the underlying flight data changes, the LiveData object will update
     * any observers, such as UI components.
     *
     * @return LiveData object containing the list of all flights.
     */
    public LiveData<List<Flight>> getAllFlights() {
        return allFlights;
    }

    /**
     * Inserts a new flight into the database.
     *
     * @param flight The flight object to be inserted.
     */
    public void insertFlight(Flight flight) {
        FlightDatabase.databaseWriteExecutor.execute(() -> {
            flightDAO.insertFlight(flight);
        });
    }

    /**
     * Deletes a specified flight from the database.
     *
     * @param flight The flight object to be deleted.
     */
    public void deleteFlight(Flight flight) {
        FlightDatabase.databaseWriteExecutor.execute(() -> {
            flightDAO.deleteFlight(flight);
        });
    }
}