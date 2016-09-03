package com.abd.classroom1;

/**
 * Created by abd on 26/03/16.
 */
public class ExamResult {
    double exaMmark=0;
    double studentMark;

    public ExamResult() {

    }

    public ExamResult(double exaMmark, double studentMark) {
        this.exaMmark = exaMmark;
        this.studentMark = studentMark;
    }

    public double getExaMmark() {
        return exaMmark;
    }

    public void setExaMmark(double exaMmark) {
        this.exaMmark = exaMmark;
    }

    public double getStudentMark() {
        return studentMark;
    }

    public void setStudentMark(double studentMark) {
        this.studentMark = studentMark;
    }
}
