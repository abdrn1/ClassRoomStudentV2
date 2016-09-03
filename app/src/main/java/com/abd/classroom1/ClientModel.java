package com.abd.classroom1;

/**
 * Created by Abd on 3/5/2016.
 */
public class ClientModel {
    private String clientName;
    private String ClientID;
    private String lastStatus = "On Line";
    private boolean clientSelected = false;
    private Integer clientImage;
    private int status = 0;
    int unreadMsgCounter = 0;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public ClientModel( String clientid, String clientName,Integer clientImage ) {
        this.clientImage = clientImage;
        this.clientName = clientName;
        this.ClientID =  clientid;
    }

    public Integer getClientImage() {
        return clientImage;
    }

    public void setClientImage(Integer clientImage) {
        this.clientImage = clientImage;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isClientSelected() {
        return clientSelected;
    }

    public void setClientSelected(boolean clientSelected) {
        this.clientSelected = clientSelected;
    }
}
