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

/**
 * A simple {@link Fragment} subclass.
 */
public class RowFragment extends Fragment {
    public RowFragment() {
        // Required empty public constructor
    }

    /**
     * Executed when the fragment is created.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This method is called when the fragment is created.
     *
     * @param savedInstanceState The saved instance state.
     */
    @NonNull
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RiverFragmentRowBinding binding = RiverFragmentRowBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}