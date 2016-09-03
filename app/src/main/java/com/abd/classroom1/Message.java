package com.abd.classroom1;

/**
 * Created by Abd on 3/7/2016.
 */
public interface Message {
    public String getSenderID();
    public void setSenderID(String senderID);
    public String getSenderName();
    public void setSenderName(String senderName);
    public String[] getRecivers();
    public void setRecivers(String[] recivers);
    public String getMessageType();
    public void setMessageType(String messageType);


}
