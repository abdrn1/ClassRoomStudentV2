package com.abd.classroom1;

/**
 * Created by abd on 30/03/16.
 */
public class RecivedFileKey {
    String  ownerID ="";
    String fielName ="";


    public RecivedFileKey(String ownerID, String fielName) {
        this.ownerID = ownerID;
        this.fielName = fielName;
    }


    @Override
    public int hashCode() {
        return Integer.decode(ownerID);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RecivedFileKey)){
            return  false;
        }
        RecivedFileKey curr= (RecivedFileKey)obj;
        if((ownerID.equals(curr.getOwnerID())) && (fielName.equals(curr.getFielName()))){
           return true;
        }

        return  false;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getFielName() {
        return fielName;
    }

    public void setFielName(String fielName) {
        this.fielName = fielName;
    }
}
