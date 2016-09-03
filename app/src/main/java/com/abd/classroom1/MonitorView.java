package com.abd.classroom1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.esotericsoftware.kryonet.Client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


/**
 * Created by PROBOOK on 3/29/2016.
 */
public class MonitorView implements MonitorFragment.ScreenshotListener {

    View view;
    Context context;
    String senderId;
    String receiverId;
    Client client;
    FragmentManager fm;
    public static final int monitorHeight = 500;
    public static final int defaultPadding = 10;
    ImageView monitorView;
    SingleMonitorFragment singleMonitorFragment;
    Bitmap bm;
    UserLogin iam;

    public MonitorView(final FragmentManager fm, final Context context, String senderId, final String receiverId, final Client client){
        this.fm = fm;
        this.context = context;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.client = client;
        monitorView = new ImageView(context);
        monitorView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        monitorView.setImageResource(R.drawable.lock);
        LinearLayout.LayoutParams monitorParams = new LinearLayout.LayoutParams(0, monitorHeight);
        monitorParams.weight = 1;
        monitorParams.setMargins(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
        monitorView.setLayoutParams(monitorParams);
        monitorView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(context, monitorView);
                popup.getMenuInflater().inflate(R.menu.monitor_context_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.enlargeMonitor) {
                            FragmentTransaction ft = fm.beginTransaction();
                            if (singleMonitorFragment == null) {
                                singleMonitorFragment = new SingleMonitorFragment();
                            }
                            singleMonitorFragment.setScreenshot(bm);
                            ft.replace(R.id.fragment_container, singleMonitorFragment, "monitor");
                            ft.addToBackStack(null);
                            ft.commit();
                        } else if (item.getItemId() == R.id.updateMonitor) {
                            update();
                        } else if (item.getItemId() == R.id.showOfMonitor) {
                            Log.d("MON","Show On monitor ");

                            if (bm != null) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                                byte[] imageBytes = baos.toByteArray();

                                BASE64Encoder encoder = new BASE64Encoder();
                                String encodedImage = encoder.encode(imageBytes);

                                try {
                                    baos.close();
                                } catch (IOException e) {
                                    Log.d("MON","error here , func: show on board ");
                                    e.printStackTrace();
                                }
                                final BoardScreenshotMessage bsm = new BoardScreenshotMessage();
                                bsm.setReceiverId(receiverId);
                                bsm.setBase64Photo(encodedImage);
                                Log.d("MON","SEND to show on board ");
                                if(SendUtil.checkConnection(client,iam)) {
                                    client.sendTCP(bsm);
                                }else{
                                    Log.d("MON","Connection Failed ");
                                }


                            }
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });
        view = monitorView;
    }

    public View getView(){
        return  view;
    }

    public void update(){
        MonitorRequestMessage message = new MonitorRequestMessage();
        message.setReceiverID(receiverId);
        message.setSenderID(senderId);
        client.sendTCP(message);
    }

    @Override
    public void screenshotReceived(ScreenshotMessage msg) {
        if(msg.getSenderID().equalsIgnoreCase(receiverId)) {
            Log.i("ttt", "Message received");
            String encodedImage = ((ScreenshotMessage) msg).getScreenshot();

            try {
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] imageBytes = decoder.decodeBuffer(encodedImage);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);

                bm = BitmapFactory.decodeStream(bis);
                monitorView.post(new Runnable() {

                    @Override
                    public void run() {
                        monitorView.setImageBitmap(bm);
                    }
                });
                bis.close();

            } catch (IOException e) {
                Log.d("ERR","error here , func: screenshotReceived() ");
                e.printStackTrace();
            }
        }
    }
}
