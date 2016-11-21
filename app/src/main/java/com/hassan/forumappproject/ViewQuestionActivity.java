package com.hassan.forumappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ViewQuestionActivity extends AppCompatActivity {


    public Button answerButton;
    public Button upvoteButton;
    public TextView questionTextView;
    public ListView answersListView;
    String questionAsked;
    String answerToQuestion;

    //for FireBase
    private static final String ALL_QUESTIONS_KEY = "All_questions";
    private DatabaseReference dbReference;

    //Store answers to question
    ArrayList<String> answerItemsArrayList = new ArrayList<String>();

    //adapter for listview
    ArrayAdapter<String> answerListAdapter;

    private static boolean alreadyRecreated = false;


    public final String  TAG = "AnswerListDebug";
    public final String  RTAG = "Debug";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        //set up textview to show the question
        questionTextView = (TextView) findViewById(R.id.questionView_TextView);
        //get the question from main activity and set it to the textview
        Intent intent = getIntent();
        questionAsked = intent.getStringExtra("Question");
        questionTextView.setText(questionAsked);


        //set up listview to show answers to question
        answersListView = (ListView) findViewById(R.id.answers_listView);
        answersListView.setEmptyView(findViewById(R.id.emptyTV));

        //Create arrayAdapter for listview
        answerListAdapter =
                new ArrayAdapter<String>(this, R.layout.answer_items, R.id.answerTextView,answerItemsArrayList);

        //set adpter to list view
        answersListView.setAdapter(answerListAdapter);

        //setup Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();


        //get answer from AnswerQuestionActivity
        Intent answerIntent = getIntent();
        answerToQuestion = answerIntent.getStringExtra("Answer");

        //fetchQuestionAnswers();
        Log.d(RTAG, "RESUME triggreed FETCH clear ");

        //this is a progress bar/spinning wheel delay dialog box.
        //when user types an answer and then clicks add answer we need to delay
        //the listview for a bit so the answer can be saved into firebase before we fetch it
        //otherwise we only fetch previous answers and not the new one that was just added
        final ProgressDialog progDailog = ProgressDialog.show(this, "Getting all answers",
                "please wait...", true);
        new Thread() {
            public void run() {
                try {
                    // sleep/delay for 4 seconds.
                    sleep(4000);
                } catch (Exception e) {
                }
                progDailog.dismiss();
                //method for fetching answer for the question from Firebase
                //so listview can be populated with answers
                fetchQuestionAnswers();
            }

        }.start();


        //button to press when you want to answer the question
        answerButton = (Button) findViewById(R.id.answer_Button);
        answerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //pass question to answer activity
                String question = questionTextView.getText().toString();

                //pass Question data to the View activity and launch it
                Intent answer_question_intent = new Intent(getApplicationContext(),
                        AnswerQuestionActivity.class);
                answer_question_intent.putExtra("Question", question);
                //launch view question activity
                startActivity(answer_question_intent);
            }
        });

        //when helpful/upvote button is clicked
        upvoteButton = (Button) findViewById(R.id.upvote_Button);
        upvoteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //method for calculating upvote
                upvoteQueston();
                Toast.makeText(ViewQuestionActivity.this,"Marked as helpful", Toast.LENGTH_SHORT).show();

                //take you back to main activity
                Intent back_to_main_activity_Intent = new Intent(getApplicationContext(),
                        MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back_to_main_activity_Intent);
            }
        });




    }


    public void fetchQuestionAnswers()
    {

        //Fetch questions from FireBase then get list of answers for each question to add to listView
        Query getAllQuestions = dbReference.child(ALL_QUESTIONS_KEY);
        DatabaseReference getAllAnswers = FirebaseDatabase.getInstance().getReference().child(ALL_QUESTIONS_KEY);
        getAllQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //arrayList of all questions
                ArrayList<Question> questions = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Question question = ds.getValue(Question.class);
                    question.setKey(ds.getKey());
                    Log.d(TAG, "question and key: " + question.getQuestion() + " " + question.getKey());
                    questions.add(question);
                }
                //iterate over questions objects from firebase, get answers and add them to listview
                for(int i = 0; i < questions.size(); i++)
                {
                    if(questions.get(i).getQuestion().equalsIgnoreCase(questionAsked))
                    {
                        Log.d(TAG, "question from textview: " + questionAsked);
                        Log.d(TAG, "question from firebase: " + questions.get(i).getKey());
                        if(questions.get(i).getAnswerList().size() >= 0)
                        {
                            for(int j = 0; j < questions.get(i).getAnswerList().size(); j++)
                            {
                                Log.d(TAG, "Answer from question: " + questions.get(i).getAnswerList());

                                answerItemsArrayList.add(questions.get(i).getAnswerList().get(j));
                                Log.d(TAG, "Answer ArrayList " + answerItemsArrayList.get(j));

                            }

                            answerListAdapter.notifyDataSetChanged();

                        }


                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //fetch question from firebase and then get there upvote count and increment it by one
    public void upvoteQueston()
    {
        //ref to single question
        final DatabaseReference mQuestionRef = FirebaseDatabase.getInstance().getReference()
                .child("All_questions");
        //Fetch all questions objects from FireBase
        final Query getAllQuestions = dbReference.child(ALL_QUESTIONS_KEY);
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


                //iterate over all question objects in arraylist that i got from firebase
                for(int i = 0; i < questions.size(); i++)
                {
                    //if the firebase question is the question on this view we get  and update its
                    //upvote count
                    if(questions.get(i).getQuestion().equalsIgnoreCase(questionAsked))
                    {
                        //add answer to the questions answer list
                        int upvoteCount = questions.get(i).getUpvote();
                        upvoteCount++;
                        questions.get(i).setUpvote(upvoteCount);

                        //updating questions answer array to add the answer into it
                        mQuestionRef.child(questions.get(i).getKey()).setValue(questions.get(i));

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }






}
