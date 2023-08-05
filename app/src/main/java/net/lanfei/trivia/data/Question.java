package net.lanfei.trivia.data;

import androidx.room.Ignore;

import java.util.ArrayList;

public class Question {

    private String questionText;

    private String option1;

    private String option2;

    private String option3;

    private String correctOption;

    @Ignore
    private ArrayList<String> options;

    private boolean correct = false;

    // Constructor
    public Question(String questionText, String option1, String option2, String option3, String correctOption) {
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.correctOption = correctOption;
    }

    // Getters and setters
    public String getQuestionText() {

        return questionText;
    }

    public void setQuestionText(String questionText) {

        this.questionText = questionText;
    }

    public String getOption1() {

        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {

        return option2;
    }

    public void setOption2(String option2) {

        this.option2 = option2;
    }

    public String getOption3() {

        return option3;
    }

    public void setOption3(String option3) {

        this.option3 = option3;
    }

    public String getCorrectOption() {

        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

