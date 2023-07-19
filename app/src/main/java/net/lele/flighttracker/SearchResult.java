package net.lele.flighttracker;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import net.databinding.LeleActivityMainBinding;

public class SearchResult extends AppCompatActivity {
    private LeleActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LeleActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String airportCode = getIntent().getStringExtra("airportCode");

        // TODO: Implement search logic and display the results in the RecyclerView
        Toast.makeText(this, "Searching for flights with airport code: " + airportCode, Toast.LENGTH_SHORT).show();
    }
}
