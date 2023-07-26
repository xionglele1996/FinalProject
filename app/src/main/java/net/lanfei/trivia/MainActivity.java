package net.lanfei.trivia;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import net.R;
import net.databinding.LanfeiActivityMainBinding;
import net.matthew.converter.ConversionQuery;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LanfeiActivityMainBinding binding;

    Toolbar toolbar;

    private Spinner spinnerCategory;

    private Button searchButton;
    private Button queryScoresButton;
    private Button saveQuizButton;

    private EditText trivianame;

    private RecyclerView.Adapter myAdapter;
    private String triviaUrl = "https://opentdb.com/api.php?amount=10&category=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LanfeiActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.triviaToolbar;
        setSupportActionBar(toolbar);

        trivianame = binding.lanfeiTrivianame;
        spinnerCategory = findViewById(R.id.category);
        searchButton = findViewById(R.id.lanfeiBtnSearch);
        queryScoresButton = findViewById(R.id.lanfeiBtnQuery);

        List<String> categories = getCategories();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerCategory.setAdapter(spinnerAdapter);

        //sharedPreference
        SharedPreferences savedPrefs = getSharedPreferences("myFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedPrefs.edit();
        String lastUser = savedPrefs.getString("TriviaName", "");
        String lastTopic = savedPrefs.getString("Topic", "");
        if (!lastUser.isEmpty()) {
            trivianame.setText(lastUser);
            spinnerCategory.setSelection(categories.indexOf(lastTopic));
        }

        searchButton.setOnClickListener(clk -> {

            String topic = spinnerCategory.getSelectedItem().toString();
            String categoryNum = getCategoryNumber(spinnerCategory.getSelectedItem().toString());

            // save the selected topic into SharedPreferences
            editor.putString("Topic", topic);
            editor.commit();

            Toast.makeText(this,
                    "You selected topic for the trivia question is: " + topic + "=" + categoryNum,
                    Toast.LENGTH_LONG).show();

            searchTriviaQuestions(categoryNum);

        });

        queryScoresButton.setOnClickListener(clk -> {
            Snackbar.make(this.getCurrentFocus(),
                    "Show the top 10 scores from previous users",
                    Snackbar.LENGTH_LONG).show();
        });

        binding.lanfeiBtnQuiz.setOnClickListener(clk -> {

            // Save the input into SharedPreferences as well
            String name = trivianame.getText().toString().trim();
            editor.putString("TriviaName", name);
            editor.commit();

            int score = 80; // will be calculated

            new AlertDialog.Builder(this)
                    .setTitle("Save Quiz Score")
                    .setMessage("Your quiz score is " + score +
                            ". Save the score into database")
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lanfei_trivia_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.trivia_help) {
            new AlertDialog.Builder(this)
                    .setTitle("Help")
                    .setMessage("Trivia questions. \n" +
                            "Click Display Score button to display the last 10 scores.\n" +
                            "Select a topic for new quize questions.\n" +
                            "Click Search Questions to list quiz questions\n" +
                            "Click Save button to save the quize score.")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<String> getCategories() {
        // Replace this with your actual list of categories
        return new ArrayList<>(Arrays.asList("Mythology", "Sports", "Geography", "History", "Art"));
    }

    private void searchTriviaQuestions(String categoryNum) {
        //String categoryNum = getCategoryNumber (spinnerCategory.getSelectedItem().toString());
        String url = "https://opentdb.com/api.php?amount=5&category=" + categoryNum + "&type=multiple";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Log.d("API_RESPONSE", "Response: " + response.toString());

                    try {
                        JSONObject results = response.getJSONObject("results");
                        Iterator<String> keys = results.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject questionObj = results.getJSONObject(key);
                            String question = questionObj.getString("question");

                            // Update UI on main thread
                            runOnUiThread(() -> {
                                // Show the conversion result and the rate in a Toast
                                Toast.makeText(this, "Question: " + question, Toast.LENGTH_SHORT).show();
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.d("API_RESPONSE", "Error: " + error.getMessage());
                    // Display the error message in a Toast
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue.
        //queue.add(jsonObjectRequest);

    }

    private String getCategoryNumber(String category) {
        if (category.equalsIgnoreCase("Mythology")) return "20";
        else if (category.equalsIgnoreCase("Sports")) return "21";
        else if (category.equalsIgnoreCase("Geography")) return "22";
        else if (category.equalsIgnoreCase("History")) return "23";
        else if (category.equalsIgnoreCase("Politics")) return "24";
        else if (category.equalsIgnoreCase("Art")) return "25";
        else return "22";
    }
}