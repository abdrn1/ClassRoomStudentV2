package com.abd.classroom1;

/**
 * Created by PROBOOK on 3/29/2016.
 */
public class MonitorRequestMessage {
    private String senderID;
    private String senderName;
    private String receiverID;

    public MonitorRequestMessage(){}

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

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
}
