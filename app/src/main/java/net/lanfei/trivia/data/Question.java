package net.lanfei.trivia.data;

import androidx.room.Ignore;

import java.util.ArrayList;

public class Question {

    private String questionText;

    private String correctOption;

    @Ignore
    private ArrayList<String> answerOptions;

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

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

