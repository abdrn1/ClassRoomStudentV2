package com.abd.classroom1;

/**
 * Created by PROBOOK on 3/30/2016.
 */
public class ScreenshotMessage {
    private String screenshot;
    private String senderID;
    private String receiverID;

    public ScreenshotMessage(){}

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

}
