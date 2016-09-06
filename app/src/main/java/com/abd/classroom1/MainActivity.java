package com.abd.classroom1;

import android.app.FragmentManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.abd.classroom1.service.MessageService;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.OnFragmentInteractionListener, Runnable,
        ActiveUsersFragment.OnFragmentInteractionListener,
        MessageViewerFragment.OnFragmentInteractionListener,
        ExamResultViewerFragment.OnFragmentInteractionListener, MonitorFragment.OnFragmentInteractionListener,
        ClientListAdapter.OnClientListAdapterInteraction,
        ExamViewerFragment.OnFragmentInteractionListener,
        AddRemoveSync, OnServiceInteractionListener {

    // begin note : should be save later in the bundle;
    private final int LOGINFRAG = 1;
    private final int ACTIVEUSERSFRAG = 2;
    private final int MESSSAGEVIWERFRAG = 3;
    private final int EXAMRESULTFRAG = 4;
    private final int EXAMVIWER = 5;
    private static final int IMGVIWER = 6;


    private static final int ADMIN_INTENT = 15;
    Client client;
    Kryo kryo;
    FragmentManager fm;
    FragmentTransaction ft;
    // the fragments
    LoginFragment loginfrag;
    ActiveUsersFragment activeusersfragment;
    MessageViewerFragment messageViewerFragment;
    ExamViewerFragment examfrag;
    ExamResultViewerFragment examResultViewerFragment;
    CmImageViewerFragment imgViewerFragment;
    MonitorFragment monitorFragment;
    String[] clientStatus;
    ///
    private UserLogin iam = null;
    private UserLogin loginDetail = null;
    private List<ChatMessageModel> chatMessageModelList;
    private List<ClientModel> clientsList;
    private List<ExamResultModel> examResultModels;
    private Hashtable<String, List<ChatMessageModel>> allStudentsLists;
    List<QuestionItem> tQuestionsList;
    private Thread checkServer;
    private int activeFragmentID = 1;
    private Hashtable<RecivedFileKey, BuildFileFromBytesV2> recivedFilesTable;
    private Handler handler;
    Listener myListener;


    private static final String description = "Sample Administrator description";
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    private Context mycontext;

    MessageService messageService;
    boolean mBound = false;

// begin connection with MessageService

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessageService.LocalBinder localBinder = (MessageService.LocalBinder) service;
            messageService = localBinder.getService();
            messageService.setsBounded(true);
            mBound = true;
            Log.i("info", "Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //messageService = null;
            //// TODO: 9/4/2016  this could make error if service = nill 
            messageService.setsBounded(false);
            mBound = false;


        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ACTIVEFRAG",activeFragmentID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        activeFragmentID = savedInstanceState.getInt("ACTIVEFRAG");
    }

    public List<QuestionItem> gettQuestionsList() {
        return tQuestionsList;
    }

    public void settQuestionsList(List<QuestionItem> tQuestionsList) {
        this.tQuestionsList = tQuestionsList;
    }

    // end connection with Messsage Service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        mycontext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set up lock As admin

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);
        recivedFilesTable = new Hashtable<>();

        Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description);
        startActivityForResult(intent1, ADMIN_INTENT);


        ExamResultModel aa = new ExamResultModel();
        aa.setClientID("105");
        aa.setClientName("Radwan");
        aa.setClientImage(R.drawable.u27);
        aa.setStudentMark(25);
        aa.setExamMark(50);
        chatMessageModelList = Collections.synchronizedList(new ArrayList<ChatMessageModel>());
        allStudentsLists = new Hashtable<>();

        examResultModels = new ArrayList<>(); // for saving exam result
        examResultModels.add(aa);
        clientsList = new ArrayList<>();// for saving active clients
        clientStatus = getResources().getStringArray(R.array.client_status); // String Array of clients status


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fm = getFragmentManager();
        loginfrag = (LoginFragment) fm.findFragmentByTag("LOGIN");
        if (loginfrag == null) {
            loginfrag = new LoginFragment();
        }
        ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, loginfrag, "LOGIN");
        ft.commit();
        prepareConnection();
        checkServer = new Thread(this);
        checkServer.start();
        Log.d("LIFE", "OnCreate()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            if (activeFragmentID == EXAMVIWER) {
                return;
            }

            if (activeFragmentID == IMGVIWER) {
                this.getSupportActionBar().show();
                activeFragmentID = MESSSAGEVIWERFRAG;
            } else {
                activeFragmentID = ACTIVEUSERSFRAG;
            }
            getFragmentManager().popBackStack();
            Log.d("Info", "Acrive frag ID = :" + Integer.toString(activeFragmentID));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mi_exam_result) {
            Log.d("frag", "Curren Active = " + Integer.toString(activeFragmentID));
            // Handle the camera action
            if (activeFragmentID == EXAMRESULTFRAG) {
                examResultViewerFragment.updateExamResultContent();
            } else {
                if (iam != null && ft != null) {
                    examResultViewerFragment = ExamResultViewerFragment.newInstance(examResultModels);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, examResultViewerFragment, "EXAMEXAM");
                    examResultViewerFragment.setL1(examResultModels);
                    activeFragmentID = EXAMRESULTFRAG;
                    ft.addToBackStack(null);
                    //examResultViewerFragment.set(iam);
                    ft.commit();


                }

            }
        } else if (id == R.id.nmi_active_users) {

            if (activeFragmentID == ACTIVEUSERSFRAG) {
                activeusersfragment.updateActiveListContent();
            } else {
                if (iam != null && ft != null) {
                    activeusersfragment = new ActiveUsersFragment();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, activeusersfragment, "EXAMEXAM");
                    activeusersfragment.setClient(client);
                    activeusersfragment.setActiveUsersList(clientsList);
                    activeusersfragment.setUserlogin(iam);
                    activeFragmentID = ACTIVEUSERSFRAG;
                    ft.commit();


                }
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /// ABd Add this code
    private void prepareConnection() {
        client = new Client((1024 * 1024), (1024 * 1024) / 10);
        kryo = client.getKryo();
        kryo.register(byte[].class);
        kryo.register(String[].class);
        kryo.register(UserLogin.class);
        kryo.register(TextMeesage.class);
        kryo.register(SimpleTextMessage.class);
        kryo.register(FileChunkMessageV2.class);
        kryo.register(LockMessage.class);
        kryo.register(StatusMessage.class);
        kryo.register(ExamResultMessage.class);
        kryo.register(MonitorRequestMessage.class);
        kryo.register(ScreenshotMessage.class);
        kryo.register(BoardScreenshotMessage.class);
        kryo.register(CapturedImageMessage.class); // class to send captured image
        kryo.register(ShowOnBoardMessage.class);
        kryo.register(CommandsMessages.class);
    }

    public boolean openConnection() throws Exception {
        client.start();
        InetAddress address = client.discoverHost(54777, 5000);
        client.connect(5000, address, 9995, 54777);
        client.setKeepAliveUDP(7000);
        client.setKeepAliveTCP(7000);
        client.setTimeout(50000);

        if (myListener == null) {

           myListener = new Listener() {
                public void received(Connection c, Object ob) {
                    if (ob instanceof SimpleTextMessage) {


                    } else if (ob instanceof UserLogin) {
                        UserLogin tUL = (UserLogin) ob;
                        if (!((UserLogin) ob).isLogin_Succesful()) {
                            showInvalidUserNameOrPassword();
                            return;
                        }
                        if (tUL.getUserType().equals("STUDENT") && (iam == null)) {
                            if (tUL.getUserID().equals(loginDetail.getUserID())) {
                                System.out.println("Login Message Recived");

                                setSuccessfulLogin(((UserLogin) ob));
                                fm = getFragmentManager();
                                ft = fm.beginTransaction();
                                activeusersfragment = new ActiveUsersFragment();
                                // }
                                ft.replace(R.id.fragment_container, activeusersfragment, "ACTIVE");
                                UserLogin teacher = new UserLogin("100", "TEACHER", "Teacher", R.drawable.u100);
                                addNewActiveClient(teacher);

                                iam = (UserLogin) ob;
                                activeusersfragment.setClient(client);
                                activeusersfragment.setUserlogin((UserLogin) ob);
                                activeusersfragment.setActiveUsersList(clientsList);
                                activeusersfragment.setAllStudentsLists(allStudentsLists);


                                ft.commit();
                                if (mBound) {
                                    messageService.setClient(client);
                                    messageService.setIam(iam);
                                    messageService.setAllStudentsLists(allStudentsLists);
                                    messageService.settQuestionsList(tQuestionsList);
                                    messageService.setActivity(MainActivity.this);
                                    messageService.setClientsList(clientsList);
                                    messageService.addClientListener();
                                    Log.i("info", "Listener set to Service");
                                }
                                Log.d("INFO", "Succesfull Log IN");
                            }

                        } else if (iam != null) {
                     /*   addNewActiveClient((UserLogin) ob);
                        if (activeFragmentID == ACTIVEUSERSFRAG && activeusersfragment != null) {

                            activeusersfragment.setActiveUsersList(clientsList);

                            activeusersfragment.updateActiveListContent();

                        }*/
                        }
                    } else if (ob instanceof StatusMessage) {
                        dealWithStatusMessage((StatusMessage) ob);
                    } else if (ob instanceof ExamResultMessage) {
                        Log.d("INFO", "Exam Result Message Recived");
                        dealWithExamResultMessage((ExamResultMessage) ob);
                    } else if (ob instanceof LockMessage) {
                        Log.d("FILE", "Lock Message received");
                        LockMessage msg = (LockMessage) ob;
                        if (msg.isLock()) {
                            boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
                            if (isAdmin) {
                                mDevicePolicyManager.setPasswordQuality(mComponentName, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
                                mDevicePolicyManager.setPasswordMinimumLength(mComponentName, 5);
                                mDevicePolicyManager.resetPassword("123456", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
                                mDevicePolicyManager.lockNow();

                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
                                        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                                    }
                                });


                            } else {
                            }
                        } else {
                            mDevicePolicyManager.setPasswordQuality(mComponentName, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
                            mDevicePolicyManager.setPasswordMinimumLength(mComponentName, 0);
                            mDevicePolicyManager.resetPassword("", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
                                }
                            });

                        }
                    } else if (ob instanceof MonitorRequestMessage) {

                        Log.i("ttt", "Monitor request received at client ....");
                        Bitmap bm = screenShot(getWindow().getDecorView().getRootView());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] imageBytes = baos.toByteArray();

                        BASE64Encoder encoder = new BASE64Encoder();
                        String encodedImage = encoder.encode(imageBytes);

                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final ScreenshotMessage m = new ScreenshotMessage();
                        m.setSenderID(iam.getUserID());
                        m.setReceiverID(((MonitorRequestMessage) ob).getSenderID());
                        m.setScreenshot(encodedImage);
                        Log.i("ttt", encodedImage.getBytes().length + "");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                client.sendUDP(m);

                            }
                        }).start();

                    }
                }
            };


            client.addListener(myListener);
        }


        return true;


    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void dealWithExamMessage(FileChunkMessageV2 efMessage) {

        BuildFileFromBytesV2 buildExamFromBytes = null;
        try {

            Log.d("INFO", "EXAM File  Recived");
            String internalSavePath = this.getApplicationContext().getFilesDir().getPath();
            String tempfilename = "";
            //recive the first packet from new file
            if (efMessage.getChunkCounter() == 1L) {
                Log.d("INFO", "New EXAM File  recived");
                internalSavePath = this.getApplicationContext().getFilesDir().getPath();
                Log.d("INFO", "Save Path " + internalSavePath);
                tempfilename = efMessage.getFileName();
                buildExamFromBytes = new BuildFileFromBytesV2(internalSavePath + "/");
                recivedFilesTable.put(new RecivedFileKey(efMessage.senderID, efMessage.getFileName()), buildExamFromBytes);

            } else {
                Log.d("INFO", "Current File Chunk: " + Long.toString(efMessage.getChunkCounter()));
                Log.d("INFO", "Current File Chunk: " + efMessage.getSenderID());
                Log.d("INFO", "Current File Chunk: " + efMessage.getSenderName());
                buildExamFromBytes = recivedFilesTable.get(new RecivedFileKey(efMessage.senderID, efMessage.getFileName()));
            }
            if (buildExamFromBytes != null) {
                if (buildExamFromBytes.constructFile(efMessage)) {
                    Log.d("INFO", "EXAM FILE COMPLETE ");
                    recivedFilesTable.remove(new RecivedFileKey(efMessage.senderID, efMessage.getFileName()));

                    Log.d("INFO", "FILE NAME : " + efMessage.getFileName());
                    tQuestionsList = XmlExamParser.examParser(internalSavePath + "/" + efMessage.getFileName());
                    Log.d("EXAM INFO", "List Size = : " + Integer.toString(tQuestionsList.size()));
                    for (QuestionItem qqi : tQuestionsList) {
                        Log.d("EXAM TEXT", qqi.getQuestionText());
                        Log.d("EXAM Type", qqi.getQuestionType());
                        Log.d("Exam Answer", qqi.getQuestionAnswer());

                    }
                    ft = fm.beginTransaction();
                    examfrag = new ExamViewerFragment();
                    examfrag.setClient(client);
                    examfrag.setIam(iam);
                    examfrag.setExamFileName(efMessage.getFileName());
                    examfrag.setQuestionsList(tQuestionsList);
                    ft.replace(R.id.fragment_container, examfrag, "EXAM");
                    activeFragmentID = EXAMVIWER;
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    private void writeByteImageTofile(byte[] imageBytes, String imagefileName) throws Exception {
        String savepath = Environment.getExternalStorageDirectory().getPath();
        savepath = savepath + "/Classroom/pics/";
        File destination = new File(savepath, imagefileName);
        FileOutputStream fo;

        destination.createNewFile();
        fo = new FileOutputStream(destination);
        fo.write(imageBytes);
        fo.close();
        //Toast.makeText(getApplicationContext(), "Write IMGE DONEe", Toast.LENGTH_SHORT).show();

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
                recivedFilesTable.put(new RecivedFileKey(fcmv2.senderID, fcmv2.getFileName()), buildfromBytesV2);

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
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.filecompleteicon);
                        icm.setImage(bm);
                        icm.setSimpleMessage(fcmv2.getFileName());
                        icm.setMessageType("FLE");
                    }

                    allStudentsLists.get(fcmv2.getSenderID()).add(icm);

                    if ((activeFragmentID == MESSSAGEVIWERFRAG) && (messageViewerFragment != null)) {
                        messageViewerFragment.updateAdapterchanges();

                    } else {
                        increasetUnreadMessageCounter(icm.getSenderID());
                        if (activeusersfragment != null) {
                            activeusersfragment.updateActiveListContent();
                        }
                    }
                    Log.d("INFO", "EOF, FILE REcived Completely");
                }
                /// SendUtil.sendFileChunkToRecivers(clientTable, fcmv2, tRecivers);
            }

        } catch (Exception ex) {
            recivedFilesTable.remove(new RecivedFileKey(fcmv2.getSenderID(), fcmv2.getFileName()));
            Toast.makeText(getApplicationContext(), "Error While Recive file from Student: " + fcmv2.getSenderName() + ", " + fcmv2.getSenderID(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    public void dealWithExamResultMessage(ExamResultMessage erm) {
        ExamResultModel temp = new ExamResultModel();
        temp.setClientID(erm.getSenderID());
        temp.setClientName(erm.getSenderName());
        int resourceID = getResourseId("u" + erm.getSenderID(), "drawable", getPackageName());
        if (resourceID == -1) {
            resourceID = R.drawable.unknown;
        }
        temp.setClientImage(resourceID);
        temp.setExamMark(erm.getExamresult());
        temp.setStudentMark(erm.getStudentresult());
        temp.examFileName = erm.examFileName;

        // TODO: 26/03/16 we should update row if it is exist
        examResultModels.add(temp);
        if (activeFragmentID == EXAMRESULTFRAG) {
            examResultViewerFragment.updateExamResultContent();
        } else {

        }

    }

    private void dealWithLikeMessage(SimpleTextMessage likem) {
        if (likem.getMessageType().equals("OK")) {
            ChatMessageModel chm = new ChatMessageModel(likem.getSenderName(), "", "OK", likem.getTextMessage(), false);
            chm.setSenderID(likem.getSenderID());
            chatMessageModelList = allStudentsLists.get(likem.getSenderID());
            if (chatMessageModelList != null) {
                chatMessageModelList.add(chm);
            }

            // unread counter

            if ((activeFragmentID == MESSSAGEVIWERFRAG) && (messageViewerFragment != null)) {
                Log.d("info", " Display on Message Viewer");

                // messageViewerFragment.setMessagesList(chatMessageModelList);
                //messageViewerFragment.addNewMessage(simplem,false);
                messageViewerFragment.updateMessageListContent();

            } else {
                Log.d("info", "Display Message On Counter Only");
                increasetUnreadMessageCounter(likem.getSenderID());
                if (activeusersfragment != null) {
                    activeusersfragment.updateActiveListContent();
                }

            }

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

        }
        if ((activeFragmentID == MESSSAGEVIWERFRAG) && (messageViewerFragment != null)) {
            Log.d("info", " Display on Message Viewer");

            // messageViewerFragment.setMessagesList(chatMessageModelList);
            //messageViewerFragment.addNewMessage(simplem,false);
            messageViewerFragment.updateMessageListContent();

        } else {
            Log.d("info", "Display Message On Counter Only");
            increasetUnreadMessageCounter(simplem.getSenderID());
            if (activeusersfragment != null) {
                activeusersfragment.updateActiveListContent();
            }

        }


    }


    private void dealWithStatusMessage(StatusMessage currSm) {
        try {

            ClientModel cm = findCurrentUser(currSm.getUserID());
            if (cm != null) {
                if (cm.getStatus() != currSm.getStatus()) {
                    cm.setLastStatus(clientStatus[currSm.getStatus()]);
                    cm.setStatus(currSm.getStatus());
                    if (activeFragmentID == ACTIVEUSERSFRAG && activeusersfragment != null) {
                        activeusersfragment.updateActiveListContent();
                    }
                }
            }
        } catch (Exception ex) {

        }

    }

    public void addNewActiveClient(UserLogin ul) {
        if (!(ul.getUserID().equals(iam.getUserID()))) {

            int resourceID = getResourseId("u" + ul.getUserID(), "drawable", getPackageName());
            if (resourceID == -1) {
                resourceID = R.drawable.unknown;
            }
            ClientModel t = new ClientModel(ul.getUserID(), ul.getUserName(), resourceID);
            t.setLastStatus(clientStatus[0]);
            t.setStatus(0);
            System.out.println();
            if (!(ifUserExistUpdate(ul))) {
                clientsList.add(t);
                allStudentsLists.put(ul.getUserID(), new ArrayList<ChatMessageModel>());
            }
        }
    }

    private boolean ifUserExistUpdate(UserLogin curr) {
        for (ClientModel ul1 : clientsList) {
            if (curr.getUserID().equals(ul1.getClientID())) {
                ul1.setClientName(curr.getUserName());
                int resourceID = getResourseId("u" + curr.getUserID(), "drawable", getPackageName());
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

    private boolean ifUserExistUpdate(String uID) {
        for (ClientModel ul1 : clientsList) {
            if (uID.equals(ul1.getClientID())) {
                return true;
            }
        }

        return false;
    }

    private ClientModel findCurrentUser(String cuserID) {
        for (ClientModel ul1 : clientsList) {
            if (cuserID.equals(ul1.getClientID())) {
                return ul1;
            }
        }

        return null;
    }

    private void increasetUnreadMessageCounter(String userId) {
        ClientModel temp = findCurrentUser(userId);
        if (temp != null)
            temp.unreadMsgCounter++;
    }

    private void resetUnreadMessageCounter(String userId) {
        ClientModel temp = findCurrentUser(userId);
        if (temp != null)
            temp.unreadMsgCounter = 0;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void setSuccessfulLogin(UserLogin ul) {
        this.iam = ul;
    }

    @Override
    public void researchforServer() {
        checkServer.start();

    }

    @Override
    public void setLoginDetails(UserLogin u) {
        this.loginDetail = u;
    }

    private void showInvalidUserNameOrPassword() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loginfrag.showInvalidLoginMessage();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  Log.d("LIFE", "Activity pause");
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            Log.d("INFO", "hello thread");

            try {
                Thread.sleep(100);
                if (openConnection()) {
                    Log.d("Info", "Connectione done");
                    loginfrag.setClient(client);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginfrag.hideErrorMessage();
                        }
                    });
                    flag = false;
                }

            } catch (Exception e) {
                //  Toast.makeText(MainActivity.this,
                // getResources().getText(R.string.unable_to_connect_server), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }


    }

    public int getResourseId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    @Override
    public void onFragmentInteraction(int fragmentID) {
        this.activeFragmentID = fragmentID;

    }

    @Override
    public void showImageViewer(String imagePath) {
        fm = getFragmentManager();
        // imgViewerFragment = (CmImageViewerFragment) fm.findFragmentByTag("IMGV");
        if (imgViewerFragment == null) {
            imgViewerFragment = new CmImageViewerFragment();

        }
        // imgViewerFragment.showImage(imagePath);
        this.getSupportActionBar().hide();
        ft = fm.beginTransaction();
        imgViewerFragment.setImagePath(imagePath);
        ft.replace(R.id.fragment_container, imgViewerFragment, "IMGV");
        activeFragmentID = IMGVIWER;

        //imgViewerFragment.showImage(imagePath);
        ft.addToBackStack(null);
        ft.commit();
        // mListener.onFragmentInteraction(IMGVIWER);
    }

    @Override
    public void addNewTextMessageFromMessageViewer(SimpleTextMessage sm) {
        // no longer needed
     /*   if (sm.getMessageType().equals("TXT")) {
            ChatMessageModel chm = new ChatMessageModel(sm.getSenderName(), "", "TXT", sm.getTextMessage(), false);
            chm.setSenderID(sm.getSenderID());
            chatMessageModelList.add(chm);
        }*/
    }

    @Override
    public void addNewChatModelMessage(ChatMessageModel cml) {

    }

    @Override
    public void showMonitor(String[] receivers) {
        ShowMonitorViewer(receivers);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        try {

            SendUtil.reConnect(client, iam);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        onStartIni();
    }

    private void onStartIni() {
        super.onStart();
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if (mBound) {
            Log.i("DE", "bounded with service");
            if (!(messageService.isClientOnline())) {
                Log.i("DE", "Client Online");
                // messageService.removeClientListener();
                prepareConnection();

                new Thread(this).start();
            } else {
                this.client = messageService.getClient();
                this.iam = messageService.getIam();
                this.allStudentsLists = messageService.getAllStudentsLists();
                this.clientsList = messageService.getClientsList();
                // this.tQuestionsList = messageService.gettQuestionsList();
                messageService.setActivity(this);
                if (messageService.isNewExam()) {
                    FileChunkMessageV2 aa = new FileChunkMessageV2();
                    aa.setFileName("Quiz");
                    showExamViewer(aa, messageService.gettQuestionsList());
                }

                /*ft = fm.beginTransaction();
                if (messageViewerFragmentFragment == null) {
                    messageViewerFragmentFragment = new MessageViewerFragment();
                }
                ft.replace(R.id.fragment_container, messageViewerFragmentFragment, "chat");
                messageViewerFragmentFragment.setClient(client);
                ft.commit();*/
            }
        } else {
            Log.i("DE", "No service to bound");
            prepareConnection();
            new Thread(this).start();
        }
        Log.i("life", "Bind To Service");
    }

    public void ShowMonitorViewer(String[] receivers) {
        ft = fm.beginTransaction();
        if (monitorFragment == null) {
            monitorFragment = new MonitorFragment();
        }
        //monitorFragment
        monitorFragment.setUserLogin(iam);
        monitorFragment.setReceivers(receivers);
        monitorFragment.setClient(client);
        ft.replace(R.id.fragment_container, monitorFragment, "MONITOR");
        activeFragmentID = 4;
        ft.addToBackStack(null);
        ft.commit();

    }

    public void ShowMessagesViewer(ClientModel clientModel) {
        ft = fm.beginTransaction();
        if (messageViewerFragment == null) {
            messageViewerFragment = new MessageViewerFragment();
        }
        messageViewerFragment.setUserlogin(iam);
        messageViewerFragment.setClient(client);
        String useriD = clientModel.getClientID();
        messageViewerFragment.setReciverID(useriD);
        //  List<ChatMessageModel> temp = SendUtil.getClientUnreadMessages(useriD, chatMessageModelList);
        messageViewerFragment.setReciverClient(clientModel);
        messageViewerFragment.setMessagesList(allStudentsLists.get(useriD));
        ft.replace(R.id.fragment_container, messageViewerFragment, "VIEWMSG");
        activeFragmentID = MESSSAGEVIWERFRAG;
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public synchronized void AddNewChatModel(ChatMessageModel newCml) {
        chatMessageModelList.add(newCml);

    }

    @Override
    public void retuntoPreviousScreen(int FRAGMENTID) {
        if (FRAGMENTID == EXAMVIWER) {
            if (activeusersfragment == null) {
                activeusersfragment = new ActiveUsersFragment();
            }
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, activeusersfragment, "ACTIVE");
            activeusersfragment.setClient(client);
            activeusersfragment.setUserlogin(iam);
            activeusersfragment.setActiveUsersList(clientsList);
            activeusersfragment.setAllStudentsLists(allStudentsLists);
            ft.commit();

        }
    }

    @Override
    public void displayExamResult(ExamResultMessage erm) {
        dealWithExamResultMessage(erm);
    }

    @Override
    public void updateMessageViewer(String uID) {
        try {
            if ((activeFragmentID == MESSSAGEVIWERFRAG) && (messageViewerFragment != null)) {
                Log.d("info", " Display on Message Viewer");
                messageViewerFragment.updateMessageListContent();

            } else {
                Log.d("info", "Display Message On Counter Only");
                increasetUnreadMessageCounter(uID);
                if (activeusersfragment != null) {
                    activeusersfragment.updateActiveListContent();
                }

            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void showExamViewer(FileChunkMessageV2 fcmv2, List<QuestionItem> tQuestionsList) {
        ft = fm.beginTransaction();
        examfrag = new ExamViewerFragment();
        examfrag.setClient(client);
        examfrag.setIam(iam);
        examfrag.setExamFileName(fcmv2.getFileName());
        examfrag.setQuestionsList(tQuestionsList);
        ft.replace(R.id.fragment_container, examfrag, "EXAM");
        activeFragmentID = EXAMVIWER;
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void updateClientsList() {
        try {
            if (activeFragmentID == ACTIVEUSERSFRAG && activeusersfragment != null) {

                activeusersfragment.setActiveUsersList(clientsList);

                activeusersfragment.updateActiveListContent();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
