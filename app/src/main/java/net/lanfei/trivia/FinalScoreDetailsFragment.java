package net.lanfei.trivia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import net.databinding.LanfeiFinalScoreDetailsFragmentBinding;

/**
 * A fragment to display the final score details for a quiz.
 */
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


    /**
     * Called to have the fragment instantiate its user interface view. This is optional,
     * and non-graphical fragments can return null (which is the default implementation). This
     * will be called between {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself, but this can be used to generate
     *                  the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
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