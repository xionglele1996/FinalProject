package net.lele.flighttracker;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import net.databinding.LeleFlightRowBinding;
import net.databinding.LeleSavedFlightActivityBinding;
import net.lele.flighttracker.data.FlightViewModel;

import java.util.List;

public class SavedFlightsActivity extends AppCompatActivity {

    private LeleSavedFlightActivityBinding binding;
    private FlightViewModel flightViewModel;

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

    public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
        private List<Flight> flights;

        public FlightAdapter(List<Flight> flights) {
            this.flights = flights;
        }

        @NonNull
        @Override
        public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LeleFlightRowBinding itemBinding = LeleFlightRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new FlightViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
            Flight flight = flights.get(position);
            holder.binding.tvFlightNumber.setText("Flight Number: " + flight.getFlightNumber());
            holder.binding.tvArrivalAirport.setText("Arrival Airport: " + flight.getDestinationAirport());
            holder.binding.tvGate.setText("Gate: " + flight.getGate());
            holder.binding.tvTerminal.setText("Terminal: " + flight.getTerminal());
            holder.binding.tvDelay.setText("Delay: " + flight.getDelay() + " min");

            holder.itemView.setOnClickListener(v -> {
                new AlertDialog.Builder(SavedFlightsActivity.this)
                        .setTitle("Delete Message")
                        .setMessage("Do you want to delete this flight?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Remove the flight from the database
                                flightViewModel.deleteFlight(flight);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            });

        }

        @Override
        public int getItemCount() {
            return flights.size();
        }

        public class FlightViewHolder extends RecyclerView.ViewHolder {
            LeleFlightRowBinding binding;

            public FlightViewHolder(LeleFlightRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
