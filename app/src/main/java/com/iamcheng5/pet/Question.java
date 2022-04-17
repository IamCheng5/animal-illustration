package com.iamcheng5.pet;

public class Question {
    private String question;
    private String[] options;

    public Question(String question, String[] options) {
        this.question = question;
        this.options = options;
    }

    public int getOptionCount(){
        return options.length;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
