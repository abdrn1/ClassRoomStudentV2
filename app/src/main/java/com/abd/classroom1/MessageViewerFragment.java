package com.abd.classroom1;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.esotericsoftware.kryonet.Client;

import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Decoder.BASE64Encoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageViewerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int FRAG_ID = 3;
    private static int FILE_SELECT_CODE = 2;
    private static int REQUEST_TAKE_PHOTO = 5;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context  mycontext;

    private Client client;
    private UserLogin iam;
    private String reciverID;
    private ListView listview;
    private List<ChatMessageModel> l1;
    private MessagesListAdapter mLAdapter;
    private String capturedImagePath="";
    private ClientModel reciverClient;
    private CmImageViewerFragment imageViwerFrag;
    private static final int IMGVIWER = 6;


    public MessageViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageViewerFragment newInstance(String param1, String param2) {
        MessageViewerFragment fragment = new MessageViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setReciverID(String rID){
        this.reciverID=rID;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview = (ListView) getActivity().findViewById(R.id.list_view_messages);

       /* l1 = new ArrayList();

        l1.add(new ChatMessageModel("ABD", "Hassan", "SIMPLE", "Hello There How?", true));
        l1.add(new ChatMessageModel("Hassan", "Hassan", "SIMPLE", "Nice OK .....", false));*/

        mLAdapter = new MessagesListAdapter(getActivity(), l1);
        listview.setAdapter(mLAdapter);
        final EditText inputMsg = (EditText)getActivity().findViewById(R.id.inputMsg);
        ImageButton btnsend = (ImageButton) getActivity().findViewById(R.id.btnSend);
        ImageButton btnsendfile = (ImageButton) getActivity().findViewById(R.id.btn_msgv_sendfile);
        ImageButton btnCaptureImage = (ImageButton) getActivity().findViewById(R.id.btn_msgv_captureimage);
        ImageButton btnlike = (ImageButton)getActivity().findViewById(R.id.btn_like);
        ImageView clientImage =(ImageView)getActivity().findViewById(R.id.useimageiv);
        TextView clientName =(TextView)getActivity().findViewById(R.id.username_tv);

        clientImage.setImageResource(reciverClient.getClientImage());
        clientName.setText(" :: : "+reciverClient.getClientName());


        GeneralUtil.buttonEffect(btnsendfile);
        GeneralUtil.buttonEffect(btnsend);
        GeneralUtil.buttonEffect( btnlike);
        GeneralUtil.buttonEffect( btnCaptureImage);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String msgType = (l1.get(position)).getMessageType();
                Log.d("item", "The Itemclicked " + msgType);
                if (msgType.equals("IMG")) {
                    String savePath;
                   // String fname = (l1.get(position)).getSimpleMessage();
                   // String savePath = Environment.getExternalStorageDirectory().getPath();
                   savePath = (l1.get(position)).getFilepath();
                  //  GeneralUtil.openImage(getActivity(), savePath);

                    mListener.showImageViewer(savePath);


                }

            }
        });

        btnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SendUtil.checkConnection(client,iam)){
                    sendOkMessage();
                }else{
                    Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                PopupMenu popup = new PopupMenu(getActivity(),view);
                popup.getMenuInflater().inflate(R.menu.monitor_item_display, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.forward_Item) {

                        }else if(item.getItemId() == R.id.show_on_monitor){
                            //    showOnBoard(position);
                        }else if(item.getItemId() == R.id.zoomIN){
                         //   client.sendTCP(new CommandsMessages(1.2));
                        }else if(item.getItemId() == R.id.zoomOUT){
                          //  client.sendTCP(new CommandsMessages(0.9));
                        }else if(item.getItemId() == R.id.open_external){
                            String savePath = (l1.get(position)).getFilepath();
                             GeneralUtil.openImage(getActivity(), savePath);
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(SendUtil.checkConnection(client,iam)) {
                           // takePicture();
                            captureAndSavePicture();
                        }else{
                            Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();

            }
        });


        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SendUtil.checkConnection(client,iam)) {
                    String msg = inputMsg.getText().toString();
                    //String savepath = Environment.getExternalStorageDirectory().getPath();
                    // Toast.makeText(getActivity(), savepath, Toast.LENGTH_LONG).show();
                    if (!(msg.equals(""))) {
                        sendTextMessage(msg);
                        inputMsg.setText("");
                    }
                }else{
                    Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
        btnsendfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        if(SendUtil.checkConnection(client,iam)) {
                            sendFile();
                        }else{
                            Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

        });



    }

    public ClientModel getReciverClient() {
        return reciverClient;
    }

    public void setReciverClient(ClientModel reciverClient) {
        this.reciverClient = reciverClient;
    }

    private void sendOkMessage(){
        TextMeesage currTm = new TextMeesage();
        currTm.setSenderID(iam.getUserID());
        currTm.setSenderName(iam.getUserName());
        currTm.setMessageType("OK");
        currTm.setTextMessage("");
        currTm.setRecivers(new String[]{reciverID});
        addNewMessage(new SimpleTextMessage(iam.getUserID(), iam.getUserName(), "OK", ""), true);
        client.sendTCP(currTm);
    }

    public void setMessagesList(List<ChatMessageModel> ll) {
        this.l1 = ll;
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

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

    private void showOnBoard(int itemIndex){
        ChatMessageModel chatMessageModel = this.l1.get(itemIndex);
        ShowOnBoardMessage  sob= new ShowOnBoardMessage();
        sob.setSenderID(iam.getUserID());
        if(chatMessageModel.getMessageType().equals("IMG")){
            sob.setFileName(chatMessageModel.getSimpleMessage());
            sob.setMessageType("IMG");
        }
        client.sendTCP(sob);

    }
    private void sendTextMessage(String txtmsg) {
        TextMeesage currTm = new TextMeesage();
        currTm.setSenderID(iam.getUserID());
        currTm.setSenderName(iam.getUserName());
        currTm.setMessageType("TXT");
        currTm.setTextMessage(txtmsg);
        currTm.setRecivers(new String[]{reciverID});
        SimpleTextMessage sm1 = new SimpleTextMessage(iam.getUserID(), iam.getUserName(), "TXT", txtmsg);
        addNewMessage(sm1, true);
        // mListener.addNewTextMessageFromMessageViewer(sm1);

        client.sendTCP(currTm);
    }

    private void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, FILE_SELECT_CODE);
        Toast.makeText(getActivity(),
                "INfo Message", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK
                && null != data) {
            final Uri uri = data.getData();
            String path = SendUtil.getRealPathFromURI(uri, getActivity());
            // String path = getRealPathFromURI(uri);
            Toast.makeText(getActivity(), "File: " + path +
                    ", Loadded Copletely", Toast.LENGTH_SHORT).show();
            FileChunkMessageV2 fblock = new FileChunkMessageV2();
            fblock.setSenderName(iam.getUserName());
            fblock.setSenderID(iam.getUserID());
            fblock.setFileName(FilenameUtils.getName(path));
            try {
                Log.d("INFO", "READ AND SEND FILE HERE");
                SendUtil.readAndSendFile(getActivity(), path, client, iam, new String[]{reciverID}, FileChunkMessageV2.FILE);
                addNewImageMessage(fblock, path, true);
            } catch (IOException e) {
                Toast.makeText(getActivity(),
                        "Error While Sending file", Toast.LENGTH_SHORT).show();
                e.printStackTrace();


            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //sendCapturedPicToClients(imageBitmap);
            SendCapturedImageFile();
            Toast.makeText(getActivity(),
                    "Image Captured And Sent Completely", Toast.LENGTH_SHORT).show();

        }
    }

    private void SendCapturedImageFile(){
        FileChunkMessageV2 fblock = new FileChunkMessageV2();
        fblock.setSenderName(iam.getUserName());
        fblock.setSenderID(iam.getUserID());
        fblock.setFileName(FilenameUtils.getName(capturedImagePath));
        try {
            Log.d("INFO", "READ AND SEND FILE HERE");
            SendUtil.readAndSendFile(getActivity(), capturedImagePath, client, iam, new String[]{reciverID}, FileChunkMessageV2.FILE);
            addNewImageMessage(fblock, capturedImagePath, true);
        } catch (IOException e) {
            Toast.makeText(getActivity(),
                    "Error While Sending file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();


        }
    }

    private void sendCapturedPicToClients(Bitmap bm) {
        String ciFileName = System.currentTimeMillis() + ".jpg";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedImage = encoder.encode(imageBytes);
        CapturedImageMessage cim = new CapturedImageMessage();
        cim.setSenderID(iam.getUserID());
        cim.setSenderName(iam.getUserName());
        cim.setRecivers(new String[]{reciverID});
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
        addNewCapturedImageMessage(cim, savepath);


        //SendUtil.convertCapturedImageMessageTOChatMessageMode(cim,savepath,allStudentsLists);
    }

    private void writeByteImageTofile(byte[] imageBytes, String imagefileName) {
        String savepath = Environment.getExternalStorageDirectory().getPath();
        savepath = savepath + "/Classroom/pics/";
        File folders = new File(savepath);
        File destination = new File(savepath, imagefileName);
        FileOutputStream fo;
        try {
            if (!(folders.exists())) {
                folders.mkdirs();
            }
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

    public void updateMessageListContent() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addNewMessage(SimpleTextMessage simplem, boolean fromMe) {

        if (simplem.getMessageType().equals("TXT")) {
            ChatMessageModel chm = new ChatMessageModel(simplem.getSenderName(), "", "TXT", simplem.getTextMessage(), fromMe);
            l1.add(chm);

        }else if(simplem.getMessageType().equals("OK")) {
            ChatMessageModel chm = new ChatMessageModel(simplem.getSenderName(), "", "OK", simplem.getTextMessage(), fromMe);
            l1.add(chm);
        }

    }

    protected ChatMessageModel addNewCapturedImageMessage(CapturedImageMessage cim, String filePath) {
        ChatMessageModel temp = new ChatMessageModel();
        temp.setIsSelf(true);
        temp.setSenderID(cim.getSenderID());
        temp.setSenderName(cim.getSenderName());
        temp.setFilepath(filePath);
        temp.setSimpleMessage(cim.getFileName());
        Bitmap bm1 = ScalDownImage.decodeSampledBitmapFromResource(filePath, 80, 80);
        temp.setImage(bm1);
        temp.setMessageType("IMG");
        l1.add(temp);
        updateAdapterchanges();
        return temp;
    }

    protected ChatMessageModel addNewImageMessage(FileChunkMessageV2 fcmv2, String filepath, boolean fromMe) {
        String fileType="FLE";

        if(GeneralUtil.checkIfFileIsImage(fcmv2.getFileName())){
            fileType = "IMG";
        }
        ChatMessageModel chm = new ChatMessageModel(fcmv2.getSenderName(), "", fileType,fcmv2.getFileName()+" Recived, Wait To Complete Loading", fromMe);
        chm.setFilepath(filepath);
        Bitmap bm;

        if (fileType.equals("IMG")) {
            bm = ScalDownImage.decodeSampledBitmapFromResource(filepath, 80, 80);
            chm.setImage(bm);
            chm.setMessageType("IMG");
            chm.setSimpleMessage(fcmv2.getFileName());
        } else {
            bm = BitmapFactory.decodeResource(getResources(), R.drawable.filecompleteicon);
            chm.setImage(bm);
            chm.setSimpleMessage(fcmv2.getFileName());
            chm.setMessageType("FLE");
        }

        chm.setImage(bm);
        l1.add(chm);
        updateAdapterchanges();
        return  chm;
    }
    protected  void updateAdapterchanges(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_viewer, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            // mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnFragmentInteractionListener) activity;
        mListener.onFragmentInteraction(3);

    }

    @Override
    public void onDetach() {
        mListener.onFragmentInteraction(-3);
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
        // public void onFragmentInteraction(Uri uri);
        void onFragmentInteraction(int fragmentID);
        void showImageViewer(String imagePath);

        void addNewTextMessageFromMessageViewer(SimpleTextMessage sm);
    }

}
