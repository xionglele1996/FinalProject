package net.zhichuan.bear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import net.zhichuan.bear.databinding.RiverFragmentRowBinding;

public class RowFragment extends Fragment {
    public RowFragment() {
        // Required empty public constructor
    }

    public static RowFragment newInstance() {
        return new RowFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RiverFragmentRowBinding binding = RiverFragmentRowBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}