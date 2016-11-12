package com.hassan.forumappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewQuestionActivity extends AppCompatActivity {


    public Button answerButton;
    public TextView questionTextView;
    String quesionAsked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        questionTextView = (TextView) findViewById(R.id.questionView_TextView);
        Intent intent = getIntent();
        quesionAsked = intent.getStringExtra("Question");
        questionTextView.setText(quesionAsked);

        answerButton = (Button) findViewById(R.id.answer_Button);
        answerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    //create and open answers activity to make an answer
            }
        });

    }
}
