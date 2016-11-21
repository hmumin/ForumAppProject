package com.hassan.forumappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    public final String  TAG = "AnswerDebug";



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

        //when Add answer button is clicked
        addAnswerButton = (Button) findViewById(R.id.add_answer_button);
        addAnswerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //get answer typed into answer editText view
                typedAnswerToQuestion = answerEditTextView.getText().toString();
                //method to save answer to firebase attached to its question
                saveAnswers();


                //pass answer data to the ViewQuestionActivity and launch it
                Intent view_question_intent = new Intent(getApplicationContext(),
                        ViewQuestionActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view_question_intent.putExtra("Answer", typedAnswerToQuestion);
                view_question_intent.putExtra("Question", questionAsked);
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
            //getting the answer typed in the answer editTextview and setting it to variable
            typedAnswerToQuestion = answerEditTextView.getText().toString();
            Log.d(TAG, "TYPED ANSWER ABOVE 2: " + typedAnswerToQuestion);



            //Fetch questions objects from FireBase
            final Query getAllQuestions = dbReference.child(ALL_QUESTIONS_KEY);
            //used to fetch question answer array
            final DatabaseReference mQuestionRef = FirebaseDatabase.getInstance().getReference().child("All_questions");
            getAllQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //arrayList of all questions
                    ArrayList<Question> questions = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                       // Log.d(TAG, "ds: " + ds);
                        Question question = ds.getValue(Question.class);
                        question.setKey(ds.getKey());
                        Log.d(TAG, "Question and KEY: " + question.getQuestion() + " " + question.getKey());
                        questions.add(question);
                    }

                    //iterate over questions objects in arraylist that i got from firebase
                    for(int i = 0; i < questions.size(); i++)
                    {
                        //if the firebase question is the question on this view we can add answers to it
                        if(questions.get(i).getQuestion().equalsIgnoreCase(questionAsked))
                        {
                            //add answer to the questions answer list
                            questions.get(i).addAnswerTolist(typedAnswerToQuestion);

                            //updating questions answer array to add the answer into it
                            mQuestionRef.child(questions.get(i).getKey()).setValue(questions.get(i));

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            answerEditTextView.getText().clear();

        }


    }
}
