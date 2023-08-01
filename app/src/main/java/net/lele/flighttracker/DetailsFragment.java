package net.lele.flighttracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.databinding.LeleFlightDetailsBinding;
import net.lele.flighttracker.data.FlightViewModel;
/**
 * Fragment to display the details of a selected flight.
 * It allows the user to view detailed information about a specific flight, such as flight number,
 * arrival airport, terminal, gate, and delay information.
 */
public class DetailsFragment extends Fragment {
    private static final String ARG_FLIGHT = "flight";

    private Flight selectedFlight;

    private FlightViewModel flightViewModel;

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param flight The flight object to be displayed.
     * @return A new instance of fragment DetailsFragment.
     */
    public static DetailsFragment newInstance(Flight flight) {
        DetailsFragment fragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_FLIGHT, flight);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Initialize the fragment and retrieve the selected flight object from the arguments.
     *
     * @param savedInstanceState Previous saved state of the fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedFlight = (Flight) getArguments().getSerializable(ARG_FLIGHT);
        }

        // Initialize the ViewModel
        flightViewModel = new ViewModelProvider(this).get(FlightViewModel.class);

    }

    /**
     * Inflate the layout for this fragment and bind the selected flight data to the views.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState Previous saved state of the fragment.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LeleFlightDetailsBinding binding = LeleFlightDetailsBinding.inflate(inflater);
        binding.tvFlightNumber.setText(String.format("Flight Number: %s", selectedFlight.getFlightNumber()));
        binding.tvArrivalAirport.setText(String.format("Arrival Airport: %s Airport", selectedFlight.getDestinationAirport()));
        binding.tvTerminal.setText(String.format("Departure Terminal: %s", selectedFlight.getTerminal()));
        binding.tvGate.setText(String.format("Departure Gate: %s", selectedFlight.getGate()));

        if(selectedFlight.getDelay() > 0){
            binding.tvDelay.setText(String.format("Estimated Departure Delay: %d minutes", selectedFlight.getDelay()));
        } else {
            binding.tvDelay.setText("Flight is on time for departure.");
        }

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save the flight to database
                flightViewModel.insertFlight(selectedFlight);

                // display success message
                Toast.makeText(getActivity(), "Flight saved successfully", Toast.LENGTH_LONG).show();
            }
        });



        return binding.getRoot();
    }
}
