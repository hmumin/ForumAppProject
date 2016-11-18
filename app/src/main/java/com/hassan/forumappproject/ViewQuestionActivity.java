package com.hassan.forumappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewQuestionActivity extends AppCompatActivity {


    public Button answerButton;
    public TextView questionTextView;
    public ListView answersListView;

    public EditText answer_editText;
    String quesionAsked;
    String answerToQuestion;
    String questionAnswered;

    //Store answers to question
    ArrayList<String> answerItems = new ArrayList<String>();

    //TODo delete if does not work, either bring in from main activity or find list matched to question, if later use Firebase below
    //for FireBase
    private static final String ALL_QUESTIONS_KEY = "All_questions";
    private DatabaseReference dbReference;


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


        //Set up Firebase //TODO delete if does not work
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

        //set up listview to show answers to question
        answersListView = (ListView) findViewById(R.id.answers_listView);

        //Create arrayAdapter for listview
        ArrayAdapter<String> answerListAdapter =
                new ArrayAdapter<String>(this, R.layout.question_items, R.id.answerTextView,answerItems);

        //set adapter to list view
        answersListView.setAdapter(answerListAdapter);

        //TODO fetch answer from firebase database and add them to listview

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
