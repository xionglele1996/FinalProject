package net.zhichuan.bear;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    SharedPreferences sharedPreferences;

    public GeneratorFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static GeneratorFragment newInstance() {
        GeneratorFragment fragment = new GeneratorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RiverFragmentGeneratorBinding.inflate(getLayoutInflater());

        width = binding.riverWidth;
        height = binding.riverHeight;
        generate = binding.riverGenerate;


        sharedPreferences = requireActivity().getSharedPreferences("net.zhichuan.bear",
                                                                   Activity.MODE_PRIVATE);
        int savedWidth = sharedPreferences.getInt(String.valueOf(R.string.river_imageWidth), 0);
        int savedHeight = sharedPreferences.getInt(String.valueOf(R.string.river_imageHeight), 0);

        if (savedWidth != 0) {
            width.setText(String.valueOf(savedWidth));
        }

        if (savedHeight != 0) {
            height.setText(String.valueOf(savedHeight));
        }

        generate.setOnClickListener(clk -> {
            int imageWidth = Integer.parseInt(String.valueOf(width.getText()));
            int imageHeight = Integer.parseInt(String.valueOf(height.getText()));

            sharedPreferences.edit().putInt(String.valueOf(R.string.river_imageWidth), imageWidth).apply();
            sharedPreferences.edit().putInt(String.valueOf(R.string.river_imageHeight), imageHeight).apply();
//            create the image url and pass it to the preview fragment
            PreviewFragment previewFragment = PreviewFragment.newInstance(imageWidth, imageHeight);

            Toast.makeText(getActivity(), "Generating a " + imageWidth + " x "
                    + imageHeight + " image...", Toast.LENGTH_LONG).show();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.river_frame, previewFragment)
                    .addToBackStack("")
                    .commit();
        });
        return binding.getRoot();
    }
}