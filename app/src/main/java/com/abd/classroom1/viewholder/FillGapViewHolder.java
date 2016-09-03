package com.abd.classroom1.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.abd.classroom1.ExamRowAdapter.ExamRowTextChangeWatcher;
import com.abd.classroom1.QuestionItem;
import com.abd.classroom1.R;

import java.util.List;


/**
 * ;
 * Created by Abd on 3/16/2016.
 */
public class FillGapViewHolder extends RecyclerView.ViewHolder {
    public TextView questionPart1;
    public TextView questionPart2;
    public EditText answerEditText;
    public ExamRowTextChangeWatcher textWatcher;
    List<QuestionItem> list;
    int position;

    public FillGapViewHolder(View itemView, ExamRowTextChangeWatcher tw) {
        super(itemView);
        questionPart1 = (TextView) itemView.findViewById(R.id.textpart1);
        questionPart2 = (TextView) itemView.findViewById(R.id.textpart2);
        answerEditText = (EditText) itemView.findViewById(R.id.et_answer);
        this.textWatcher = tw;
        answerEditText.addTextChangedListener(tw);


    }
}
