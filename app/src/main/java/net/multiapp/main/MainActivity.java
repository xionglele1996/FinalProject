package net.multiapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import net.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final TextView[] apps = new TextView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apps[0] = binding.app1;
        apps[1] = binding.app2;
        apps[2] = binding.app3;
        apps[3] = binding.app4;

        apps[0].setText("Currency Converter");
        apps[1].setText("Bear Generator");

        apps[0].setOnClickListener(click -> {
            Intent intent = new Intent(this, net.matthew.converter.MainActivity.class);
            startActivity(intent);
        });

        apps[1].setOnClickListener(click -> {
            Intent intent = new Intent(this, net.zhichuan.bear.MainActivity.class);
            startActivity(intent);
        });
    }
}
