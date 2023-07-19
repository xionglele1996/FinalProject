package net.zhichuan.bear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import net.databinding.RiverFragmentRowBinding;
import org.jetbrains.annotations.NotNull;

public class RowFragment extends Fragment {
    public RowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RiverFragmentRowBinding binding = RiverFragmentRowBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}