package com.abd.classroom1;

/**
 * Created by Abd on 3/18/2016.
 */
public class ExamFileMessage extends FileChunkMessageV2 {
    String examTitle;
    int examID;

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }
}
