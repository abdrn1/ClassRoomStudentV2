package com.abd.classroom1.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.abd.classroom1.ChatMessageModel;
import com.abd.classroom1.ClientModel;
import com.abd.classroom1.OnServiceInteractionListener;
import com.abd.classroom1.QuestionItem;
import com.abd.classroom1.UserLogin;
import com.esotericsoftware.kryonet.Client;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MessageService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private Client client;
    private MessageListener l1;
    private Hashtable<String, List<ChatMessageModel>> allStudentsLists;
    private boolean sBounded = false;
    private OnServiceInteractionListener activity;




    public void setClient(Client cl){
        this.client=cl;

    }
    public List<ClientModel> getClientsList() {
        return l1.getClientsList();
    }

    public void setClientsList(List<ClientModel> clientsList) {
        this.l1.setClientsList( clientsList);
    }

    public boolean isNewExam(){
        return l1.newExam;
    }
    public void setNewExam(boolean fg){
        l1.newExam =fg;
    }

    public void setIam(UserLogin i){

        this.l1.setIam(i);
    }

    public UserLogin getIam() {
        return l1.getIam();
    }

    public Client getClient() {
        return client;
    }


    public List<QuestionItem> gettQuestionsList() {
        return l1.tQuestionsList;
    }

    public void settQuestionsList(List<QuestionItem> tQuestionsList) {

        l1.tQuestionsList = tQuestionsList;
    }

    public boolean issBounded() {
        return sBounded;

    }

    public Hashtable<String, List<ChatMessageModel>> getAllStudentsLists() {
        return allStudentsLists;
    }

    public void setAllStudentsLists(Hashtable<String, List<ChatMessageModel>> allStudentsLists) {
        this.allStudentsLists = allStudentsLists;
        l1.setAllStudentsLists(allStudentsLists);
    }

    public OnServiceInteractionListener getActivity() {
        return activity;
    }

    public void setActivity(OnServiceInteractionListener activity) {
        this.activity = activity;
        l1.activity = activity;
    }

    public void setsBounded(boolean sBounded) {
        this.sBounded = sBounded;
        l1.setsBounded(sBounded);

    }

    @Override
    public void onCreate() {
       // allStudentsLists = new Hashtable<>();
        l1 = new MessageListener(allStudentsLists,MessageService.this);
    }


    public void addClientListener(){
        if (client !=null){
            client.addListener(l1);
            Log.i("info", "Listenter Set successfuly for Service");
        }

    }

    public void removeClientListener(){
        try {
            client.removeListener(l1);
        }catch(Exception ex){

        }
    }
    public boolean isClientOnline(){
        // this method check if client is connected and already loged in
      if(!client.isConnected()){
          Log.i("DE", "New Login");
          return false;

      }else{
          if(l1.getIam() !=null) {
              Log.i("DE", "Connection with server existed");
              return true;
          }

      }
        Log.i("DE", "New Login");
        return false;
    }
    public MessageService() {
    }

    public class LocalBinder extends Binder{

        public MessageService getService(){
            return MessageService.this;
        }

    }




    @Override
    public IBinder onBind(Intent intent) {
        return  mBinder;
    }
}
