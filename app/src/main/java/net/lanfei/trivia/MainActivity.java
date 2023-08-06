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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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


/**
 * MainActivity class extending AppCompatActivity for Trivia Questions application.
 */
public class MainActivity extends AppCompatActivity {
    private LanfeiActivityMainBinding binding;

    Toolbar toolbar;

    private Spinner spinnerCategory;

    private Button startQuizButton;
    private Button queryScoresButton;

    private EditText amountEdit;
    private EditText usernameEdit;

    private ScoresAdapter triviaScoreAdapter;

    private List<TriviaScore> triviaScores = new ArrayList<TriviaScore>();

    TriviaDatabase db;

    private RecyclerView recyclerViewScores;

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

        // items on the Main Activity layout
        amountEdit = binding.lanfeiQuestionAmount;
        spinnerCategory = findViewById(R.id.category);
        startQuizButton = findViewById(R.id.lanfeiBtnSearch);
        queryScoresButton = findViewById(R.id.lanfeiBtnQuery);
        usernameEdit = findViewById(R.id.lanfeiUsername);

        // Set up the RecyclerView and adapter for query scores
        recyclerViewScores = findViewById(R.id.lanfeRecyclerViewScores);
        triviaScoreAdapter = new ScoresAdapter(triviaScores, this);  // Initialize with empty list
        recyclerViewScores.setAdapter(triviaScoreAdapter);
        recyclerViewScores.setLayoutManager(new LinearLayoutManager(this));

        // initializre the trivia database
        db = Room.databaseBuilder(getApplicationContext(),
                TriviaDatabase.class, "TriviaScore").build();

        // Spinner for the question category
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

        // start quiz button
        startQuizButton.setOnClickListener(clk -> {

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

            //Start the quiz question activity, put the username, amount and topic in next page
            Intent nextPage = new Intent(this, QuizQuestionActivity.class);
            nextPage.putExtra("LoginName", username);
            nextPage.putExtra("Amount", amount);
            nextPage.putExtra("Topic", topic);
            startActivity(nextPage);
        });

        // query the top 10 scores from database
        queryScoresButton.setOnClickListener(clk -> {
            Snackbar.make(this.getCurrentFocus(),
                    "Show the top 10 scores from previous users",
                    Snackbar.LENGTH_LONG).show();

            // query the scores
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
                    .setMessage("Trivia questions with version 1.0 from Fei Lan. \n" +
                            "Input user name. \n" +
                            "Select a topic for new quize questions.\n" +
                            "Input the question amount (1-10).\n" +
                            "Click Start Quiz to start a new quiz\n" +
                            "Click Query Trivia Scores button to display the top 10 scores.\n")
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

            List<TriviaScore> scores = db.triviaScoreDAO().getAllScores();

            runOnUiThread(() -> {
                triviaScoreAdapter.setData(scores);
                triviaScoreAdapter.notifyDataSetChanged();
            });

        }).start();
    }


    /**
     * An adapter for displaying TriviaScore data in a RecyclerView.
     *
     * <p>This adapter is responsible for creating and managing individual view holders for each item
     * in the RecyclerView. It takes a list of TriviaScore data to be displayed and provides methods for
     * setting new data and binding the data to the view holders.
     */
    public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoresViewHolder> {

        Context context;

        private List<TriviaScore> triviaScores;

        /**
         * Constructor for ScoresAdapter.
         *
         * @param data    The list of TriviaScore data to be displayed.
         * @param context The context of the application or activity using the adapter.
         */
        public ScoresAdapter(List<TriviaScore> data, Context context) {

            this.triviaScores = data;
            this.context = context;
        }

        /**
         * Sets new data for the adapter.
         *
         * @param data The new list of TriviaScore data to be displayed.
         */
        public void setData(List<TriviaScore> data) {
            this.triviaScores = data;
        }

        /**
         * Called when the RecyclerView needs a new ViewHolder of the given viewType to represent an item.
         *
         * <p>This method is responsible for inflating the layout for each item view in the RecyclerView.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         * @return A new instance of ScoresViewHolder representing an item view in the RecyclerView.
         */
        @NonNull
        @Override
        public ScoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the layout for each item view in the RecyclerView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lanfei_row_score, parent, false);
            return new ScoresViewHolder(view);
        }

        /**
         * Called by the RecyclerView to display the data at the specified position in the adapter.
         *
         * <p>This method is responsible for binding the data to the view holder, which represents an item
         * in the RecyclerView. It gets the TriviaScore object from the list of triviaScores based on the
         * provided position. Then, it calls the 'bind' method of the ScoresViewHolder to set the data for
         * the individual item view.
         *
         * <p>This method also sets an onClickListener for the 'delete' button in the ScoresViewHolder, which
         * displays an AlertDialog when clicked. The AlertDialog prompts the user to confirm the deletion of
         * the selected TriviaScore entry. If the user confirms the deletion, the method executes the deletion
         * operation in a separate thread to remove the TriviaScore entry from the database using the
         * TriviaScoreDao. If the user chooses not to delete the entry, no action is taken.
         *
         * @param holder   The ScoresViewHolder that holds the view for an individual item in the RecyclerView.
         * @param position The position of the item in the adapter's data set.
         */
        @Override
        public void onBindViewHolder(@NonNull ScoresViewHolder holder, int position) {
            // Bind the data to the view holder
            TriviaScore score = triviaScores.get(position);
            holder.bind(score);

            holder.delete.setOnClickListener(click -> {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Delete this score")
                        .setMessage("Do you want to delete username=" + score.getUsername() +
                                " Score=" + score.getScore() + "?\n").
                        setPositiveButton("Yes", (dialog, cl) -> {

                            new Thread(() -> {
                                db.triviaScoreDAO().delete(score);
                            }).start();

                        }).
                        setNegativeButton("No", (dialog, cl) -> {

                        }).
                        create().show();

                // refresh scores - not working
                queryScores();

            });
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in the adapter's data set.
         */
        @Override
        public int getItemCount() {
            // Return the total number of items in the data set
            return triviaScores.size();
        }

        /**
         * ViewHolder class for holding the views for each item in the RecyclerView.
         */
        public class ScoresViewHolder extends RecyclerView.ViewHolder {

            private TriviaScore score;
            private TextView nameTextView;
            private TextView scoreTextView;
            private Button delete;

            /**
             * Constructor for creating a new ScoresViewHolder instance.
             *
             * @param itemView The View object representing an item in the RecyclerView.
             */
            public ScoresViewHolder(@NonNull View itemView) {
                super(itemView);
                // Initialize the views in the ViewHolder
                this.nameTextView = itemView.findViewById(R.id.textView_username);
                this.scoreTextView = itemView.findViewById(R.id.textView_score);
                this.delete =  itemView.findViewById(R.id.lanfei_button_delete);
            }

            /**
             * Bind the TriviaScore data to the ViewHolder views.
             *
             * @param score The TriviaScore object to be displayed.
             */
            public void bind(TriviaScore score) {
                nameTextView.setText(score.getUsername());
                scoreTextView.setText(String.valueOf(score.getScore()));
                this.score = score;
            }
        }
    }
}