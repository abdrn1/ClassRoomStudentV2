package com.abd.classroom1.service;

/**
 * Created by Abd on 5/26/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.abd.classroom1.BuildFileFromBytesV2;
import com.abd.classroom1.ChatMessageModel;
import com.abd.classroom1.ClientModel;
import com.abd.classroom1.ExamViewerFragment;
import com.abd.classroom1.FileChunkMessageV2;
import com.abd.classroom1.MainActivity;
import com.abd.classroom1.MonitorRequestMessage;
import com.abd.classroom1.OnServiceInteractionListener;
import com.abd.classroom1.QuestionItem;
import com.abd.classroom1.R;
import com.abd.classroom1.RecivedFileKey;
import com.abd.classroom1.ScalDownImage;
import com.abd.classroom1.SendUtil;
import com.abd.classroom1.SimpleTextMessage;
import com.abd.classroom1.UserLogin;
import com.abd.classroom1.XmlExamParser;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class MessageListener extends Listener {
    public static final int NOTIFICATION_ID = 1337;
    private static final String NOTIFY_KEYWORD = "snicklefritz";
    private static final int POLL_PERIOD = 60000;
    private List<ChatMessageModel> chatMessageModelList;
    private Service ownerService;
    private Hashtable<String, List<ChatMessageModel>> allStudentsLists;
    boolean sBounded = false;
    protected OnServiceInteractionListener activity;
    private Hashtable<RecivedFileKey, BuildFileFromBytesV2> recivedFilesTable;
    List<QuestionItem> tQuestionsList;
    public boolean newExam = false;
    private List<ClientModel> clientsList;
    private UserLogin iam;

    public MessageListener(Hashtable<String, List<ChatMessageModel>> allStudentsL, Service s) {
        this.allStudentsLists = allStudentsL;
        this.ownerService = s;
        recivedFilesTable = new Hashtable<>();
    }

    public UserLogin getIam() {
        return iam;
    }

    public void setIam(UserLogin iam) {
        this.iam = iam;
    }

    public List<ClientModel> getClientsList() {
        return clientsList;
    }

    public void setClientsList(List<ClientModel> clientsList) {
        this.clientsList = clientsList;
    }

    public boolean issBounded() {
        return sBounded;
    }

    public void setsBounded(boolean sBounded) {
        this.sBounded = sBounded;
    }

    public List<ChatMessageModel> getChatMessageModelList() {
        return chatMessageModelList;
    }

    public void setChatMessageModelList(List<ChatMessageModel> chatMessageModelList) {
        this.chatMessageModelList = chatMessageModelList;
    }

    public Hashtable<String, List<ChatMessageModel>> getAllStudentsLists() {
        return allStudentsLists;
    }

    public void setAllStudentsLists(Hashtable<String, List<ChatMessageModel>> allStudentsLists) {
        this.allStudentsLists = allStudentsLists;
    }

    @Override
    public void received(Connection connection, Object ob) {
        String title;
        String content;
        if (ob instanceof SimpleTextMessage) {
            SimpleTextMessage simplem = (SimpleTextMessage) ob;
            if (simplem.getMessageType().equals("TXT")) {

                SimpleTextMessage stm = (SimpleTextMessage) ob;
                dealWithSimpleTextMessage(stm);
                // Show Notification
                Log.i("newMSG", "New Simple Text Message Recived");
                title = ownerService.getResources().getString(R.string.message_from);
                title = title + " " + simplem.getSenderName();
                content = simplem.getTextMessage();
                showNotification(title, content);
            }
        } else if (ob instanceof FileChunkMessageV2) {
            if (((FileChunkMessageV2) ob).getFiletype().equals(FileChunkMessageV2.FILE)) {
                Log.d("INFO", "New File Recived");
                dealWithFileMessage(((FileChunkMessageV2) ob));
            } else if (((FileChunkMessageV2) ob).getFiletype().equals(FileChunkMessageV2.EXAM)) {
                Log.d("INFO", "New File Recived");
                dealWithExamMessage((FileChunkMessageV2) ob);

            }
        } else if (ob instanceof UserLogin) {
            if (!((UserLogin) ob).isLogin_Succesful()) {
                return;
            } else if (iam != null) {
                addNewActiveClient((UserLogin) ob);

            }
        }
    }


    public void addNewActiveClient(UserLogin ul) {
        if (!(ul.getUserID().equals(iam.getUserID()))) {

            String clientStatus[] = ownerService.getResources().getStringArray(R.array.client_status);

            int resourceID = getResourseId("u" + ul.getUserID(), "drawable", ownerService.getPackageName());
            if (resourceID == -1) {
                resourceID = R.drawable.unknown;
            }
            ClientModel t = new ClientModel(ul.getUserID(), ul.getUserName(), resourceID);
            t.setLastStatus(clientStatus[0]);
            t.setStatus(0);
            System.out.println();
            if (!(ul.getUserID().equals(iam.getUserID()))) {
                if (!(ifUserExistUpdate(ul))) {
                    clientsList.add(t);
                    allStudentsLists.put(ul.getUserID(), new ArrayList<ChatMessageModel>());
                }
            }
        }


    }

    private boolean ifUserExistUpdate(UserLogin curr) {
        for (ClientModel ul1 : clientsList) {
            if (curr.getUserID().equals(ul1.getClientID())) {
                ul1.setClientName(curr.getUserName());
                int resourceID = getResourseId("u" + curr.getUserID(), "drawable", ownerService.getPackageName());
                if (resourceID == -1) {
                    resourceID = R.drawable.unknown;
                }
                // TODO: 25/03/16 re edit this after solve profile image problem
                ul1.setClientImage(resourceID);
                return true;
            }

        }

        return false;
    }

    public int getResourseId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return ownerService.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void dealWithExamMessage(FileChunkMessageV2 efMessage) {

        BuildFileFromBytesV2 buildExamFromBytes = null;
        try {

            Log.d("INFO", "EXAM File  Recived");
            String internalSavePath = ownerService.getApplicationContext().getFilesDir().getPath();
            String tempfilename = "";
            //recive the first packet from new file
            if (efMessage.getChunkCounter() == 1L) {
                Log.d("INFO", "New EXAM File  recived");
                internalSavePath = ownerService.getApplicationContext().getFilesDir().getPath();
                Log.d("INFO", "Save Path " + internalSavePath);
                tempfilename = efMessage.getFileName();
                buildExamFromBytes = new BuildFileFromBytesV2(internalSavePath + "/");
                recivedFilesTable.put(new RecivedFileKey(efMessage.getSenderID(), efMessage.getFileName()), buildExamFromBytes);

            } else {
                Log.d("INFO", "Current File Chunk: " + Long.toString(efMessage.getChunkCounter()));
                Log.d("INFO", "Current File Chunk: " + efMessage.getSenderID());
                Log.d("INFO", "Current File Chunk: " + efMessage.getSenderName());
                buildExamFromBytes = recivedFilesTable.get(new RecivedFileKey(efMessage.getSenderID(), efMessage.getFileName()));
            }
            if (buildExamFromBytes != null) {
                if (buildExamFromBytes.constructFile(efMessage)) {
                    Log.d("INFO", "EXAM FILE COMPLETE ");
                    recivedFilesTable.remove(new RecivedFileKey(efMessage.getSenderID(), efMessage.getFileName()));

                    Log.d("INFO", "FILE NAME : " + efMessage.getFileName());
                    tQuestionsList = XmlExamParser.examParser(internalSavePath + "/" + efMessage.getFileName());
                    Log.d("EXAM INFO", "List Size = : " + Integer.toString(tQuestionsList.size()));
                    for (QuestionItem qqi : tQuestionsList) {
                        Log.d("EXAM TEXT", qqi.getQuestionText());
                        Log.d("EXAM Type", qqi.getQuestionType());
                        Log.d("Exam Answer", qqi.getQuestionAnswer());

                    }

                    String title = "Quiz from :" + efMessage.getSenderName();
                    String content = "Quize  : " + efMessage.getFileName();
                    showNotification(title, content);

                    if (sBounded) {
                        activity.showExamViewer(efMessage, tQuestionsList);
                    } else {
                        newExam = true;
                    }

                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }


    public void dealWithFileMessage(FileChunkMessageV2 fcmv2) {
        BuildFileFromBytesV2 buildfromBytesV2;
        ChatMessageModel icm;
        try {

            String savepath = Environment.getExternalStorageDirectory().getPath();
            Log.d("INFO", "File Chunk Recived");
            //recive the first packet from new file
            if (fcmv2.getChunkCounter() == 1L) {
                //  final FileChunkMessageV2 tfcmv2 = fcmv2;
                Log.d("INFO PAth=", savepath + "/Classrom");
                icm = new ChatMessageModel();
                icm.setSenderID(fcmv2.getSenderID());
                icm.setSenderName(fcmv2.getSenderName());
                icm.setFilepath(savepath + "/Classrom/" + fcmv2.getFileName());
                icm.setIsSelf(false);
                buildfromBytesV2 = new BuildFileFromBytesV2(savepath + "/Classrom/");
                buildfromBytesV2.setChatMessageModel(icm);
                // buildfromBytesV2.constructFile(fcmv2);
                recivedFilesTable.put(new RecivedFileKey(fcmv2.getSenderID(), fcmv2.getFileName()), buildfromBytesV2);

            } else {
                buildfromBytesV2 = recivedFilesTable.get(new RecivedFileKey(fcmv2.getSenderID(), fcmv2.getFileName()));
            }
            if (buildfromBytesV2 != null) {

                Log.d("INFO", "Current File Chunk: " + Long.toString(fcmv2.getChunkCounter()));
                if (buildfromBytesV2.constructFile(fcmv2)) {
                    recivedFilesTable.remove(new RecivedFileKey(fcmv2.getSenderID(), fcmv2.getFileName()));
                    icm = buildfromBytesV2.getChatMessageModel();
                    if (SendUtil.checkIfFileIsImage(fcmv2.getFileName())) {
                        // Bitmap bm = BitmapFactory.decodeFile(savepath + "/Classrom/" + fcmv2.getFileName());
                        String tempImagePath = savepath + "/Classrom/" + fcmv2.getFileName();
                        // Bitmap bm = ScalingUtilities.fitImageDecoder(tempImagePath,mDstWidth,mDstHeight);
                        Bitmap bm = ScalDownImage.decodeSampledBitmapFromResource(tempImagePath, 80, 80);
                        icm.setImage(bm);
                        icm.setMessageType("IMG");
                        icm.setSimpleMessage(fcmv2.getFileName());
                    } else {
                        Bitmap bm = BitmapFactory.decodeResource(ownerService.getResources(), R.drawable.filecompleteicon);
                        icm.setImage(bm);
                        icm.setSimpleMessage(fcmv2.getFileName());
                        icm.setMessageType("FLE");
                    }

                    allStudentsLists.get(fcmv2.getSenderID()).add(icm);
                    String title = ownerService.getResources().getString(R.string.message_from);
                    title = title + " " + fcmv2.getSenderName();
                    String content = "File : " + fcmv2.getFileName();
                    showNotification(title, content);
                    if (sBounded) {
                        activity.updateMessageViewer(fcmv2.getSenderID());
                    }
                    Log.d("INFO", "EOF, FILE REcived Completely");
                }
                /// SendUtil.sendFileChunkToRecivers(clientTable, fcmv2, tRecivers);
            }

        } catch (Exception ex) {
            recivedFilesTable.remove(new RecivedFileKey(fcmv2.getSenderID(), fcmv2.getFileName()));
            ex.printStackTrace();
        }
    }


    public void dealWithSimpleTextMessage(SimpleTextMessage simplem) {
        if (simplem.getMessageType().equals("TXT")) {
            ChatMessageModel chm = new ChatMessageModel(simplem.getSenderName(), "", "TXT", simplem.getTextMessage(), false);
            chm.setSenderID(simplem.getSenderID());
            chatMessageModelList = allStudentsLists.get(simplem.getSenderID());
            if (chatMessageModelList != null) {
                chatMessageModelList.add(chm);
            }

        } else if (simplem.getMessageType().equals("OK")) {
            ChatMessageModel chm = new ChatMessageModel(simplem.getSenderName(), "", "OK", simplem.getTextMessage(), false);
            chm.setSenderID(simplem.getSenderID());
            chatMessageModelList = allStudentsLists.get(simplem.getSenderID());
            if (chatMessageModelList != null) {
                chatMessageModelList.add(chm);
            }
        }
        if (sBounded) {
            activity.updateMessageViewer(simplem.getSenderID());
        }

    }


    public void showNotification(String title, String content) {

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        //Intent resultIntent = new Intent(ownerService, MainActivity.class);
        //PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0


        Intent resultIntent = new Intent(ownerService, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.setAction("android.intent.action.MAIN");
        resultIntent.addCategory("android.intent.category.LAUNCHER");

        int mId = 2525;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ownerService)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(soundUri)
                .setAutoCancel(true);
        // .addAction(0, "Remind", pIntent)

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ownerService);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(ownerService, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


       /* PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );*/
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) ownerService.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());

    }


}
