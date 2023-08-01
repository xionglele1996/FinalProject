package net.lele.flighttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.R;
import net.databinding.LeleActivitySearchResultBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    private LeleActivitySearchResultBinding binding;
    private List<Flight> flights; // This list should be populated with the Flight objects you received

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LeleActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        flights = (ArrayList<Flight>) getIntent().getSerializableExtra("flights");


        FlightAdapter flightAdapter = new FlightAdapter(flights);
        binding.flightRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.flightRecyclerView.setAdapter(flightAdapter);
    }

    public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

        private List<Flight> flights;

        public FlightAdapter(List<Flight> flights) {
            this.flights = flights;
        }

        @NonNull
        @Override
        public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lele_flight_row, parent, false);
            return new FlightViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
            Flight flight = flights.get(position);
            holder.tvFlightNumber.setText("Flight Number: " + flight.getFlightNumber());
           // holder.tvArrivalAirport.setText("Arrival Airport: " + flight.getDestinationAirport() + " Airport");
           // holder.tvGate.setText("Gate: " + flight.getGate());
           // holder.tvTerminal.setText("Terminal: " + flight.getTerminal());
           // holder.tvDelay.setText("Delay: " + flight.getDelay() + " min");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailsFragment detailsFragment = DetailsFragment.newInstance(flight);
                    ((AppCompatActivity)v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, detailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

        }


        @Override
        public int getItemCount() {
            return flights.size();
        }

        public class FlightViewHolder extends RecyclerView.ViewHolder {
            TextView tvFlightNumber, tvArrivalAirport, tvGate, tvTerminal, tvDelay;

            public FlightViewHolder(@NonNull View itemView) {
                super(itemView);
                tvFlightNumber = itemView.findViewById(R.id.tvFlightNumber);
                //tvArrivalAirport = itemView.findViewById(R.id.tvArrivalAirport);
                //tvGate = itemView.findViewById(R.id.tvGate);
                //tvTerminal = itemView.findViewById(R.id.tvTerminal);
                //tvDelay = itemView.findViewById(R.id.tvDelay);


            }
        }
    }
}
