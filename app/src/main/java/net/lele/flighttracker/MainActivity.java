package net.lele.flighttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import net.databinding.LeleActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private LeleActivityMainBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LeleActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("FlightTrackerPrefs", MODE_PRIVATE);

        // Show Toast
        Toast.makeText(this, "Welcome to Flight Tracker", Toast.LENGTH_SHORT).show();

        // Show Snackbar
        binding.leleSearchButton.setOnClickListener(v -> {
            String input = binding.leleAirportCodeEditText.getText().toString();

            if (input.length() != 3) {
                // Input length is not 3
                Snackbar.make(binding.getRoot(), "Error! Airport code must be 3 letters.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Clear the input and initialize to empty string
                                binding.leleAirportCodeEditText.setText("");
                            }
                        })
                        .show();
            } else if (!input.matches("[a-zA-Z]+")) {
                // Input contains characters other than letters
                Snackbar.make(binding.getRoot(), "Error! Airport code must only contain letters.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Clear the input and initialize to empty string
                                binding.leleAirportCodeEditText.setText("");
                            }
                        })
                        .show();
            } else {
                // Show AlertDialog to confirm search
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Search Flights")
                        .setMessage("Are you sure you want to search flights for this airport?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Store the airport code in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("airportCode", input);
                            editor.apply();

                            // Execute the search logic here
                            executeSearch(input);
                        })
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        // Prefill the EditText with the stored airport code
        String storedAirportCode = sharedPreferences.getString("airportCode", "");
        binding.leleAirportCodeEditText.setText(storedAirportCode);
    }

    private void executeSearch(String airportCode) {
        Intent intent = new Intent(this, SearchResult.class);
        intent.putExtra("airportCode", airportCode);
        startActivity(intent);
    }
}
