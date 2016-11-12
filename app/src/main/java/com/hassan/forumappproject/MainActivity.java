package com.hassan.forumappproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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

    //Store questions
    ArrayList<Question> questionItems = new ArrayList<Question>();
    //adapter for listView
    QuestionsAdapter adapter;

    //for FireBase
    private static final String ALL_QUESTIONS_KEY = "All_questions";
    private DatabaseReference dbReference;

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

        //method fetching questions from Firebase so listview can be populated with questions
        fetchQuestion();




    }

    //save question to fireBase database
    public void saveQuestion()
    {

        //get question typed into editText and get date
        questionAsked = questionEditText.getText().toString();
        String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

        //question object to pass to firebase
        Question quesionObj = new Question(questionAsked,date);

        DatabaseReference newQuestion = dbReference.child(ALL_QUESTIONS_KEY).push();
        //pass question object to firebase
        newQuestion.setValue(quesionObj);
        adapter.clear();
        fetchQuestion();

        questionEditText.getText().clear();

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
                    Question question = ds.getValue(Question.class);
                    questions.add(question);
                }
                //iterate over questions objects in firebase and add them to listview
                for(int i = 0; i < questions.size(); i++)
                {
                    adapter.add(questions.get(i));
                    adapter.notifyDataSetChanged();

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

