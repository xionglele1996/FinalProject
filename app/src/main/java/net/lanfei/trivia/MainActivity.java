package net.lanfei.trivia;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.snackbar.Snackbar;

import net.R;
import net.databinding.LanfeiActivityMainBinding;
import net.lanfei.trivia.data.TriviaDatabase;
import net.lanfei.trivia.data.TriviaScoreDao;

//import net.matthew.converter.AppDatabase;
//import net.matthew.converter.ConversionQuery;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import net.lanfei.trivia.data.TriviaScore;
import net.matthew.converter.AppDatabase;
import net.matthew.converter.ConversionQuery;
import net.matthew.converter.ConversionQueryAdapter;


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
    private Button saveButton;

    private EditText amountEdit;
    private EditText usernameEdit;

   // private RecyclerView.Adapter myAdapter;

    private TriviaScoreAdapter trivisScoreAdapter;

    private TriviaScore lastScore = null;
    private int score = 70;

    private TriviaScoreDao triviaDao;

    private RecyclerView recyclerViewScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LanfeiActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.triviaToolbar;
        setSupportActionBar(toolbar);

        amountEdit = binding.lanfeiQuestionAmount;
        spinnerCategory = findViewById(R.id.category);
        searchButton = findViewById(R.id.lanfeiBtnSearch);
        queryScoresButton = findViewById(R.id.lanfeiBtnQuery);
        saveButton = findViewById(R.id.lanfeiBtnSave);
        usernameEdit = findViewById(R.id.lanfeiUsername);

        recyclerViewScores = findViewById(R.id.lanfeRecyclerViewScores);
        recyclerViewScores.setLayoutManager(new LinearLayoutManager(this));

        trivisScoreAdapter = new TriviaScoreAdapter(new ArrayList<>());  // Initialize with empty list
        recyclerViewScores.setAdapter(trivisScoreAdapter);

        TriviaDatabase db = Room.databaseBuilder(getApplicationContext(),
                TriviaDatabase.class, "TriviaScore").build();

        triviaDao = db.triviaScoreDAO();

        List<String> categories = getCategories();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerCategory.setAdapter(spinnerAdapter);

        //sharedPreference
        SharedPreferences savedPrefs = getSharedPreferences("myFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedPrefs.edit();
        String lastAmount = savedPrefs.getString("Amount", "");
        String lastTopic = savedPrefs.getString("Topic", "");
        String lastUser = savedPrefs.getString("User", "");

        if (!lastAmount.isEmpty()) {
            amountEdit.setText(lastAmount);
            spinnerCategory.setSelection(categories.indexOf(lastTopic));
        }

        if (!lastUser.isEmpty()) {
            usernameEdit.setText(lastUser);
        }

        searchButton.setOnClickListener(clk -> {

            String topic = spinnerCategory.getSelectedItem().toString();
            String categoryNum = getCategoryNumber(spinnerCategory.getSelectedItem().toString());
            String amount = amountEdit.getText().toString().trim();

            // save the selected topic into SharedPreferences
            editor.putString("Amount", amount);
            editor.putString("Topic", topic);
            editor.apply();

            Toast.makeText(this,
                    "You selected topic for the trivia question is: " + topic + "=" + categoryNum,
                    Toast.LENGTH_LONG).show();

            searchQuestions(categoryNum, amount);
        });

        queryScoresButton.setOnClickListener(clk -> {
            Snackbar.make(this.getCurrentFocus(),
                    "Show the top 10 scores from previous users",
                    Snackbar.LENGTH_LONG).show();

            queryScores();
        });

        saveButton.setOnClickListener(clk -> {
            String username = usernameEdit.getText().toString().trim();

            // save the selected topic into SharedPreferences
            editor.putString("User", username);
            editor.apply();

            score++; // will be calculated

            new AlertDialog.Builder(this)
                    .setTitle("Save Quiz Score")
                    .setMessage(username + " quiz score is " + score +
                            ". Save the score into database")
                    .setPositiveButton("OK", null)
                    .show();
            lastScore = new TriviaScore(username, score);

            saveScore();
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
        } else if (item.getItemId() == R.id.quiz_question) {
            Intent intent = new Intent(this, net.lanfei.trivia.TriviaQuestionActivity.class);
            startActivity(intent);
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
                        // JSONObject results = response.getJSONObject("results");
                        // Iterator<String> keys = results.keys();

                        JSONArray results = response.getJSONArray("results");
                        int numOfQuestions = results.length();

                        // while (keys.hasNext()) {
                        //     String key = keys.next();
                        JSONObject questionObj = results.getJSONObject(0);
                        String question = questionObj.getString("question");

                        // Update UI on main thread
                        runOnUiThread(() -> {
                            // Show the conversion result and the rate in a Toast
                            Toast.makeText(this, "Question: " + question, Toast.LENGTH_SHORT).show();
                        });
                        // }

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

    private void searchQuestions(String category, String amount) {

        String url = "https://opentdb.com/api.php?amount=" + amount + "&category=" + category + "&type=multiple";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Log.d("API_RESPONSE", "Response: " + response.toString());
                    try {

                        JSONArray results = response.getJSONArray("results");
                        int numOfQuestions = results.length();

                        if (numOfQuestions > 0) {
                            JSONObject questionObject = results.getJSONObject(0);

                            String question = questionObject.getString("question");

                            // Update UI on main thread
                            runOnUiThread(() -> {
                                // Show the question result in a Toast
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
        queue.add(jsonObjectRequest);
    }

    private void saveScore() {
        if (lastScore == null) {
            Toast.makeText(this, "Please do a quiz before saving.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            //   TriviaDatabase db = Room.databaseBuilder(getApplicationContext(),
            //                  TriviaDatabase.class, "TriviaScore")
            //          .fallbackToDestructiveMigration()
            //         .build();
            triviaDao.insert(lastScore);
            // loadQueries();
        }).start();

        // Save the query into SharedPreferences as well
        // String query = lastScore.getUsername() + "-" + lastScore.getScore();
        // SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = sharedPref.edit();
        // editor.putString("lastScore", query);
        // editor.apply();

        Toast.makeText(this, "Score saved", Toast.LENGTH_SHORT).show();
        lastScore = null; // Clear the last score result after saving
    }

    private void queryScores() {
        new Thread(() -> {

            List<TriviaScore> scores = triviaDao.getAllScores();

          //  runOnUiThread(() -> {
           //     trivisScoreAdapter.updateData(scores);
           // });
        }).start();
    }

}