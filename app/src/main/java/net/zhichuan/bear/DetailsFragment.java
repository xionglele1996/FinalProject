package net.zhichuan.bear;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.databinding.FragmentDetailsBinding;
import net.zhichuan.bear.utils.DownloadImage;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    /**
     * The format of the date.
     * It is used to format the date into a string.
     */

    @SuppressLint("SimpleDateFormat")
    private static final String ARG_WIDTH = "width";
    private static final String ARG_HEIGHT = "height";
    private static final String ARG_DATE = "date";

    private int width;
    private int height;
    private long date;

    private FragmentDetailsBinding binding;

    /**
     * The required empty public constructor.
     */
    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param width  The width of the image.
     * @param height The height of the image.
     * @param date   The creation date of the image.
     * @return A new instance of fragment DetailsFragment.
     */
    @NonNull
    public static DetailsFragment newInstance(int width, int height, long date) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WIDTH, width);
        args.putInt(ARG_HEIGHT, height);
        args.putLong(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is created.
     * It gets the arguments passed to the fragment and stores them in the class.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            width = getArguments().getInt(ARG_WIDTH);
            height = getArguments().getInt(ARG_HEIGHT);
            date = getArguments().getLong(ARG_DATE);
        }
    }

    /**
     * Called when the fragment is created.
     * It creates the view of the fragment.
     *
     * @param inflater           The layout inflater.
     * @param container          The view group container.
     * @param savedInstanceState The saved instance state.
     * @return The view of the fragment.
     */
    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        binding.riverDetailsWidth.setText(String.valueOf(width));
        binding.riverDetailsHeight.setText(String.valueOf(height));
        binding.riverDetailsUrl.setText(String.format("%s/%d/%d", DownloadImage.DOWNLOAD_URL, width, height));

        String formattedDate = SimpleDateFormat.getDateInstance().format(new Date(date));
        binding.riverDetailsDate.setText(formattedDate);

        return binding.getRoot();
    }
}