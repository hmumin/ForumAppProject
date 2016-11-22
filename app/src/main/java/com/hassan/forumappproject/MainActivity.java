package com.hassan.forumappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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


public class MainActivity extends AppCompatActivity {


    //initialize ui components
    public Button askQuestionButton;
    public ListView questionsListView;
    public EditText questionEditText;
    public String questionAsked;

    public Button signInButton;

    //Store questions to work with the database
    ArrayList<Question> questionItems = new ArrayList<Question>();
    //adapter for listView
    QuestionsAdapter adapter;

    //for FireBase
    private static final String ALL_QUESTIONS_KEY = "All_questions";
    private DatabaseReference dbReference;


    //Debug log tag
    public static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //set listview to xml
        questionsListView = (ListView) findViewById(R.id.list_main_display);
        //when listview is empty show text saying no questions
        questionsListView.setEmptyView(findViewById(R.id.emptyTV));

        //create adapter and pass array of questions to it
        adapter = new QuestionsAdapter(this,questionItems);

        //set the listView adapter
        questionsListView.setAdapter(adapter);

        //set Question edit text
        questionEditText = (EditText) findViewById(R.id.question_editText);
        //get question typed from editText
        questionAsked = questionEditText.getText().toString();


        //set button to xml
        askQuestionButton = (Button) findViewById(R.id.button_new_topic);
        //when add question button clicked
        askQuestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveQuestion();
            }
        });

        //Set up Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

//        signInButton = (Button) findViewById(R.id.sign_in_user_Button);
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent login_intent =
//                        new Intent(getApplicationContext(), login.class);
//                startActivity(login_intent);
//            }
//        });

        //this is a progress bar/spinning wheel delay dialog box.
        //when user types an answer and then clicks add answer we need to delay
        //the listview for a bit so the answer can be saved into firebase before we fetch it
        //otherwise we only fetch previous answers and not the new one that was just added
        final ProgressDialog progDailog = ProgressDialog.show(this, "Getting all questions",
                "please wait...", true);
        new Thread() {
            public void run() {
                try {
                    // sleep/delay for 4 seconds.
                    sleep(2000);
                } catch (Exception e) {
                }
                progDailog.dismiss();
                //method fetching questions from Firebase so listview can be populated with questions
                fetchQuestion();
            }

        }.start();



        //when quesiton in the listview is clicked
        questionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId)
            {
                //defining and finding textview
                TextView dateTextView = (TextView) view.findViewById(R.id.dateTV);
                TextView questionTextView = (TextView) view.findViewById(R.id.questionTV);
                TextView upvoteTextView = (TextView) view.findViewById(R.id.upvote_downvoteTV);

                //get information from the listView components
                String date = dateTextView.getText().toString();
                String question = questionTextView.getText().toString();
                String upvote = upvoteTextView.getText().toString();

                //pass listView data to the View question activity and launch it
                Intent viewQuestion_Intent = new Intent(getApplicationContext(),
                        ViewQuestionActivity.class);
                viewQuestion_Intent.putExtra("Date", date);
                viewQuestion_Intent.putExtra("Question", question);
                viewQuestion_Intent.putExtra("Upvote", upvote);
                //launch view question activity
                startActivity(viewQuestion_Intent);

            }
        });



    }

    //save question to fireBase database
    public void saveQuestion()
    {

        //get question typed into editText and get date if only user types something otherwise
        if(questionEditText.getText().toString().isEmpty()) //To check if andthing is there
        {
            Toast.makeText(this,"Type a question", Toast.LENGTH_LONG).show(); //Inform use to type in a question.
        }
        else
        {
            questionAsked = questionEditText.getText().toString(); //getting text typed in, put into variable
            String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date()); //Getting date and putting in month, day, year format

            //question object to pass to firebase
            Question quesionObj = new Question(questionAsked, date);

            DatabaseReference newQuestion = dbReference.child(ALL_QUESTIONS_KEY).push(); //adding question to the database
            //pass question object to firebase
            newQuestion.setValue(quesionObj);
            adapter.clear(); //Clearing out adapter to be ready for next question
            fetchQuestion(); //Showing new list of questions, with the new one added.

            questionEditText.getText().clear(); //Clearing questionEditText for next question to be written in
        }

    }

    //Fetch data from Firebase
    public void fetchQuestion()
    {
        //Fetch data from FireBase to add to listView
        Query getAllQuestions = dbReference.child(ALL_QUESTIONS_KEY);
        getAllQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //arrayList of all questions
                ArrayList<Question> questions = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Log.d(TAG, "ds: " + ds);
                    Question question = ds.getValue(Question.class);
                    //get the questions key from firebase and set it to the question object
                    question.setKey(ds.getKey());
                    Log.d(TAG, "question and key: " + question.getQuestion() + " " + question.getKey());
                    questions.add(question);
                }
                //iterate over questions objects in our arraylist that we received from firebase
                // and add them to listview
                for(int i = 0; i < questions.size(); i++)
                {
                    adapter.add(questions.get(i));
                    adapter.notifyDataSetChanged();

                }



            }
            //Error protection, hopefully to prevent from crashing
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

