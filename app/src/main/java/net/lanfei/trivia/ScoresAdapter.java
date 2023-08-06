package net.lanfei.trivia;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.R;
import net.lanfei.trivia.data.TriviaDatabase;
import net.lanfei.trivia.data.TriviaScore;
import net.lanfei.trivia.data.TriviaScoreDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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
    public void onBindViewHolder(ScoresViewHolder holder, int position) {
        // Bind the data to the view holder
        TriviaScore score = triviaScores.get(position);
        holder.bind(score);

        holder.delete.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete this score")
                    .setMessage("Do you want to delete username=" + score.getUsername() +
                            " Score=" + score.getScore() + "?\n").
                    setPositiveButton("Yes", (dialog, cl) -> {

                        new Thread(() -> {
                            TriviaDatabase db = TriviaDatabase.getInstance(context);
                            db.triviaScoreDAO().delete(score);
                        }).start();

                    }).
                    setNegativeButton("No", (dialog, cl) -> {

                    }).
                    create().show();

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
