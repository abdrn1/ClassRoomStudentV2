package com.abd.classroom1;

/**
 * Created by abd on 25/03/16.
 */
public class ExamResultMessage {

    private String senderID;
    private String senderName;
    private String[] receivers;
    private double examresult;
    private double studentresult;
    String examFileName;

    public ExamResultMessage() {
    }

    public ExamResultMessage(double examresult, double studentresult) {
        this.examresult = examresult;
        this.studentresult = studentresult;
    }

    public double getExamresult() {
        return examresult;
    }

    public void setExamresult(double examresult) {
        this.examresult = examresult;
    }

    public double getStudentresult() {
        return studentresult;
    }

    public void setStudentresult(double studentresult) {
        this.studentresult = studentresult;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }
}
