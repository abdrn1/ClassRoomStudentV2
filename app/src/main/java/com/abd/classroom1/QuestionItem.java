package com.abd.classroom1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abd on 3/15/2016.
 */
public class QuestionItem {

    final static String QMCQ = "MCQ";
    final static String QTRUEORFALSE = "TRUEORFALSE";
    final static String MFILL = "FILL";
    final static String QHEAD = "HEAD";
    private String questionText;
    private String questionType;
    private int questionWeight = 1;
    private String questionAnswer;
    private String studentQuestionAnswer = "";
    private List<ChoiceItem> choices;

    public QuestionItem() {
        choices = new ArrayList();

    }

    public QuestionItem(String qtext, String qtype, String qans) {

        choices = new ArrayList();
        this.questionText = qtext;
        this.questionType = qtype;
        if (qans==null) {
            questionAnswer ="";
        }else {
            this.questionAnswer = qans;
        }
    }

    public int getQuestionWeight() {
        return questionWeight;
    }

    public void setQuestionWeight(int questionWeight) {
        this.questionWeight = questionWeight;
    }

    /**
     * @return the questionText
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * @param questionText the questionText to set
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * @return the questionType
     */
    public String getQuestionType() {
        return questionType;
    }

    /**
     * @param questionType the questionType to set
     */
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    /**
     * @return the questionAnswer
     */
    public String getQuestionAnswer() {
        return questionAnswer;
    }

    /**
     * @param questionAnswer the questionAnswer to set
     */
    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    /**
     * @return the choices
     */
    public List<ChoiceItem> getChoices() {
        return choices;
    }

    /**
     * @param choices the choices to set
     */
    public void setChoices(List<ChoiceItem> choices) {
        this.choices = choices;
    }

    /**
     * @return the studentQuestionAnswer
     */
    public String getStudentQuestionAnswer() {
        return studentQuestionAnswer;
    }

    /**
     * @param studentQuestionAnswer the studentQuestionAnswer to set
     */
    public void setStudentQuestionAnswer(String studentQuestionAnswer) {
        this.studentQuestionAnswer = studentQuestionAnswer;
    }

    public static class ChoiceItem {

        private String choiceText;
        private boolean checked = false;
        private boolean studentChecked = false;

        public ChoiceItem() {

        }


        public ChoiceItem(String chtxt, boolean checked) {
            this.choiceText = chtxt;
            this.checked = checked;
        }

        /**
         * @return the choiceText
         */
        public String getChoiceText() {
            return choiceText;
        }

        /**
         * @param choiceText the choiceText to set
         */
        public void setChoiceText(String choiceText) {
            this.choiceText = choiceText;
        }

        /**
         * @return the checked
         */
        public boolean isChecked() {
            return checked;
        }

        /**
         * @param checked the checked to set
         */
        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        /**
         * @return the studentChecked
         */
        public boolean isStudentChecked() {
            return studentChecked;
        }

        /**
         * @param studentChecked the studentChecked to set
         */
        public void setStudentChecked(boolean studentChecked) {
            this.studentChecked = studentChecked;
        }


    }

}
