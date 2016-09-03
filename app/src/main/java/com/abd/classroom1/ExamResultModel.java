package com.abd.classroom1;

/**
 * Created by abd on 26/03/16.
 */
public class ExamResultModel {
    private String clientName;
    private String ClientID;
    private Integer clientImage;
    private double examMark = 0;
    private double studentMark = 0;
    String examFileName;

    public ExamResultModel() {

    }

    public ExamResultModel(String clientID, String clientName) {
        ClientID = clientID;
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public Integer getClientImage() {
        return clientImage;
    }

    public void setClientImage(Integer clientImage) {
        this.clientImage = clientImage;
    }

    public double getExamMark() {
        return examMark;
    }

    public void setExamMark(double examMark) {
        this.examMark = examMark;
    }

    public double getStudentMark() {
        return studentMark;
    }

    public void setStudentMark(double studentMark) {
        this.studentMark = studentMark;
    }
}
