package net.lanfei.trivia.data;

import java.util.ArrayList;

public class Question {
    private String question;
    private String correctAnswer;
    private String [] options;

    void Question(String question, String answer, String[] options) {
        this.question = question;
        this.correctAnswer = answer;
        this.options = options;
    }

    String getQuestion() {
        return question;
    }

    String getCorrectAnswer() {
        return correctAnswer;
    }

    String [] getOptions() {
        return options;
    }
}

//only test
