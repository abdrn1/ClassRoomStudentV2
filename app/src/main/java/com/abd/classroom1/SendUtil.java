package com.abd.classroom1;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.esotericsoftware.kryonet.Client;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Abd on 3/7/2016.
 */
public class SendUtil {

    public static AddRemoveSync synAddOrRemove;


    public static void readAndSendFile(Activity activity, String path, Client client, UserLogin currentUser, String[] recivers, String fileType) throws IOException {
        //File f = new File(path) ;


        Log.d("INFO FIle Name", FilenameUtils.getName(path));
        Log.d("INFO","CONVERT TO ARRA");
        FileChunkMessageV2 fmsg = new FileChunkMessageV2();
        fmsg.setSenderID(currentUser.getUserID());
        fmsg.setSenderName(currentUser.getUserName());
        fmsg.setFileName(FilenameUtils.getName(path));
        fmsg.setFiletype(fileType);
        fmsg.setRecivers(recivers);
        FileSenderThreadV2 ftV2 = new FileSenderThreadV2(client,path,fmsg);
        ftV2.start();
        Log.d("INFO", "Start Thread");
    }

    public static void convertTextMessageToChatMessageModl(TextMeesage tm, Hashtable<String, List<ChatMessageModel>> allStudentsLists) {
        List<ChatMessageModel> currList = null;
        String[] recivers = tm.getRecivers();
        if (recivers != null) {
            for (String rec : recivers) {

                currList = allStudentsLists.get(rec);
                if (currList == null) {
                    currList = new ArrayList<ChatMessageModel>();
                    allStudentsLists.put(rec, currList);
                    Log.d("ERR", "No Chat List Found , func: sendutil.convertTextMess");
                }
                ChatMessageModel temp = new ChatMessageModel();
                temp.setSenderID(rec);
                temp.setSenderName(tm.getSenderName());
                temp.setSimpleMessage(tm.getTextMessage());
                temp.setMessageType(tm.getMessageType());
                temp.setIsSelf(true);
                // TODO: 27/03/16 image and file message
                currList.add(temp);

            }
        }
    }

    public static void convertFileChunkToChatMessageModl(Activity activity, String filePath, FileChunkMessageV2 tm, Hashtable<String, List<ChatMessageModel>> allStudentsLists) {
        Log.d("OK", "Start Cnvert File  func:ConvertFileChunk");
        String[] recivers = tm.getRecivers();
        List<ChatMessageModel> currList = null;
        if (recivers != null) {
            for (String rec : recivers) {
                currList = allStudentsLists.get(rec);
                if (currList == null) {
                    currList = new ArrayList<ChatMessageModel>();
                    allStudentsLists.put(rec, currList);
                    Log.d("ERR", "No Chat List Found , func: sendutil.convertTextMess");
                }

                Log.d("OK", "User Found = " + rec + "  func:ConvertFileChunk");

                ChatMessageModel temp = new ChatMessageModel();
                temp.setSenderID(rec);
                temp.setSenderName(tm.getSenderName());
                temp.setFilepath(filePath);
                temp.setSimpleMessage(tm.getFileName());
                temp.setIsSelf(true);

                if (checkIfFileIsImage(tm.getFileName())) {
                    Bitmap bm = ScalDownImage.decodeSampledBitmapFromResource(filePath, 80, 80);
                    temp.setImage(bm);
                    temp.setMessageType("IMG");
                } else {
                    Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.filecompleteicon);
                    temp.setImage(bm);
                    temp.setMessageType("FLE");
                    // ANY FILE
                }
                currList.add(temp);
            }
        } else {
            Log.d("OK", "ther is no reciver, func:ConvertFileChunk");
        }
    }

    public static void convertCapturedImageMessageTOChatMessageMode(CapturedImageMessage cim, String filePath, Hashtable<String, List<ChatMessageModel>> allStudentsLists) {
        String[] recivers = cim.getRecivers();
        List<ChatMessageModel> currList = null;
        if (recivers != null) {
            for (String rec : recivers) {
                currList = allStudentsLists.get(rec);
                if (currList == null) {
                    currList = new ArrayList<ChatMessageModel>();
                    allStudentsLists.put(rec, currList);
                    Log.d("ERR", "No Chat List Found , func: sendutil.convertTextMess");
                }

                ChatMessageModel temp = new ChatMessageModel();
                temp.setIsSelf(true);
                temp.setSenderID(rec);
                temp.setSenderName(cim.getSenderName());
                temp.setFilepath(filePath);
                temp.setSimpleMessage(cim.getFileName());
                Bitmap bm1 = ScalDownImage.decodeSampledBitmapFromResource(filePath, 80, 80);
                temp.setImage(bm1);
                temp.setMessageType("IMG");
            }
        }
    }

    public static boolean checkIfFileIsImage(String fileName) {

        String ext = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < (fileName.length() - 1)) {
            ext = fileName.substring(i + 1).toLowerCase();
        }
        if (ext == null)
            return false;
        else
            return !(!ext.equals("jpg") && !ext.equals("jpeg") && !ext.equals("png") && !ext.equals("gif"));
    }


    public static List<ChatMessageModel> getClientUnreadMessages(String fromID, List<ChatMessageModel> cmlLsit) {
        List<ChatMessageModel> templ = new ArrayList<>();
        for (ChatMessageModel cml : cmlLsit) {
            if (cml.getSenderID().equals(fromID)) {
                templ.add(cml);
            }
        }

        return templ;
    }

    public static String getRealPathFromURI(Uri contentURI, Activity act) {
        String result;
        Cursor cursor = act.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static boolean checkConnection(Client cl, UserLogin iam) {
        if (cl.isConnected()) {
            return true;
        } else {
            try {
                SendUtil.reConnect(cl, iam);
                return  true;
            } catch (Exception ex1) {
                return false;

            }
        }
    }



    public static void reConnect(Client cl, UserLogin iam) throws Exception {

        if ((cl != null) && (iam != null)) {
            if (!cl.isConnected()) {
                cl.reconnect();
                cl.sendTCP(iam);
                Log.d("con", "Try To Reconnect");
                Log.d("con", "Iam " + iam.getUserID());
                Log.d("con", "Iam " + iam.getUserType());

            }
        }

    }



}
