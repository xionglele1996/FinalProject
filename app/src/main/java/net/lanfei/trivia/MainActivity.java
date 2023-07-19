package net.lanfei.trivia;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import net.R;
import net.databinding.LanfeiActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private LanfeiActivityMainBinding binding;
    private EditText category;

    private RecyclerView.Adapter myAdapter;
    private String triviaUrl = "https://opentdb.com/api.php?amount=10&category=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LanfeiActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        category = binding.lanfeiCategory;

        //sharedPreference
        SharedPreferences savedPrefs = getSharedPreferences("myFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedPrefs.edit();
        String code = savedPrefs.getString("Category", "");
        category.setText(code);

        binding.lanfeiBtnSearch.setOnClickListener(clk -> {
            String value = category.getText().toString().trim();

           /* AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do you want to continue:" + value)
                    .setTitle("Question:")
                    .setNegativeButton("No", (dialog, cl) -> {
                    })
                    .setPositiveButton("Yes", (dialog, cl) -> {
                    })
                    .create()
                    .show();
            */
            if (!checkCategory(value)) {
                Snackbar.make(this.getCurrentFocus(),
                              "Warning: Question Category should be a number between 20 and 30",
                              Snackbar.LENGTH_LONG).show();
            } else {
                editor.putString("Category", value);
                editor.commit();
                //make toast
                Toast.makeText(this,
                               "Your question category is: " + selectTopic(value),
                               Toast.LENGTH_LONG).show();

                triviaUrl += value + "&type=multiple";
            }
        });

        binding.lanfeiBtnScores.setOnClickListener(clk -> {

            // TODO
            Snackbar.make(this.getCurrentFocus(),
                          "Show the top 10 high scores from previous users",
                          Snackbar.LENGTH_LONG).show();
        });

        binding.lanfeiBtnQuiz.setOnClickListener(clk -> {
            // TODO
            Snackbar.make(this.getCurrentFocus(),
                          "Input your name to start a quiz",
                          Snackbar.LENGTH_LONG).show();
        });
    }

    private boolean checkCategory(String value) {
        if (value == null)
            return false;

        // validate the number between 20 and 30
        return (value.matches("2[0-9]|30"));
    }

    private String selectTopic(String value) {
        int categoryNum = Integer.valueOf(value);
        String topic;

        switch (categoryNum) {
            case 20:
                topic = "Mythology";
                break;
            case 21:
                topic = "Sports";
                break;
            case 22:
                topic = "Geography";
                break;
            case 23:
                topic = "History";
                break;
            case 24:
                topic = "Politics";
                break;
            case 25:
                topic = "Art";
                break;
            case 26:
                topic = "Celebrities";
                break;
            case 27:
                topic = "Animals";
                break;
            case 28:
                topic = "Vehicles";
                break;
            case 29:
                topic = "Entertainment";
                break;
            case 30:
                topic = "Science";
                break;
            default:
                topic = "Geography";
        }
        return topic;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.lanfei_about) {
            Toast.makeText(getApplicationContext(), "Version 1.0, Trivia Questions created by Fei Lan",
                           Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lanfei_trivia_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

}