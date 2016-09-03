package com.abd.classroom1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abd on 26/03/16.
 */
public class ExamResultListAdapter extends ArrayAdapter {
    private final List<ExamResultModel> list;
    private final Activity context;

    public ExamResultListAdapter(Activity context, List<ExamResultModel> list) {
        super(context, R.layout.exam_result_list, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView stdName;
        protected TextView examMark;
        protected TextView examFileName;

        protected TextView studentMark;
        protected ImageView img;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.exam_result_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.stdName = (TextView) view.findViewById(R.id.std_name);
            viewHolder.examFileName = (TextView) view.findViewById(R.id.exam_file_name);
            viewHolder.examMark = (TextView) view.findViewById(R.id.std_exam_mark);
            viewHolder.img = (ImageView) view.findViewById(R.id.std_image);
            viewHolder.studentMark = (TextView) view.findViewById(R.id.std_student_mark);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.stdName.setText(list.get(position).getClientName());
        holder.examFileName.setText(list.get(position).examFileName);
        holder.img.setImageResource(list.get(position).getClientImage());
        holder.studentMark.setText(Double.toString(list.get(position).getStudentMark()));
        holder.examMark.setText(Double.toString(list.get(position).getExamMark()));
        return view;
    }
}
