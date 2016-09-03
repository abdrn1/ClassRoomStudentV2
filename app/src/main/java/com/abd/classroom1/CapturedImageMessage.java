package com.abd.classroom1;

/**
 * Created by Abd on 8/10/2016.
 */
public class CapturedImageMessage {
    String senderID;
    String senderName;
    String[] recivers;
    String picture; // this the image.
    String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String[] getRecivers() {
        return recivers;
    }

    public void setRecivers(String[] recivers) {
        this.recivers = recivers;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
