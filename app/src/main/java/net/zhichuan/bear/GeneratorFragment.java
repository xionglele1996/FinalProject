package net.zhichuan.bear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import net.R;
import net.databinding.RiverFragmentGeneratorBinding;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneratorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneratorFragment extends Fragment {
    RiverFragmentGeneratorBinding binding;

    private EditText width;
    private EditText height;
    private Button generate;

    public GeneratorFragment() {
        // Required empty public constructor
    }

    public static GeneratorFragment newInstance() {
        GeneratorFragment fragment = new GeneratorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RiverFragmentGeneratorBinding.inflate(getLayoutInflater());

        width = binding.riverWidth;
        height = binding.riverHeight;
        generate = binding.riverGenerate;

        generate.setOnClickListener(clk -> {
            int imageWidth = Integer.parseInt(String.valueOf(width.getText()));
            int imageHeight = Integer.parseInt(String.valueOf(height.getText()));

//            create the image url and pass it to the preview fragment
            PreviewFragment previewFragment = PreviewFragment.newInstance(imageWidth, imageHeight);

            Toast.makeText(getActivity(), "Generating a " + imageWidth + " x "
                    + imageHeight + " image...", Toast.LENGTH_LONG).show();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.river_frame, previewFragment)
                    .addToBackStack(null)
                    .commit();
        });
        return binding.getRoot();
    }
}