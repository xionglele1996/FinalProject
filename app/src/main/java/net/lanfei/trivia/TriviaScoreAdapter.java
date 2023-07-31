package net.lanfei.trivia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.R;
import net.lanfei.trivia.data.TriviaScore;

import java.util.List;

public class TriviaScoreAdapter extends RecyclerView.Adapter<TriviaScoreAdapter.ViewHolder> {

    private List<TriviaScore> triviaScores;


    public TriviaScoreAdapter(List<TriviaScore> triviaScores) {
        this.triviaScores = triviaScores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lanfei_row_score,
                                                                     parent,
                                                                     false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TriviaScore triviaScore = triviaScores.get(position);
        String score = triviaScore.getUsername() + " " + triviaScore.getScore();
        holder.textViewUsername.setText(triviaScore.getUsername());
        holder.textViewScore.setText(triviaScore.getScore());
    }


    @Override
    public int getItemCount() {
        return triviaScores.size();
    }

    public void updateData(List<TriviaScore> newTriviaScores) {
        this.triviaScores = newTriviaScores;
        notifyDataSetChanged();  // Notify the adapter of the data change
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewUsername;
        private final TextView textViewScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewUsername = itemView.findViewById(R.id.textView_username);
            this.textViewScore = itemView.findViewById(R.id.textView_score);
        }
    }
}

