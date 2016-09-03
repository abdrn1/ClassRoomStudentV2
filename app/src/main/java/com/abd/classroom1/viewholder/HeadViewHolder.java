package com.abd.classroom1.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.abd.classroom1.R;

/**
 * Created by Abd on 3/15/2016.
 */
public class HeadViewHolder extends RecyclerView.ViewHolder {
    public TextView headText;



    public HeadViewHolder(View itemView) {
        super(itemView);
        headText = (TextView) itemView.findViewById(R.id.head_text);
    }
}
