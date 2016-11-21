package com.hassan.forumappproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmumin on 11/8/16.
 */

public class Question {

    public String question;
    public String date;
    public int upvote;
    public final ArrayList<String> answerList = new ArrayList<>();
    public String key;

    //empty constructor
    public Question(){}

    //TODO, add listview for Answers?, and int for upvote
    public Question(String question, String date, ArrayList<String> answerList) // int upvote)
    {
        this.question = question;
        this.date  = date;
        this.answerList = answerList;

        //TODO add in which way?
        //this.upvote = upvote;
        //this.answerList = answerList;
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

    public ArrayList<String> getAnswerList() {
        return answerList;
    }


    public void addAnswerTolist(String answer)
    {
       getAnswerList().add(answer);
    }


    //to get each questions key from firebase
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
