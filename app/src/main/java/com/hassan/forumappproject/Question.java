package com.hassan.forumappproject;

import java.util.ArrayList;

/**
 * Created by hmumin on 11/8/16.
 */

public class Question {

    public String question;
    public String date;
    public int upvote;
    public String answer;
    public ArrayList<String> answerList = new ArrayList<>();

    //empty constructor
    public Question(){}

    public Question(String question, String date)
    {
        this.question = question;
        this.date  = date;
    }


    //getters and setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }



    public ArrayList getAnswerList()
    {
        return this.answerList;
    }


    public void setAnswerList(ArrayList<String> answerList) {
        this.answerList = answerList;
    }

    public void addAnswerTolist(String answer)
    {
        answerList.add(answer);
    }
}
