package net.zhichuan.bear;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.R;
import net.R.string;
import net.databinding.RiverActivityMainBinding;

/**
 * The main activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This method is called when the activity is created.
     *
     * @param savedInstanceState The saved instance state.
     */
    RiverActivityMainBinding binding;

    /**
     * This method is called when the activity is created.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RiverActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.river_frame, ListFragment.newInstance())
                .commit();
    }
}