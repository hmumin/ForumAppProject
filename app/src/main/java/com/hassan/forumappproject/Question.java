package com.hassan.forumappproject;

/**
 * Created by hmumin on 11/8/16.
 */

public class Question {

    public String question;
    public String date;
    public String answer;
    public int upvote;
    public int downVote;

    //empty contructor
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

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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


}
