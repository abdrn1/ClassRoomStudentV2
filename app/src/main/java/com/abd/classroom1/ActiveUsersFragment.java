package com.abd.classroom1;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.esotericsoftware.kryonet.Client;

import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import Decoder.BASE64Encoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActiveUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActiveUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActiveUsersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int FILE_SELECT_CODE = 2;
    private static int EXAM_SELECT_CODE = 3;
    private static int MONITOR_CODE = 4;
    private static int REQUEST_TAKE_PHOTO = 5;
    boolean isLock = true;
    private ListView listview;
    private List<ClientModel> l1;
    private List<ChatMessageModel> chatMessageModelList;
    private ClientListAdapter clientListAdapter;
    private Client client;
    private UserLogin iam;
    private Hashtable<String, List<ChatMessageModel>> allStudentsLists;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mCurrentPhotoPath;
    private OnFragmentInteractionListener mListener;
    private String capturedImagePath="";

    public ActiveUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActiveUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActiveUsersFragment newInstance(String param1, String param2) {
        ActiveUsersFragment fragment = new ActiveUsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Hashtable<String, List<ChatMessageModel>> getAllStudentsLists() {
        return allStudentsLists;
    }

    public void setAllStudentsLists(Hashtable<String, List<ChatMessageModel>> allStudentsLists) {
        this.allStudentsLists = allStudentsLists;
    }

    public void setClient(Client cl) {
        this.client = cl;
    }

    public void setUserlogin(UserLogin ul) {
        this.iam = ul;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public List<ChatMessageModel> getChatMessageModelList() {
        return chatMessageModelList;
    }

    public void setChatMessageModelList(List<ChatMessageModel> chatMessageModelList) {
        this.chatMessageModelList = chatMessageModelList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listview = (ListView) getActivity().findViewById(R.id.clients_listview);
        // l1 = new ArrayList<>();
        clientListAdapter = new ClientListAdapter(getActivity(), l1);
        listview.setAdapter(clientListAdapter);

        ImageButton sendfile = (ImageButton) getActivity().findViewById(R.id.btnsendfile);
        ImageButton btnsend = (ImageButton) getActivity().findViewById(R.id.btnSend);
        ImageButton btnStartExam = (ImageButton) getActivity().findViewById(R.id.btn_start_exam);
        ImageButton locksend = (ImageButton) getActivity().findViewById(R.id.btnLock);
        ImageButton unlocksend = (ImageButton) getActivity().findViewById(R.id.btnunLock);
        ImageButton monitorBtn = (ImageButton) getActivity().findViewById(R.id.btnMonitor);
        ImageButton captureImage = (ImageButton) getActivity().findViewById(R.id.btn_capturepic);

        final EditText inputMsg = (EditText) getActivity().findViewById(R.id.inputMsg);
        // Give Button Animation effect On press Button
        GeneralUtil.buttonEffect(sendfile);
        GeneralUtil.buttonEffect(btnsend);
        GeneralUtil.buttonEffect(btnStartExam);
        GeneralUtil.buttonEffect(locksend);
        GeneralUtil.buttonEffect(unlocksend);
        GeneralUtil.buttonEffect(captureImage);

        // end
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnFragmentInteractionListener aa = (OnFragmentInteractionListener)getActivity();
                aa.ShowMessagesViewer(l1.get(position));
                l1.get(position).unreadMsgCounter =0;
                Log.d("Hello","Item Clicked");
                Toast.makeText(getActivity().getApplicationContext(), "hello : " + l1.get(position).getClientName(), Toast.LENGTH_SHORT).show();
            }
        });

        sendfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SendUtil.checkConnection(client,iam)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/*");
                    startActivityForResult(intent, FILE_SELECT_CODE);
                    Toast.makeText(getActivity(),
                            "INfo Message", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT);
                }
            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SendUtil.checkConnection(client,iam)) {
                   // takePicture();
                    captureAndSavePicture();
                }else{
                    Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT);
                }

            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SendUtil.checkConnection(client,iam)) {
                    String tempMsg = inputMsg.getText().toString();
                    //l1.add(new ClientModel("5", "ZAKI", R.drawable.a3));
                    //clientListAdapter.notifyDataSetChanged();
                    if (!(tempMsg.equals(""))) {
                        sendTextMessage(tempMsg);
                        inputMsg.setText("");
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "your disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        locksend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SendUtil.checkConnection(client,iam)) {
                    Toast.makeText(getActivity().getApplicationContext(), "lock sent", Toast.LENGTH_LONG).show();
                    sendLockMessage(true);
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "your disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        unlocksend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SendUtil.checkConnection(client,iam)) {
                    Toast.makeText(getActivity().getApplicationContext(), "lock sent", Toast.LENGTH_LONG).show();
                    sendLockMessage(false);
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "your disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Start New Exam
        btnStartExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SendUtil.checkConnection(client, iam)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/xml");
                    startActivityForResult(intent, EXAM_SELECT_CODE);
                    Toast.makeText(getActivity(),
                            "Select XML files Only", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "your disconnected", Toast.LENGTH_SHORT).show();
                }
            }

        });

        monitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SendUtil.checkConnection(client, iam)) {
                    mListener.showMonitor(getSelectedRecivers());
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "your disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setActiveUsersList(List tlist) {
        this.l1 = tlist;
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ///  here get notified that file open completed

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK
                && null != data) {
            final Uri uri = data.getData();
            String path = getRealPathFromURI(uri);
            Log.d("INFO", path);
            //  path = uri.getPath();
            Log.d("INFO", path);
            Toast.makeText(getActivity(),
                    "File Loadded Copletely", Toast.LENGTH_SHORT).show();

            Log.d("INFO", iam.getUserName());
            Log.d("INFO", iam.getUserType());

            FileChunkMessageV2 fblock = new FileChunkMessageV2();
            fblock.setSenderName(iam.getUserName());
            fblock.setSenderID(iam.getUserID());
            fblock.setFileName(FilenameUtils.getName(path));
            fblock.setRecivers(getSelectedRecivers());
            try {
                Log.d("INFO", "READ AND SEND FILE HERE");
                SendUtil.readAndSendFile(getActivity(), path, client, iam, getSelectedRecivers(), FileChunkMessageV2.FILE);
                Log.d("OK", "Convert FileTO MODEl");
                SendUtil.convertFileChunkToChatMessageModl(getActivity(), path, fblock, allStudentsLists);

            } catch (IOException e) {
                Toast.makeText(getActivity(),
                        "Error While Sending file", Toast.LENGTH_SHORT).show();
                e.printStackTrace();


            }
        } else if (requestCode == EXAM_SELECT_CODE && resultCode == Activity.RESULT_OK
                && null != data) {
            final Uri uri = data.getData();
            String path = getRealPathFromURI(uri);
            Log.d("INFO", path);
            Toast.makeText(getActivity(),
                    "Exam Loaded", Toast.LENGTH_SHORT).show();

            Log.d("INFO", iam.getUserName());
            Log.d("INFO", iam.getUserType());

            FileChunkMessageV2 fblock = new FileChunkMessageV2();
            fblock.setSenderName(iam.getUserName());
            fblock.setSenderID(iam.getUserID());

            //  client.sendTCP(fblock);
            try {
                Log.d("INFO", "READ AND SEND EXAM HERE");
                SendUtil.readAndSendFile(getActivity(), path, client, iam, getSelectedRecivers(), FileChunkMessageV2.EXAM);

            } catch (IOException e) {
                Toast.makeText(getActivity(),
                        "Erroe While Sending Exam", Toast.LENGTH_SHORT).show();
                e.printStackTrace();


            }

        }else if(resultCode == MONITOR_CODE && resultCode == Activity.RESULT_OK){

        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
           // Bundle extras = data.getExtras();
           // Bitmap imageBitmap = (Bitmap) extras.get("data");
            //sendCapturedPicToClients(imageBitmap);
            //sendCapturedPicFileToClients(capturedImagePath);
            SendCauptureImageAsFile(capturedImagePath);
            Toast.makeText(getActivity(),
                    "Image Captured And Sent Completely", Toast.LENGTH_SHORT).show();

        }

    }

    private void SendCauptureImageAsFile(String cappath){
        FileChunkMessageV2 fblock = new FileChunkMessageV2();
        fblock.setSenderName(iam.getUserName());
        fblock.setSenderID(iam.getUserID());
        fblock.setFileName(FilenameUtils.getName(cappath));
        fblock.setRecivers(getSelectedRecivers());
        try {
            Log.d("INFO", "READ AND SEND FILE HERE");
            SendUtil.readAndSendFile(getActivity(), cappath, client, iam, getSelectedRecivers(), FileChunkMessageV2.FILE);
            Log.d("OK", "Convert FileTO MODEl");
            SendUtil.convertFileChunkToChatMessageModl(getActivity(), cappath, fblock, allStudentsLists);

        } catch (IOException e) {
            Toast.makeText(getActivity(),
                    "Error While Sending file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();


        }
    }

    private void sendCapturedPicToClients(Bitmap bm) {
        String ciFileName = System.currentTimeMillis() + ".jpg";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedImage = encoder.encode(imageBytes);
        CapturedImageMessage cim = new CapturedImageMessage();
        cim.setSenderID(iam.getUserID());
        cim.setSenderName(iam.getUserName());
        cim.setRecivers(getSelectedRecivers());
        cim.setPicture(encodedImage);
        cim.setFileName(ciFileName);

        final CapturedImageMessage ttcim = cim;
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendTCP(ttcim);
            }
        }).start();

        // Write Image to file
        writeByteImageTofile(imageBytes, ciFileName);
        String savepath = Environment.getExternalStorageDirectory().getPath();
        savepath = savepath + "/Classroom/pics/" + cim.getFileName();
        SendUtil.convertCapturedImageMessageTOChatMessageMode(cim, savepath, allStudentsLists);
    }



    private void writeByteImageTofile(byte[] imageBytes, String imagefileName) {
        String savepath = Environment.getExternalStorageDirectory().getPath();
        savepath = savepath + "/Classroom/pics/";
        File destination = new File(savepath, imagefileName);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(imageBytes);
            fo.close();
            Toast.makeText(getActivity().getApplicationContext(), "Write IMGE DONEe", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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


    private void sendTextMessage(String txtmsg) {
        String[] recivers = getSelectedRecivers();
        TextMeesage currTm = new TextMeesage();
        currTm.setSenderID(iam.getUserID());
        currTm.setSenderName(iam.getUserName());
        currTm.setMessageType("TXT");
        currTm.setTextMessage(txtmsg);
        currTm.setRecivers(recivers);
        client.sendTCP(currTm);

        if (chatMessageModelList != null) {
            // TODO: 27/03/16  this must saved in DB 
            SendUtil.convertTextMessageToChatMessageModl(currTm, allStudentsLists);
        }

    }


    public void sendLockMessage(boolean lockstate) {
        String[] receivers = getSelectedRecivers();
        LockMessage lockMessage = new LockMessage();
        lockMessage.setReceivers(receivers);
        lockMessage.setSenderID(iam.getUserID());
        lockMessage.setSenderName(iam.getUserName());
        lockMessage.setLock(lockstate);
        client.sendTCP(lockMessage);
        isLock = !isLock;
        Log.i("Lock", "Lock Message send");
    }

    private String[] getSelectedRecivers(){
        Log.d("INFO", "Get selected recivers");
        String[] recivers = null;
        List<String> selectedClients = new ArrayList<>();
        for (ClientModel temp : l1) {
            if (temp.isClientSelected()) {
                selectedClients.add(temp.getClientID());
            }
        }
        if (!(selectedClients.isEmpty())) {
            recivers = new String[selectedClients.size()];
            selectedClients.toArray(recivers);
        }

        return  recivers;

    }


    public void updateActiveListContent() {
        try{

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    clientListAdapter.notifyDataSetChanged();
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    private boolean isUserExistUpdate(UserLogin curr) {
        for (ClientModel ul1 : l1) {
            if (curr.getUserID().equals(ul1.getClientID())) {
                ul1.setClientName(curr.getUserName());
                ul1.setClientImage(curr.getUserIMage());
                return true;
            }
        }

        return false;
    }

    /// this functions used to save captured image from cam;
    public void captureAndSavePicture() {
        String savepath = Environment.getExternalStorageDirectory().getPath();
        String saveDirectoryPath = savepath + "/Classroom/pics/";
        File folders = new File(saveDirectoryPath);

        if (!(folders.exists())) {
            folders.mkdirs();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "CaptureCMS_" + date + ".jpg";

         capturedImagePath = saveDirectoryPath + photoFile;

        File NewImageFile = new File(capturedImagePath);
        try {
            NewImageFile.createNewFile();
        } catch (IOException e) {
        }

        Uri outputFileUri = Uri.fromFile(NewImageFile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_users, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //  mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnFragmentInteractionListener) activity;
        mListener.onFragmentInteraction(2);
        /*try {
         //   mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        mListener.onFragmentInteraction(-2);
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int fragmentID);

        void addNewChatModelMessage(ChatMessageModel cml);
         void ShowMessagesViewer(ClientModel clientModel);

        void showMonitor(String[] receivers);
    }



}
