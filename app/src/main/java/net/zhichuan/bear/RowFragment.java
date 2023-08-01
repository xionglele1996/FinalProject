package net.zhichuan.bear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import net.databinding.RiverFragmentRowBinding;
import org.jetbrains.annotations.NotNull;

public class RowFragment extends Fragment {
    public RowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RiverFragmentRowBinding binding = RiverFragmentRowBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}