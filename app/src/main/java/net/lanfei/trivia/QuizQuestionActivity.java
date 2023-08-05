/**
 * This class represents the activity for taking a quiz with trivia questions.
 * It allows the user to answer questions, calculate the total score, and save the score in a local database.
 * The questions are fetched from an external API and displayed in a RecyclerView.
 */
package net.lanfei.trivia;
// Import statements

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.R;
import net.databinding.LanfeiActivityQuizQuestionBinding;
import net.lanfei.trivia.data.Question;
import net.lanfei.trivia.data.TriviaDatabase;
import net.lanfei.trivia.data.TriviaScore;
import net.lanfei.trivia.data.TriviaScoreDao;

/**
 * QuizQuestionActivity class extending AppCompatActivity.
 */
public class QuizQuestionActivity extends AppCompatActivity {

    // Member variables and constants (omitted for brevity)
    private RecyclerView.Adapter<QuizQuestionAdapter.QuestionViewHolder> myAdapter;
    private LanfeiActivityQuizQuestionBinding binding;
    ArrayList<Question> questions = new ArrayList<>();

    Context context;
    RequestQueue queue = null;

    int finalScore = 0;

    private Executor thread;

    boolean calcualted = false;

    /**
     * Initializes the quiz question activity and sets up UI components and event listeners.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Method body (omitted for brevity)
        super.onCreate(savedInstanceState);

        binding = LanfeiActivityQuizQuestionBinding.inflate(getLayoutInflater());
        //This part goes at the top of the onCreate function:
        queue = Volley.newRequestQueue(this); //like a constructor
        setContentView(binding.getRoot());

        context = this;

        // get the values from the parent page
        String topic = getIntent().getStringExtra("Topic");
        String amount = getIntent().getStringExtra("Amount");
        String username = getIntent().getStringExtra("LoginName");

        binding.categoryTextView.setText(" for " + topic + " Questions");

        TriviaDatabase db = Room.databaseBuilder(getApplicationContext(),
                TriviaDatabase.class, "TriviaScore").build();

        TriviaScoreDao triviaDao = db.triviaScoreDAO();

        // Set up the RecyclerView and adapter
        myAdapter = new QuizQuestionAdapter(this, questions);

        binding.recycleView.setAdapter(myAdapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        String categoryNum = getCategoryNumber(topic);
        // Load the questions from the API
        loadQuestions(amount, categoryNum);

        binding.calculateScoreButton.setOnClickListener(clk -> {
            calcualted = true;
            finalScore = 0;
            int correctAnswers = 0;

            // count the correct answers
            for (Question question : questions) {
                if (question.isCorrect()) {
                    correctAnswers++;
                }
            }

            // round the score to integer
            finalScore = (int) Math.ceil(100.0 * correctAnswers / questions.size());

            String finalAnswer = correctAnswers + "/" + questions.size();

            FinalScoreDetailsFragment scoreFragment = new FinalScoreDetailsFragment(username, topic, amount, finalAnswer, finalScore);
            FragmentManager fMgr = ((QuizQuestionActivity) context).getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.add(R.id.fragmentLocation, scoreFragment);
            tx.replace(R.id.fragmentLocation, scoreFragment);
            tx.addToBackStack(null);
            tx.commit();

        /*    AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("You've achieved:  " + correctAnswers + "/" +
                            questions.size() + " correct answers")
                    .setMessage("Do you want to save your score: " + finalScore + "?\n").
                    setPositiveButton("Yes", (dialog, cl) -> {

                        TriviaScore score = new TriviaScore(username, finalScore);
                        thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            triviaDao.insert(score);
                        });

                        finish();
                    }).
                    setNegativeButton("No", (dialog, cl) -> {

                    }).
                    create().show();

         */
        });

        binding.saveScoreButton.setOnClickListener(clk -> {
            if (calcualted) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save your score")
                        .setMessage("Do you want to save your score: " + finalScore + "?\n").
                        setPositiveButton("Yes", (dialog, cl) -> {

                            TriviaScore score = new TriviaScore(username, finalScore);
                            thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                triviaDao.insert(score);
                            });

                            finish();
                        }).
                        setNegativeButton("No", (dialog, cl) -> {

                        }).
                        create().show();

              /*  FinalScoreDetailsFragment scoreFragment = new FinalScoreDetailsFragment(username, topic, amount, finalScore);
                FragmentManager fMgr = ((QuizQuestionActivity) context).getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                tx.add(R.id.fragmentLocation, scoreFragment);
                tx.replace(R.id.fragmentLocation, scoreFragment);
                tx.addToBackStack(null);
                tx.commit();
                */
            } else {
                Toast.makeText(this,
                        "Please calculate score before save final score",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Loads trivia questions from the API based on the selected category and amount.
     *
     * @param amount   The number of questions to fetch.
     * @param category The selected trivia category.
     */
    private void loadQuestions(String amount, String category) {
        // Method body (omitted for brevity)
        String url = "https://opentdb.com/api.php?amount=" + amount + "&category=" +
                category + "&type=multiple";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Get the array of questions from the response
                        JSONArray questionsArray = response.getJSONArray("results");

                        // Clear the existing questions list
                        questions.clear();

                        // Iterate over the array and create a new Question object for each item
                        for (int i = 0; i < questionsArray.length(); i++) {
                            JSONObject questionObject = questionsArray.getJSONObject(i);

                            // Get the question details
                            String questionText = questionObject.getString("question");
                            String correctAnswer = questionObject.getString("correct_answer");
                            JSONArray incorrectAnswers = questionObject.getJSONArray("incorrect_answers");

                            // Create a new Question object and add it to the list
                            Question question = new Question(questionText, correctAnswer);

                            //make answers randomly and save them in a arraylist
                            ArrayList<String> options = new ArrayList<String>();
                            options.add(incorrectAnswers.getString(0));
                            options.add(incorrectAnswers.getString(1));
                            options.add(incorrectAnswers.getString(2));
                            options.add(correctAnswer);

                            //make the options order random
                            Collections.shuffle(options);
                            question.setAnswerOptions(options);

                            questions.add(question);
                        }

                        // Notify the adapter that the data has changed
                        myAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        // Add the request to the queue
        queue.add(jsonObjectRequest);
    }

    void resetCalculateFlag() {
        calcualted = false;
    }
    /**
     * RecyclerView Adapter for holding the individual trivia question views.
     */
    class QuizQuestionAdapter extends RecyclerView.Adapter<QuizQuestionAdapter.QuestionViewHolder> {
        // Adapter class definition (omitted for brevity)
        private List<Question> questionList;

        String selectedAnswer;
        private Context context;

        /**
         * Initializes the RecyclerView adapter with the provided context, question list, and ViewModel.
         *
         * @param context      The context of the activity.
         * @param questionList The list of trivia questions to display.
         *                     param quizModel    The ViewModel for storing user responses and scores.
         */
        public QuizQuestionAdapter(Context context, List<Question> questionList) {
            this.context = context;
            this.questionList = questionList;
        }

        /**
         * Inflates the view holder for the RecyclerView.
         *
         * @param parent   The parent view group.
         * @param viewType The view type.
         * @return The inflated QuestionViewHolder.
         */
        @NonNull
        @Override
        public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lanfei_question, parent, false);
            return new QuestionViewHolder(view);
        }

        /**
         * Binds the data to the view holder and handles user responses.
         *
         * @param holder   The QuestionViewHolder.
         * @param position The position of the item in the RecyclerView.
         */
        @Override
        public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
            Question question = questionList.get(position);
            holder.questionTextView.setText(question.getQuestionText());

            //get answers
            List<String> answers = question.getAnswerOptions();

            holder.option1RadioButton.setText(answers.get(0));
            holder.option2RadioButton.setText(answers.get(1));
            holder.option3RadioButton.setText(answers.get(2));
            holder.option4RadioButton.setText(answers.get(3));


            holder.optionsRadioGroup.setTag(position);
            holder.optionsRadioGroup.clearCheck();

            // reset the correct answer to false
            question.setCorrect(false);

            holder.option1RadioButton.setOnClickListener(click -> {
                selectedAnswer = holder.option1RadioButton.getText().toString();
                if (selectedAnswer.equalsIgnoreCase(question.getCorrectOption())) {
                    question.setCorrect(true);
                } else {
                    question.setCorrect(false);
                }
                calcualted = false;
            });

            holder.option2RadioButton.setOnClickListener(click -> {
                selectedAnswer = holder.option2RadioButton.getText().toString();
                if (selectedAnswer.equalsIgnoreCase(question.getCorrectOption())) {
                    question.setCorrect(true);
                } else {
                    question.setCorrect(false);
                }
                calcualted = false;
            });

            holder.option3RadioButton.setOnClickListener(click -> {
                selectedAnswer = holder.option3RadioButton.getText().toString();
                if (selectedAnswer.equalsIgnoreCase(question.getCorrectOption())) {
                    question.setCorrect(true);
                } else {
                    question.setCorrect(false);
                }
                calcualted = false;
            });

            holder.option4RadioButton.setOnClickListener(click -> {
                selectedAnswer = holder.option4RadioButton.getText().toString();
                if (selectedAnswer.equalsIgnoreCase(question.getCorrectOption())) {
                    question.setCorrect(true);
                } else {
                    question.setCorrect(false);
                }
                calcualted = false;
            });
        }

        /**
         * Returns the total number of trivia questions in the adapter.
         *
         * @return The number of trivia questions in the adapter.
         */
        @Override
        public int getItemCount() {
            return questionList.size();
        }

        /**
         * ViewHolder class for holding the individual trivia question views.
         */
        public class QuestionViewHolder extends RecyclerView.ViewHolder {

            // ViewHolder class definition (omitted for brevity)
            private TextView questionTextView;
            private RadioGroup optionsRadioGroup;
            private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;

            /**
             * Initializes the ViewHolder with the provided item view.
             *
             * @param itemView The item view for the trivia question.
             */
            public QuestionViewHolder(@NonNull View itemView) {
                super(itemView);
                questionTextView = itemView.findViewById(R.id.questionTextView);
                optionsRadioGroup = itemView.findViewById(R.id.optionsRadioGroup);
                option1RadioButton = itemView.findViewById(R.id.option1RadioButton);
                option2RadioButton = itemView.findViewById(R.id.option2RadioButton);
                option3RadioButton = itemView.findViewById(R.id.option3RadioButton);
                option4RadioButton = itemView.findViewById(R.id.option4RadioButton);
            }

        }
    }

    /**
     * Returns the category number for the selected trivia category.
     *
     * @param category The selected trivia category name.
     * @return The category number as a string.
     */
    private String getCategoryNumber(String category) {
        if (category.equalsIgnoreCase("Mythology")) return "20";
        else if (category.equalsIgnoreCase("Sports")) return "21";
        else if (category.equalsIgnoreCase("Geography")) return "22";
        else if (category.equalsIgnoreCase("History")) return "23";
        else if (category.equalsIgnoreCase("Politics")) return "24";
        else if (category.equalsIgnoreCase("Art")) return "25";
        else return "22";
    }

}