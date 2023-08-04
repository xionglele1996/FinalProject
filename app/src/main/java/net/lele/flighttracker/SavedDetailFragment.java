package net.lele.flighttracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import net.databinding.LeleSavedFlightDetailsBinding;
import net.lele.flighttracker.data.FlightViewModel;

public class SavedDetailFragment extends Fragment {
    private static final String ARG_FLIGHT = "flight";

    private Flight selectedFlight;

    private FlightViewModel flightViewModel;

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param flight The flight object to be displayed.
     * @return A new instance of fragment DetailsFragment.
     */
    public static SavedDetailFragment newInstance(Flight flight) {
        SavedDetailFragment fragment = new SavedDetailFragment();

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

        LeleSavedFlightDetailsBinding binding = LeleSavedFlightDetailsBinding.inflate(inflater);
        binding.tvFlightNumber.setText(String.format("Flight Number: %s", selectedFlight.getFlightNumber()));
        binding.tvArrivalAirport.setText(String.format("Arrival Airport: %s Airport", selectedFlight.getDestinationAirport()));
        binding.tvTerminal.setText(String.format("Departure Terminal: %s", selectedFlight.getTerminal()));
        binding.tvGate.setText(String.format("Departure Gate: %s", selectedFlight.getGate()));

        if(selectedFlight.getDelay() > 0){
            binding.tvDelay.setText(String.format("Estimated Departure Delay: %d minutes", selectedFlight.getDelay()));
        } else {
            binding.tvDelay.setText("Flight is on time for departure.");
        }

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()) // Use getActivity() here since you are inside a fragment
                        .setTitle("Delete Flight")
                        .setMessage("Do you want to delete this flight?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Remove the flight from the database
                                flightViewModel.deleteFlight(selectedFlight);

                                // Display success message
                                Toast.makeText(getActivity(), "Flight deleted successfully", Toast.LENGTH_LONG).show();

                                // Optionally, you may want to navigate back or update the UI after deletion
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });




        return binding.getRoot();
    }
}
