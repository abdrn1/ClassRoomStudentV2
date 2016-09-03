package com.abd.classroom1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.abd.classroom1.viewholder.FillGapViewHolder;
import com.abd.classroom1.viewholder.HeadViewHolder;
import com.abd.classroom1.viewholder.MCQViewHolder;
import com.abd.classroom1.viewholder.TrueFalseViewHolder;

import java.util.List;

/**
 * Created by Abd on 3/15/2016.
 */
public class ExamRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater inflater;
    Context con;
    private List<QuestionItem> questionsList;


    public ExamRowAdapter(Context context, List<QuestionItem> questions) {
        this.questionsList = questions;
        this.con = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1) {
            view = inflater.inflate(R.layout.list_row_mcq, parent, false);
            return new MCQViewHolder(view);
        } else if (viewType == 2) {
            view = inflater.inflate(R.layout.list_row_true_false, parent, false);
            return new TrueFalseViewHolder(view);
        } else if (viewType == 3) {
            view = inflater.inflate(R.layout.list_row_fill_gap, parent, false);
            return new FillGapViewHolder(view, new ExamRowTextChangeWatcher());
        }else if (viewType == 4) {
            view = inflater.inflate(R.layout.list_row_head, parent, false);
            return new HeadViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /// determine view holder class according to question TYpe// bind mcq question

        // bind
        if (questionsList.get(position).getQuestionType().toUpperCase().equals(QuestionItem.QMCQ)) {
            MCQViewHolder mcqViewHolder = (MCQViewHolder) holder;
            QuestionItem cqi = questionsList.get(position);
            ((MCQViewHolder) holder).mcqtext.setText(cqi.getQuestionText());
            QuestionItem.ChoiceItem ci = cqi.getChoices().get(0);
            mcqViewHolder.ch1.setChecked(ci.isStudentChecked());
            mcqViewHolder.ch1.setText(ci.getChoiceText());
            mcqViewHolder.ch1.setTag(ci);
            mcqViewHolder.ch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QuestionItem.ChoiceItem lci;
                    CheckBox chb = (CheckBox) v;
                    lci = (QuestionItem.ChoiceItem) chb.getTag();
                    lci.setStudentChecked(chb.isChecked());
                    Log.d("INFO", "checkbox state: " + Boolean.toString(lci.isStudentChecked()));
                }
            });

            // Set up check 2
            ci = cqi.getChoices().get(1);
            mcqViewHolder.ch2.setChecked(ci.isStudentChecked());
            mcqViewHolder.ch2.setText(ci.getChoiceText());
            mcqViewHolder.ch2.setTag(ci);
            mcqViewHolder.ch2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QuestionItem.ChoiceItem lci;
                    CheckBox chb = (CheckBox) v;
                    lci = (QuestionItem.ChoiceItem) chb.getTag();
                    lci.setStudentChecked(chb.isChecked());
                    Log.d("INFO", "checkbox state: " + Boolean.toString(lci.isStudentChecked()));

                }
            });

            // setup check 3
            ci = cqi.getChoices().get(2);
            mcqViewHolder.ch3.setChecked(ci.isStudentChecked());
            mcqViewHolder.ch3.setText(ci.getChoiceText());
            mcqViewHolder.ch3.setTag(ci);
            mcqViewHolder.ch3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QuestionItem.ChoiceItem lci;
                    CheckBox chb = (CheckBox) v;
                    lci = (QuestionItem.ChoiceItem) chb.getTag();
                    lci.setStudentChecked(chb.isChecked());
                    Log.d("INFO", "checkbox state: " + Boolean.toString(lci.isStudentChecked()));

                }
            });


            // bind true or false question

        } else if (questionsList.get(position).getQuestionType().toUpperCase().equals(QuestionItem.QTRUEORFALSE)) {
            final TrueFalseViewHolder trueFalseViewHolder = (TrueFalseViewHolder) holder;
            QuestionItem cqi = questionsList.get(position);
            trueFalseViewHolder.trueFalseText.setText(cqi.getQuestionText());

            if (cqi.getStudentQuestionAnswer().equals("true")) {
                trueFalseViewHolder.trueorfalseg.check(R.id.trueradio);
            } else if (cqi.getStudentQuestionAnswer().equals("false")) {
                trueFalseViewHolder.trueorfalseg.check(R.id.falseradio);
            }
            trueFalseViewHolder.trueorfalseg.setTag(cqi);
            trueFalseViewHolder.trueorfalseg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    QuestionItem qi = (QuestionItem) trueFalseViewHolder.trueorfalseg.getTag();
                    if (checkedId == R.id.trueradio) {
                        qi.setStudentQuestionAnswer("true");
                    } else if (checkedId == R.id.falseradio) {
                        qi.setStudentQuestionAnswer("false");
                    }
                }
            });

        } else if (questionsList.get(position).getQuestionType().toUpperCase().equals(QuestionItem.MFILL)) {
            final FillGapViewHolder fillGapViewHolder = (FillGapViewHolder) holder;
            QuestionItem cqi = questionsList.get(position);
            String[] splitQuestion = cqi.getQuestionText().split("#gap#", 2);
            fillGapViewHolder.questionPart1.setText(splitQuestion[0]);
            fillGapViewHolder.questionPart2.setText(splitQuestion[1]);
            fillGapViewHolder.textWatcher.updatePosition(position);
            fillGapViewHolder.answerEditText.setText(cqi.getStudentQuestionAnswer());
            fillGapViewHolder.answerEditText.setTag(cqi);


        }else if (questionsList.get(position).getQuestionType().toUpperCase().equals(QuestionItem.QHEAD)) {
            final HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            QuestionItem cqi = questionsList.get(position);
            headViewHolder.headText.setText(cqi.getQuestionText());
        }
        // End of bind true or false question

        // start of bind fiil gap question


    }


    public void setQuestionsList(List<QuestionItem> ql) {
        this.questionsList = ql;
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        QuestionItem qi = questionsList.get(position);
        if (qi.getQuestionType().toUpperCase().equals(QuestionItem.QMCQ)) {
            return 1;

        } else if (qi.getQuestionType().toUpperCase().equals(QuestionItem.QTRUEORFALSE)) {

            return 2;
        } else if (qi.getQuestionType().toUpperCase().equals(QuestionItem.MFILL)) {
            return 3;
        }  else if (qi.getQuestionType().toUpperCase().equals(QuestionItem.QHEAD)) {
            return 4;
        }
        else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ExamRowTextChangeWatcher implements TextWatcher {
        //private EditText currentEditText;
        private QuestionItem tqi;
        private int position;

        public ExamRowTextChangeWatcher() {
            //  this.currentEditText=et;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tqi = questionsList.get(position);
            tqi.setStudentQuestionAnswer(s.toString());

        }

        public void updatePosition(int position) {
            this.position = position;
        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
