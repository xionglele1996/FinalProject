package net.lanfei.trivia.data;

import androidx.room.Ignore;

import java.util.ArrayList;

public class Question {

    private String questionText;

    // the correct answer
    private String correctOption;

    // the options of with all correct and wrong answers
    @Ignore
    private ArrayList<String> answerOptions;

    // set to true if the answer is correct
    private boolean correct = false;

    // Constructor
    public Question(String questionText, String correctOption) {
        this.questionText = questionText;
        this.correctOption = correctOption;
    }

    // Getters and setters
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public ArrayList<String> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(ArrayList<String> answerOptions) {
        this.answerOptions = answerOptions;
    }

    /**
     * @return true if the answer is correct
     */
    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

