package net.lanfei.trivia;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Adapter for displaying TriviaScore data in a RecyclerView.
 */
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoresViewHolder> {

    Context context;

    private List<TriviaScore> triviaScores;

    /**
     * Constructor for ScoresAdapter.
     *
     * @param data The list of TriviaScore data to be displayed.
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

    @NonNull
    @Override
    public ScoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item view in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lanfei_row_score, parent, false);
        return new ScoresViewHolder(view);
    }

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

                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            TriviaDatabase db = TriviaDatabase.getInstance(context);
                            TriviaScoreDao triviaDao = db.triviaScoreDAO();
                            triviaDao.delete(score);
                        });

                    }).
                    setNegativeButton("No", (dialog, cl) -> {

                    }).
                    create().show();

                });
    }

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
        private TextView delete;

        public ScoresViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views in the ViewHolder
            this.nameTextView = itemView.findViewById(R.id.textView_username);
            this.scoreTextView = itemView.findViewById(R.id.textView_score);
            this.delete =  itemView.findViewById(R.id.textView_delete);
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
