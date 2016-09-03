package com.abd.classroom1;

/**
 * Created by abd on 24/03/16.
 */
public class StatusMessage {
    private String userID;
    private int status;

    public StatusMessage() {

    }

    public StatusMessage(String userID, int status) {
        this.userID = userID;
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
