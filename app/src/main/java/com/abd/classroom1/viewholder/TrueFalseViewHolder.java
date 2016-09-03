package com.abd.classroom1.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.abd.classroom1.R;

/**
 * Created by Abd on 3/15/2016.
 */
public class TrueFalseViewHolder extends RecyclerView.ViewHolder {
    public TextView trueFalseText;
    public RadioGroup trueorfalseg;


    public TrueFalseViewHolder(View itemView) {
        super(itemView);
        trueFalseText = (TextView) itemView.findViewById(R.id.truefalse_text);
        trueorfalseg = (RadioGroup) itemView.findViewById(R.id.truefalsegroub);
    }
}
