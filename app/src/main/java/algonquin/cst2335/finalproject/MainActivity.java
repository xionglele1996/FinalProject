package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private Spinner spinnerFromCurrency;
    private Spinner spinnerToCurrency;
    private Button buttonConvert;
    private Button buttonSave;
    private RecyclerView recyclerViewQueries;
    private ConversionQueryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    AppDatabase.class, "conversion_query_database").build();
            List<ConversionQuery> conversionQueries = db.conversionQueryDao().getAllQueries();
            runOnUiThread(() -> {
                adapter = new ConversionQueryAdapter(new ArrayList<>(), this::deleteQuery);  // Initialize with empty list
                recyclerViewQueries.setAdapter(adapter);

                loadQueries();
            });
        }).start();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                .setTitle("Help")
                .setMessage("This is a currency converter. Enter an amount and select the source and destination currencies. Click the Convert button to see the conversion result, and click the Save button to save the conversion query.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void deleteQuery(ConversionQuery conversionQuery) {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "conversion_query_database").build();
            db.conversionQueryDao().delete(conversionQuery);
            runOnUiThread(this::loadQueries);
        }).start();
    }



    private List<String> getCurrencies() {
        // Replace this with your actual list of currencies
        return new ArrayList<>(Arrays.asList("USD", "EUR", "GBP", "AUD", "CAD"));
    }

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

                        while(keys.hasNext()) {
                            String key = keys.next();
                            JSONObject currencyObject = ratesObject.getJSONObject(key);
                            double rate = currencyObject.getDouble("rate");

                            double convertedAmount = Double.parseDouble(amount) * rate;

                            ConversionQuery conversionQuery = new ConversionQuery(sourceCurrency, destinationCurrency, amount, String.valueOf(convertedAmount));
                            // Save the query into SharedPreferences as well
                            String query = sourceCurrency + "-" + destinationCurrency + "-" + amount;
                            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("lastQuery", query);
                            editor.apply();
                            Toast.makeText(this, "Query saved", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    // Display the error message in a Toast
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }



    private void saveQuery() {
        String sourceCurrency = spinnerFromCurrency.getSelectedItem().toString();
        String destinationCurrency = spinnerToCurrency.getSelectedItem().toString();
        String amount = editTextAmount.getText().toString();
        String convertedAmount = "YOUR_CONVERTED_AMOUNT"; // replace with the actual converted amount

        if(sourceCurrency.isEmpty() || destinationCurrency.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please enter valid data before saving.", Toast.LENGTH_SHORT).show();
            return;
        }

        ConversionQuery conversionQuery = new ConversionQuery(sourceCurrency, destinationCurrency, amount, convertedAmount);
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "conversion_query_database").build();
            db.conversionQueryDao().insert(conversionQuery);
            loadQueries();
        }).start();

        // Save the query into SharedPreferences as well
        String query = sourceCurrency + "-" + destinationCurrency + "-" + amount;
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lastQuery", query);
        editor.apply();

        Toast.makeText(this, "Query saved", Toast.LENGTH_SHORT).show();
    }


    private void loadQueries() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "conversion_query_database").build();
            List<ConversionQuery> conversionQueries = db.conversionQueryDao().getAllQueries();
            runOnUiThread(() -> {
                adapter.updateData(conversionQueries);  // Update the adapter's data
            });
        }).start();
    }






}
