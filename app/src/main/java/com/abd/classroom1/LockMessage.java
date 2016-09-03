package com.abd.classroom1;

/**
 * Created by PROBOOK on 3/23/2016.
 */
public class LockMessage {
    private String senderID;
    private String senderName;
    private String[] receivers;
    private boolean lock = true;

    public LockMessage(){}

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

    public void setReceivers(String[] recivers) {
        this.receivers = recivers;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
