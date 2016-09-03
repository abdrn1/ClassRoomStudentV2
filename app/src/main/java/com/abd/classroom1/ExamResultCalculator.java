package com.abd.classroom1;


import android.util.Log;

import java.util.List;

/**
 * Created by Abd on 3/16/2016.
 */
public class ExamResultCalculator {


    public static ExamResult CalculateExamResult(List<QuestionItem> questionsList) {
        ExamResult examResult = new ExamResult();
        double mcqResult = 0;
        double truefalseResult = 0;
        double fillResult = 0;
        double finalResult = 0;
        for (QuestionItem qi : questionsList) {
            switch (qi.getQuestionType()) {
                case QuestionItem.QMCQ:
                    mcqResult = mcqResult + calculateMCQ(qi);
                    examResult.exaMmark = examResult.exaMmark + qi.getQuestionWeight();
                    break;
                case QuestionItem.QTRUEORFALSE:
                    Log.d("result MCQ", "Found TRUE OR FALSE");
                    truefalseResult = truefalseResult + calculateTrueFalse(qi);
                    examResult.exaMmark = examResult.exaMmark + qi.getQuestionWeight();
                    break;
                case QuestionItem.MFILL:
                    fillResult = fillResult + calculateFill(qi);
                    examResult.exaMmark = examResult.exaMmark + qi.getQuestionWeight();
                    break;
            }
        }
        finalResult = mcqResult + truefalseResult + fillResult;
        examResult.setStudentMark(finalResult);
        return examResult;
    }

    private static int calculateMCQ(QuestionItem mcqItem) {
        int result = 0;
        List<QuestionItem.ChoiceItem> choiceList = mcqItem.getChoices();
        for (QuestionItem.ChoiceItem chi : choiceList) {
            if (chi.isChecked()) {
                if (chi.isStudentChecked()) {
                    result = mcqItem.getQuestionWeight();

                }

            }
        }
        Log.d("result MCQ", Integer.toString(result));
        return result;
    }

    private static int calculateTrueFalse(QuestionItem tfItem) {
        int result =0;

        if (tfItem.getStudentQuestionAnswer().equals(tfItem.getQuestionAnswer())) {
            result =tfItem.getQuestionWeight();
            Log.d("result TrueORFalse", Integer.toString(result));
            return result;

        }
        return result;
    }

    private static int calculateFill(QuestionItem fillItem) {
        int result =0;
        if (fillItem.getStudentQuestionAnswer().trim().equals(fillItem.getQuestionAnswer().trim())) {
            result =fillItem.getQuestionWeight();
            Log.d("result Fill", Integer.toString(result));
            return result;
        }
        return result;
    }

}
