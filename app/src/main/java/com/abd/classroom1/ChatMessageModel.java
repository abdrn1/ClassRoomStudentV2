package com.abd.classroom1;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by Abd on 3/7/2016.
 */
public class ChatMessageModel {

    String senderName;
    String recivers;
    String messageType;
    String simpleMessage;
    String senderID;
    Bitmap image;
    String filepath;
    boolean isSelf;
    int allok1;
    int allok2;



    public ChatMessageModel(){

    }


    public ChatMessageModel(String senderName, String recivers, String messageType, String simpleMessage, boolean isSelf) {
        this.senderName = senderName;
        this.recivers = recivers;
        this.messageType = messageType;
        this.simpleMessage = simpleMessage;
        this.isSelf = isSelf;
        allok1 = View.GONE;
        allok2 = View.GONE;
    }

    public int getAllok1() {
        return allok1;
    }

    public void setAllok1(int allok1) {
        this.allok1 = allok1;
    }

    public int getAllok2() {
        return allok2;
    }

    public void setAllok2(int allok2) {
        this.allok2 = allok2;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecivers() {
        return recivers;
    }

    public void setRecivers(String recivers) {
        this.recivers = recivers;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSimpleMessage() {
        return simpleMessage;
    }

    public void setSimpleMessage(String simpleMessage) {
        this.simpleMessage = simpleMessage;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }
}
