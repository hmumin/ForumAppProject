package com.hassan.forumappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnswerQuestionActivity extends AppCompatActivity {


    public TextView questionTextView;
    public EditText answerEditTextView;
    public Button addAnswerButton;
    String questionAsked;
    String typedAnswerToQuestion;


    //for FireBase
    private static final String ALL_QUESTIONS_KEY = "All_questions";
    public DatabaseReference dbReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);


        questionTextView = (TextView) findViewById(R.id.theQuestionTV);
        //get question that was passed on by the question view activity
        Intent intent = getIntent();
        questionAsked = intent.getStringExtra("Question");
        //display the quesiton to our textview
        questionTextView.setText(questionAsked);

        //initialize editext view for answer
        answerEditTextView = (EditText) findViewById(R.id.answer_editText);

        //Set up Firebase to save answers
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

        //Add answer button
        addAnswerButton = (Button) findViewById(R.id.add_answer_button);
        addAnswerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                //method to save answer to firebase attached to its question
                saveAnswers();


                typedAnswerToQuestion = answerEditTextView.getText().toString();
                //pass answer data to the View question activity and launch it
                Intent view_question_intent = new Intent(getApplicationContext(),
                        ViewQuestionActivity.class);
                view_question_intent.putExtra("Answer", typedAnswerToQuestion);
                //launch view question activity
                startActivity(view_question_intent);


            }
        });


    }


    //save answers to fireBase database
    public void saveAnswers()
    {

        //if user tries to click add answer without typing any answer
        if(answerEditTextView.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Type an answer", Toast.LENGTH_LONG).show();
        }
        else
        {
            //getting the answer typed in the editTextview and setting it to variable
            typedAnswerToQuestion = answerEditTextView.getText().toString();


            //need to get the question and attach the answer to it
            //then save it to firebase


            answerEditTextView.getText().clear();
        }


    }
}
