package net.matthew.converter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import net.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The main activity class for the currency converter application.
 * It provides functionalities to convert currencies, save queries, and view details of conversion queries.
 */
public class MainActivity extends AppCompatActivity implements ConversionQueryAdapter.OnItemClickListener {

    private EditText editTextAmount;
    private Spinner spinnerFromCurrency;
    private Spinner spinnerToCurrency;
    private Button buttonConvert;
    private Button buttonSave;
    private RecyclerView recyclerViewQueries;
    private ConversionQueryAdapter adapter;
    private ConversionQuery lastConversionResult;
    private Button buttonViewSavedQueries;


    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matthew_activity_main);

        editTextAmount = findViewById(R.id.editText_amount);
        spinnerFromCurrency = findViewById(R.id.spinner_from_currency);
        spinnerToCurrency = findViewById(R.id.spinner_to_currency);
        buttonConvert = findViewById(R.id.button_convert);
        buttonSave = findViewById(R.id.button_save);

        List<String> currencies = getCurrencies();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        spinnerFromCurrency.setAdapter(spinnerAdapter);
        spinnerToCurrency.setAdapter(spinnerAdapter);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String lastQuery = sharedPref.getString("lastQuery", "");

        // If there is a saved query, set it in the EditText and Spinners
        if (!lastQuery.isEmpty()) {
            String[] parts = lastQuery.split("-");
            spinnerFromCurrency.setSelection(currencies.indexOf(parts[0]));
            spinnerToCurrency.setSelection(currencies.indexOf(parts[1]));
            editTextAmount.setText(parts[2]);
        }

        buttonConvert.setOnClickListener(v -> convertCurrency());
        buttonSave.setOnClickListener(v -> saveQuery());

        recyclerViewQueries = findViewById(R.id.recyclerView_queries);
        recyclerViewQueries.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "conversion_query_database")
                    .fallbackToDestructiveMigration()
                    .build();

            List<ConversionQuery> conversionQueries = db.conversionQueryDao().getAllQueries();
            runOnUiThread(() -> {
                adapter = new ConversionQueryAdapter(new ArrayList<>(), new ConversionQueryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ConversionQuery conversionQuery) {
                        deleteQuery(conversionQuery);
                    }

                    @Override
                    public void onItemDetailClick(ConversionQuery conversionQuery) {
                        showDetail(conversionQuery);
                    }
                });
                recyclerViewQueries.setAdapter(adapter);
                adapter.updateData(conversionQueries);  // Update the adapter's data
            });
        }).start();



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonViewSavedQueries = findViewById(R.id.button_view_saved_queries);

        buttonViewSavedQueries.setOnClickListener(v -> {
            if (recyclerViewQueries.getVisibility() == View.GONE) {
                recyclerViewQueries.setVisibility(View.VISIBLE);
            } else {
                recyclerViewQueries.setVisibility(View.GONE);
            }
        });

    }

    /**
     * Shows the details of a specific conversion query.
     *
     * @param conversionQuery The conversion query to display.
     */
    public void showDetail(ConversionQuery conversionQuery) {
        ConversionDetailFragment detailFragment = ConversionDetailFragment.newInstance(conversionQuery);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }



    @Override
    public void onItemDetailClick(ConversionQuery conversionQuery) {
        showDetail(conversionQuery);
    }

    @Override
    public void onItemClick(ConversionQuery conversionQuery) {
        // Do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.matthew_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.matthew_help))
                .setMessage(getString(R.string.matthew_help_message))
                .setPositiveButton(getString(R.string.matthew_ok), null)
                .show();
    }


    private void deleteQuery(ConversionQuery conversionQuery) {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "conversion_query_database")
                    .fallbackToDestructiveMigration()
                    .build();

            db.conversionQueryDao().delete(conversionQuery);

            // Run on UI thread to update RecyclerView and show Snackbar
            runOnUiThread(() -> {
                loadQueries();

                // Assuming your activity's root layout has an id of "root_layout"
                View rootView = findViewById(android.R.id.content);

                // Show a Snackbar with an Undo action
                Snackbar snackbar = Snackbar.make(rootView, getString(R.string.matthew_query_deleted), Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.matthew_undo), view -> {
                    // When the Undo action is clicked, re-insert the deleted query
                    new Thread(() -> {
                        db.conversionQueryDao().insert(conversionQuery);
                        runOnUiThread(this::loadQueries);
                    }).start();
                });
                snackbar.show();
            });
        }).start();
    }


//    private void closeDatabase(AppDatabase db) {
//        if (db.isOpen()) {
//            db.close();
//        }
//    }

    private List<String> getCurrencies() {
        // Replace this with your actual list of currencies
        return new ArrayList<>(Arrays.asList("USD", "EUR", "GBP", "AUD", "CAD"));
    }

    /**
     * Converts currency using an API call and updates the UI with the result.
     */
    private void convertCurrency() {
        String sourceCurrency = spinnerFromCurrency.getSelectedItem().toString();
        String destinationCurrency = spinnerToCurrency.getSelectedItem().toString();
        String amount = editTextAmount.getText().toString();

        String url = "https://api.getgeoapi.com/v2/currency/convert?format=json&from=" + sourceCurrency + "&to=" + destinationCurrency + "&amount=" + amount + "&api_key=fa3353a8571e455f5e31a1686e119caaf59e3b12&format=json";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Log.d("API_RESPONSE", "Response: " + response.toString());
                    try {
                        JSONObject ratesObject = response.getJSONObject("rates");
                        Iterator<String> keys = ratesObject.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject currencyObject = ratesObject.getJSONObject(key);
                            double rate = currencyObject.getDouble("rate");

                            double convertedAmount = Double.parseDouble(amount) * rate;
                            lastConversionResult = new ConversionQuery(sourceCurrency, destinationCurrency, amount, String.valueOf(convertedAmount));

                            // Update UI on main thread
                            runOnUiThread(() -> {
                                // Show the conversion result and the rate in a Toast
                                Toast.makeText(this, getString(R.string.matthew_converted_amount) + convertedAmount + getString(R.string.matthew_rate) + rate, Toast.LENGTH_SHORT).show();
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.d("API_RESPONSE", "Error: " + error.getMessage());
                    // Display the error message in a Toast
                    Toast.makeText(this, getString(R.string.matthew_error) + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    /**
     * Saves the last conversion query to the database and shared preferences.
     */
    private void saveQuery() {
        if (lastConversionResult == null) {
            Toast.makeText(this, getString(R.string.matthew_please_convert_before_saving), Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "conversion_query_database")
                    .fallbackToDestructiveMigration()
                    .build();
            db.conversionQueryDao().insert(lastConversionResult);
            loadQueries();
        }).start();

        // Save the query into SharedPreferences as well
        String query = lastConversionResult.getSourceCurrency() + "-" + lastConversionResult.getDestinationCurrency() + "-" + lastConversionResult.getAmount();
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lastQuery", query);
        editor.apply();

        Toast.makeText(this, getString(R.string.matthew_query_saved), Toast.LENGTH_SHORT).show();
        lastConversionResult = null; // Clear the last conversion result after saving
    }

    /**
     * Loads the saved conversion queries from the database.
     */
    private void loadQueries() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "conversion_query_database")
                    .fallbackToDestructiveMigration()
                    .build();
            List<ConversionQuery> conversionQueries = db.conversionQueryDao().getAllQueries();
            runOnUiThread(() -> {
                adapter.updateData(conversionQueries);  // Update the adapter's data
            });
        }).start();
    }
}