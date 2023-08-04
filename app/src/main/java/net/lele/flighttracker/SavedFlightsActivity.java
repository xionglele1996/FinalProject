package net.lele.flighttracker;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import net.R;
import net.databinding.LeleSavedFlightActivityBinding;
import net.databinding.LeleSavedFlightRowBinding;
import net.lele.flighttracker.data.FlightViewModel;

import java.util.List;
/**
 * Activity to display and manage saved flights.
 * It uses a RecyclerView to list each flight, and allows the user to delete individual flights.
 */
public class SavedFlightsActivity extends AppCompatActivity {

    private LeleSavedFlightActivityBinding binding;
    private FlightViewModel flightViewModel;

    /**
     * Initializes the activity, setting up views and connecting to the FlightViewModel to
     * retrieve the saved flights.
     *
     * @param savedInstanceState Information about the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LeleSavedFlightActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        flightViewModel = new ViewModelProvider(this).get(FlightViewModel.class);

        flightViewModel.getAllFlights().observe(this, flights -> {
            FlightAdapter flightAdapter = new FlightAdapter(flights);
            binding.savedFlightsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.savedFlightsRecyclerView.setAdapter(flightAdapter);


        });
    }

    /**
     * Inner class for the RecyclerView Adapter to handle the display of saved flights.
     */
    public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
        private List<Flight> flights;

        /**
         * Constructor for FlightAdapter.
         *
         * @param flights List of Flight objects to display.
         */
        public FlightAdapter(List<Flight> flights) {
            this.flights = flights;
        }

        @NonNull
        @Override
        public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LeleSavedFlightRowBinding itemBinding = LeleSavedFlightRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new FlightViewHolder(itemBinding);
        }

        /**
         * Binds the flight data to the ViewHolder.
         *
         * @param holder   The ViewHolder to bind data to.
         * @param position The position of the flight in the list.
         */
        @Override
        public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
            Flight flight = flights.get(position);
            holder.binding.tvFlightNumber.setText("Flight Number: " + flight.getFlightNumber());
           // holder.binding.tvArrivalAirport.setText("Arrival Airport: " + flight.getDestinationAirport() + " Airport");
            //holder.binding.tvGate.setText("Gate: " + flight.getGate());
            //holder.binding.tvTerminal.setText("Terminal: " + flight.getTerminal());
            //holder.binding.tvDelay.setText("Delay: " + flight.getDelay() + " min");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SavedDetailFragment detailsFragment = SavedDetailFragment.newInstance(flight);
                    ((AppCompatActivity)v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.savedFragment, detailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return flights.size();
        }

        /**
         * Inner class for the RecyclerView ViewHolder to hold the views for each saved flight.
         */
        public class FlightViewHolder extends RecyclerView.ViewHolder {
            LeleSavedFlightRowBinding binding;

            public FlightViewHolder(LeleSavedFlightRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
