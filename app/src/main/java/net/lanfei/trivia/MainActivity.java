/**
 * This class represents the main activity of the trivia app.
 * It allows the user to search and start quizzes, query scores, and save scores into a local database.
 */
package net.lanfei.trivia;

// Import statements
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.android.material.snackbar.Snackbar;

import net.R;
import net.databinding.LanfeiActivityMainBinding;
import net.lanfei.trivia.data.TriviaDatabase;
import net.lanfei.trivia.data.TriviaScoreDao;

import net.lanfei.trivia.data.TriviaScore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;


/**
 * MainActivity class extending AppCompatActivity.
 */
public class MainActivity extends AppCompatActivity {
    private LanfeiActivityMainBinding binding;

    Toolbar toolbar;

    private Spinner spinnerCategory;

    private Button searchButton;
    private Button queryScoresButton;
    private Button saveButton;

    private EditText amountEdit;
    private EditText usernameEdit;

    private ScoresAdapter triviaScoreAdapter;

    private TriviaScore lastScore = null;
    private int score = 80;

    private List<TriviaScore> triviaScores = new ArrayList<TriviaScore>();

    private TriviaScoreDao triviaDao;

    private RecyclerView recyclerViewScores;

    private Executor thread;

    /**
     * Initializes the main activity and sets up UI components and event listeners.
     *
     * @param savedInstanceState The saved instance state bundle.
     */

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

        usernameEdit = findViewById(R.id.lanfeiUsername);

        recyclerViewScores = findViewById(R.id.lanfeRecyclerViewScores);
        triviaScoreAdapter = new ScoresAdapter(triviaScores, this);  // Initialize with empty list
        recyclerViewScores.setAdapter(triviaScoreAdapter);
        recyclerViewScores.setLayoutManager(new LinearLayoutManager(this));

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
        String lastUser = savedPrefs.getString("LoginName", "");

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
            String username = usernameEdit.getText().toString().trim();

            // save the username, selected topic and amount into SharedPreferences
            editor.putString("LoginName", username);
            editor.putString("Amount", amount);
            editor.putString("Topic", topic);
            editor.commit();

            Toast.makeText(this,
                    "You selected topic is: " + topic + "=" + categoryNum
                            + " and " + amount + " questions",
                    Toast.LENGTH_LONG).show();

            //Start the quiz activity with user name and selected topic
            Intent nextPage = new Intent(this, QuizQuestionActivity.class);
            nextPage.putExtra("LoginName", username);
            nextPage.putExtra("Amount", amount);
            nextPage.putExtra("Topic", topic);
            startActivity(nextPage);
        });

        queryScoresButton.setOnClickListener(clk -> {
            Snackbar.make(this.getCurrentFocus(),
                    "Show the top 10 scores from previous users",
                    Snackbar.LENGTH_LONG).show();

            queryScores();
        });

    }

    /**
     * Inflates the options menu for the activity.
     *
     * @param menu The menu object to inflate.
     * @return True if the menu is successfully inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lanfei_trivia_menu, menu);
        return true;
    }

    /**
     * Handles the selection of items in the options menu.
     *
     * @param item The selected menu item.
     * @return True if the menu item is handled successfully.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle options menu item selection
        if (item.getItemId() == R.id.trivia_help) {
            new AlertDialog.Builder(this)
                    .setTitle("Help")
                    .setMessage("Trivia questions. \n" +
                            "Click Query Trivia Scores button to display the last 10 scores.\n" +
                            "Select a topic for new quize questions.\n" +
                            "Click Start Quiz to list questions\n")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieves a list of trivia categories.
     *
     * @return A list of trivia categories.
     */
    private List<String> getCategories() {
        // Replace this with your actual list of categories
        return new ArrayList<>(Arrays.asList("Mythology", "Sports", "Geography", "History", "Art"));
    }

    /**
     * Maps a category name to its corresponding category number.
     *
     * @param category The category name to map.
     * @return The category number corresponding to the given category name.
     */
    private String getCategoryNumber(String category) {
        // Map category name to category number
        if (category.equalsIgnoreCase("Mythology")) return "20";
        else if (category.equalsIgnoreCase("Sports")) return "21";
        else if (category.equalsIgnoreCase("Geography")) return "22";
        else if (category.equalsIgnoreCase("History")) return "23";
        else if (category.equalsIgnoreCase("Politics")) return "24";
        else if (category.equalsIgnoreCase("Art")) return "25";
        else return "22";
    }

    /**
     * Queries the top 10 scores from the local database and updates the RecyclerView adapter.
     */
    private void queryScores() {
        new Thread(() -> {

            List<TriviaScore> scores = triviaDao.getAllScores();

            runOnUiThread(() -> {
                triviaScoreAdapter.setData(scores);
                triviaScoreAdapter.notifyDataSetChanged();
            });

        }).start();
    }
}