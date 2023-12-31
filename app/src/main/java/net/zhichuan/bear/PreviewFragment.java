package net.zhichuan.bear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import net.R;
import net.databinding.RiverFragmentPreviewBinding;
import net.zhichuan.bear.utils.DownloadImage;
import net.zhichuan.bear.utils.ImageEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_WIDTH = "width";
    private static final String ARG_HEIGHT = "height";
    RiverFragmentPreviewBinding binding;
    private int mWidth;
    private int mHeight;

    private ImageView preview;
    private Button retry;
    private Button save;

    /**
     * The format of the date.
     * It is used to format the date into a string.
     */
    public PreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param width  the width of the image.
     * @param height the height of the image.
     * @return A new instance of fragment PreviewFragment.
     */
    @NonNull
    public static PreviewFragment newInstance(int width, int height) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WIDTH, width);
        args.putInt(ARG_HEIGHT, height);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method is called when the fragment is created.
     * It is used to get the width and height of the image.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWidth = getArguments().getInt(ARG_WIDTH);
            mHeight = getArguments().getInt(ARG_HEIGHT);
        }
    }

    /**
     * This method is called when the fragment is created.
     * It is used to inflate the layout of the fragment.
     *
     * @param inflater           The inflater used to inflate the layout.
     * @param container          The container used to inflate the layout.
     * @param savedInstanceState The saved instance state.
     * @return The inflated layout.
     */
    @NonNull
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        binding = RiverFragmentPreviewBinding.inflate(inflater);

        preview = binding.riverPreview;
        retry = binding.riverRetry;
        save = binding.riverSave;

        DownloadImage.downloadImage(mWidth, mHeight, binding.riverPreview);

        retry.setOnClickListener(clk -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.river_frame, GeneratorFragment.newInstance())
                .addToBackStack("")
                .commit());

        save.setOnClickListener(clk -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.river_frame,
                         ListFragment.newInstance(new ImageEntity(mWidth, mHeight, new Date().getTime())))
                .addToBackStack("")
                .commit());

        return binding.getRoot();
    }
}