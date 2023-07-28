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

public class DetailsFragment extends Fragment {
    private static final String ARG_FLIGHT = "flight";

    private Flight selectedFlight;

    private FlightViewModel flightViewModel;

    public static DetailsFragment newInstance(Flight flight) {
        DetailsFragment fragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_FLIGHT, flight);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedFlight = (Flight) getArguments().getSerializable(ARG_FLIGHT);
        }

        // Initialize the ViewModel
        flightViewModel = new ViewModelProvider(this).get(FlightViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LeleFlightDetailsBinding binding = LeleFlightDetailsBinding.inflate(inflater);
        binding.tvFlightNumber.setText(String.format("Flight Number: %s", selectedFlight.getFlightNumber()));
        binding.tvArrivalAirport.setText(String.format("Arrival Airport: %s", selectedFlight.getDestinationAirport()));
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
