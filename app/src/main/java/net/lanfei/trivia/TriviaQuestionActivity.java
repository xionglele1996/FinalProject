package net.lanfei.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TriviaQuestionActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;
    private Button nextButton;
    private int currentQuestion = 0;
    private int correctAnswers = 0;

    private String[] questions;
    private String[][] options;
    private int[] correctAnswersIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lanfei_activity_question);

        questionTextView = findViewById(R.id.questionTextView);
        option1RadioButton = findViewById(R.id.option1RadioButton);
        option2RadioButton = findViewById(R.id.option2RadioButton);
        option3RadioButton = findViewById(R.id.option3RadioButton);
        option4RadioButton = findViewById(R.id.option4RadioButton);
        nextButton = findViewById(R.id.nextButton);

        //int category = getIntent().getIntExtra("category", 0);
       // int questionCount = getIntent().getIntExtra("questionCount", 0);

        int category = 20;
        int questionCount = 5;

        // Create the API URL
        String apiUrl = "https://opentdb.com/api.php?amount=" + questionCount + "&category=" + category + "&type=multiple";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response and store the questions, options, and correct answers in the arrays.
                            JSONArray results = response.getJSONArray("results");
                            int numOfQuestions = results.length();

                            questions = new String[numOfQuestions];
                            options = new String[numOfQuestions][4];
                            correctAnswersIndex = new int[numOfQuestions];

                            for (int i = 0; i < numOfQuestions; i++) {
                                JSONObject questionObject = results.getJSONObject(i);
                                questions[i] = questionObject.getString("question");
                                options[i][0] = questionObject.getString("correct_answer");
                                JSONArray incorrectAnswersArray = questionObject.getJSONArray("incorrect_answers");
                                options[i][1] = incorrectAnswersArray.getString(0);
                                options[i][2] = incorrectAnswersArray.getString(1);
                                options[i][3] = incorrectAnswersArray.getString(2);

                                // Set the index of the correct answer in the options array
                                correctAnswersIndex[i] = 0;
                            }

                            displayQuestion(currentQuestion);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors if any
                Toast.makeText(TriviaQuestionActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        nextButton.setOnClickListener(view -> {
            if (currentQuestion < questionCount - 1) {
                checkAnswer(currentQuestion);
                currentQuestion++;
                displayQuestion(currentQuestion);
            } else {
                checkAnswer(currentQuestion);
              //  showFinalScore();
            }
        });

    }

    private void displayQuestion(int questionIndex) {
        questionTextView.setText(questions[questionIndex]);
        option1RadioButton.setText(options[questionIndex][0]);
        option2RadioButton.setText(options[questionIndex][1]);
        option3RadioButton.setText(options[questionIndex][2]);
        option4RadioButton.setText(options[questionIndex][3]);

        // Clear the selection of radio buttons when displaying a new question
        option1RadioButton.setChecked(false);
        option2RadioButton.setChecked(false);
        option3RadioButton.setChecked(false);
        option4RadioButton.setChecked(false);
    }

    private void checkAnswer(int questionIndex) {
        if (option1RadioButton.isChecked() && correctAnswersIndex[questionIndex] == 0) {
            correctAnswers++;
        } else if (option2RadioButton.isChecked() && correctAnswersIndex[questionIndex] == 1) {
            correctAnswers++;
        } else if (option3RadioButton.isChecked() && correctAnswersIndex[questionIndex] == 2) {
            correctAnswers++;
        } else if (option4RadioButton.isChecked() && correctAnswersIndex[questionIndex] == 3) {
            correctAnswers++;
        }
    }

    private void showFinalScore() {

        //String quizCompleted = getString(R.string.lanfe_quizComplete);
        //String totalQuestions = getString(R.string.lanfei_totalQ);
        String message = "Quiz comppleted correct answers " + correctAnswers + " out of questions " + questions.length;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Open FinalScoreActivity and pass the final score as an extra
        Intent intent = new Intent(this, FinalScoreActivity.class);
        intent.putExtra("finalScore", correctAnswers);
        startActivity(intent);

    }
}
