package com.hassan.forumappproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hmumin on 11/11/16.
 */

public class QuestionsAdapter extends ArrayAdapter<Question> {

    public QuestionsAdapter(Context context, ArrayList<Question> questions)
    {
        super(context, 0 , questions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //get data item for this positon
        Question question = getItem(position);
        //check if an existing view is being reused, otherwise inflate the view
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.question_items, parent
                                                                    , false);
        }

        //Lookup view from data population
        TextView dateTv = (TextView) convertView.findViewById(R.id.dateTV);
        TextView upVoteTv = (TextView) convertView.findViewById(R.id.upvote_downvoteTV);
        TextView questionTv = (TextView) convertView.findViewById(R.id.questionTV);

        //populate data into template view using data object
        dateTv.setText(question.getDate());
        questionTv.setText(question.getQuestion());
        //upVoteTv.setText(question.getUpvote());

        //return completed view to screen
        return convertView;
    }

}
