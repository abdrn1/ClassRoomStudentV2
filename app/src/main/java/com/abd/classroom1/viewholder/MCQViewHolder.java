package com.abd.classroom1.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.abd.classroom1.R;


/**
 * Created by Abd on 3/15/2016.
 */
public class MCQViewHolder extends RecyclerView.ViewHolder {
    public TextView mcqtext;
    public CheckBox ch1, ch2, ch3;

    public MCQViewHolder(View itemView) {
        super(itemView);
        mcqtext = (TextView) itemView.findViewById(R.id.mcq_text);
        ch1 = (CheckBox) itemView.findViewById(R.id.check1);
        ch2 = (CheckBox) itemView.findViewById(R.id.check2);
        ch3 = (CheckBox) itemView.findViewById(R.id.check3);
    }

}
