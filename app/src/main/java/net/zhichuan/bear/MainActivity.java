package net.zhichuan.bear;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import net.R;
import net.databinding.RiverActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    RiverActivityMainBinding binding;

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