package net.lele.flighttracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.databinding.LeleFlightDetailsBinding;

public class DetailsFragment extends Fragment {
    private static final String ARG_FLIGHT = "flight";

    private Flight selectedFlight;

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


        return binding.getRoot();
    }
}
