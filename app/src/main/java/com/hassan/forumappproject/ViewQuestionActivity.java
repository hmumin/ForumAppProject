package com.hassan.forumappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewQuestionActivity extends AppCompatActivity {


    public Button answerButton;
    public TextView questionTextView;
    public ListView answersListView;
    String quesionAsked;
    String answerToQuestion;

    //Store answers to question
    ArrayList<String> answerItems = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        //set up textview to show the question
        questionTextView = (TextView) findViewById(R.id.questionView_TextView);
        //get the question from main activity and set it to the textview
        Intent intent = getIntent();
        quesionAsked = intent.getStringExtra("Question");
        questionTextView.setText(quesionAsked);


        //set up listview to show answers to question
        answersListView = (ListView) findViewById(R.id.answers_listView);

        //Create arrayAdapter for listview
        ArrayAdapter<String> answerListAdapter =
                new ArrayAdapter<String>(this, R.layout.question_items, R.id.answerTextView,answerItems);

        //set adpter to list view
        answersListView.setAdapter(answerListAdapter);

        //fetch answer from firebase database and add them to listview

        //get answer from answer activity
        Intent answerIntent = getIntent();
        answerToQuestion = intent.getStringExtra("Answer");

        //button to press when you want to answer the question
        answerButton = (Button) findViewById(R.id.answer_Button);
        answerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //pass question to answer activity
                String question = questionTextView.getText().toString();

                //pass listView data to the View activity and launch it
                Intent answer_question_intent = new Intent(getApplicationContext(),
                        AnswerQuestionActivity.class);
                answer_question_intent.putExtra("Question", question);
                //launch view question activity
                startActivity(answer_question_intent);

            }
        });

    }
}
