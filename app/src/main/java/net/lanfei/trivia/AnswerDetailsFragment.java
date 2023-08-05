package net.lanfei.trivia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import net.databinding.LanfeiDetailsLayoutFragmentBinding;

import net.lanfei.trivia.data.Question;

public class AnswerDetailsFragment extends Fragment {
    Question selected;

    /**
     * Constructor for the AnswerDetailsFragment.
     *
     * @param q The selected Question object.
     */
    public AnswerDetailsFragment(Question q) {selected = q;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LanfeiDetailsLayoutFragmentBinding binding = LanfeiDetailsLayoutFragmentBinding.inflate(inflater);

        binding.answerText.setText(selected.getCorrectOption());
        binding.idText.setText("Id = " + selected.getCorrectOption());

        return binding.getRoot();
    }
}