package net.lanfei.trivia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import net.databinding.LanfeiFinalScoreDetailsFragmentBinding;

public class FinalScoreDetailsFragment extends Fragment {
    String name;
    String amount;
    String category;

    String correctAnswers;

    int score;

    /**
     * Constructor for the FinalScoreDetailsFragment.
     *
     * @param name the user name
     * @param category the selected category
     * @param amount the selected amount
     * @param score the quiz score
     */
    public FinalScoreDetailsFragment(String name, String category, String amount, String correctAnswers, int score) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.correctAnswers = correctAnswers;
        this.score = score;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LanfeiFinalScoreDetailsFragmentBinding binding = LanfeiFinalScoreDetailsFragmentBinding.inflate(inflater);

        binding.quizUsername.setText("User: " + name);
        binding.QuizCategory.setText("Category: " + category);
        binding.QuizAmount.setText("Number of Questions: " + amount);
        binding.QuizCorrectAnswers.setText("Correct Answers: " + correctAnswers);
        binding.QuizScore.setText("Final Score: " + score);
        return binding.getRoot();
    }
}