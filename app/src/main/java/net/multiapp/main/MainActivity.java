package net.multiapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import net.R;
import net.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.appSelectionToolbar;
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_currency_converter) {
            Intent intent = new Intent(this, net.matthew.converter.MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.app_bear_generator) {
            Intent intent = new Intent(this, net.zhichuan.bear.MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.app_trivia_question) {
            Intent intent = new Intent(this, net.lanfei.trivia.MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.app_flight_tracker) {
            Intent intent = new Intent(this, net.lele.flighttracker.MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
