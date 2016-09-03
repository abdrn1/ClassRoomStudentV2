package com.abd.classroom1;

/**
 * Created by Abd on 3/5/2016.
 */
public class UserLogin {
     String userID;
     String userPassword;
     String userName;
     String userType;
     int UserIMage=1;


    public boolean isLogin_Succesful() {
        return login_Succesful;
    }

    public void setLogin_Succesful(boolean login_Succesful) {
        this.login_Succesful = login_Succesful;
    }


     boolean login_Succesful=false;

    public UserLogin() {

    }

    public UserLogin(String userID,String type) {
        this.userID = userID;
        this.userType=type;
    }

    public UserLogin(String userID, String userType, String userName, int userIMage) {
        this.userID = userID;
        this.userType = userType;
        this.userName = userName;
        UserIMage = userIMage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getUserIMage() {
        return UserIMage;
    }

    public void setUserIMage(int userIMage) {
        UserIMage = userIMage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
