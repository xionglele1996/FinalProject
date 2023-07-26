package net.lele.flighttracker;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import net.R;

import net.databinding.LeleActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private LeleActivityMainBinding binding;
    private SharedPreferences sharedPreferences;

    private JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LeleActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = binding.toolBar;
        setSupportActionBar(toolbar);




        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("FlightTrackerPrefs", MODE_PRIVATE);

        String savedAirportCode = sharedPreferences.getString("airportCode", "");
        binding.airportCodeEditText.setText(savedAirportCode);

        // Show Toast
        Toast.makeText(this, R.string.lele_welcomeMessage, Toast.LENGTH_LONG).show();



        binding.searchButton.setOnClickListener(v -> {
            String input = binding.airportCodeEditText.getText().toString();

            if (input.length() != 3) {
                showErrorSnackbar(R.string.lele_lengthErrorMessage);
            } else if (!input.matches("[a-zA-Z]+")) {
                showErrorSnackbar(R.string.lele_digitErrorMessage);
            } else {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.lele_searchMsg)
                        .setMessage(R.string.lele_questionMsg)
                        .setPositiveButton(R.string.lele_yesBtn, (dialog, which) -> {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("airportCode", input);
                            editor.apply();

                            executeSearch(input);
                        })
                        .setNegativeButton(R.string.lele_noBtn, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lele_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.item1) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(R.string.lele_helpTitle);
            alertDialog.setMessage(getString(R.string.lele_helpMsg));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.lele_okMsg),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void showErrorSnackbar(int messageResource) {
        String message = getString(messageResource);
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction(R.string.lele_retryMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.airportCodeEditText.setText("");
                    }
                })
                .show();
    }

    private void executeSearch(String airportCode) {
        String url = "http://api.aviationstack.com/v1/flights?access_key=20385bd99950907e638561f5dc176319&dep_iata=" + airportCode;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                (successfulResponse) -> {
                    try {
                        JSONArray data = successfulResponse.getJSONArray("data");
                        ArrayList<Flight> flights = new ArrayList<>();

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject thisObj = data.getJSONObject(i);
                            JSONObject departure = thisObj.getJSONObject("departure");

                            if (departure.getString("iata").equals(airportCode)) {
                                String status = thisObj.getString("flight_status");

                                String gate = null;
                                if (!departure.isNull("gate")) {
                                    gate = departure.getString("gate");
                                }

                                int delay = 0;
                                if (!departure.isNull("delay")) {
                                    delay = departure.getInt("delay");
                                }

                                JSONObject airline = thisObj.getJSONObject("airline");
                                String airlineName = airline.isNull("name") ? "" : airline.getString("name"); // Handle null value

                                // Get flight number
                                JSONObject flightObj = thisObj.getJSONObject("flight");
                                String flightNumber = flightObj.isNull("iata") ? "" : flightObj.getString("iata"); // Handle null value

                                Flight flight = new Flight(flightNumber, status, gate, delay, airlineName);
                                flights.add(flight);

                            }
                        }

                        Log.d("FlightTracker", "Number of flights found: " + flights.size());

                        Intent intent = new Intent(MainActivity.this, SearchResult.class);
                        intent.putExtra("flights", flights);
                        startActivity(intent);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },
                (error) -> {showErrorSnackbar(R.string.lele_networkErrorMsg);
                    error.printStackTrace();}
        );

        requestQueue.add(jsonObjectRequest);
    }
}
