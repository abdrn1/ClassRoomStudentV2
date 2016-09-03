package com.abd.classroom1;

/**
 * Created by Abd on 3/8/2016.
 */
public class TextMeesage implements Message {
    String senderID;
    String senderName;
    String[] recivers;
    String messageType;
    String textMessage;

    @Override
    public String getSenderID() {
        return senderID;
    }

    @Override
    public void setSenderID(String senderID) {
        this.senderID=senderID;
    }

    @Override
    public String getSenderName() {
        return senderName;
    }

    @Override
    public void setSenderName(String senderName) {
        this.senderName=senderName;
    }

    @Override
    public String[] getRecivers() {
        return recivers;
    }

    @Override
    public void setRecivers(String[] recivers) {
            this.recivers = recivers;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(String messageType) {
            this.messageType = messageType;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
