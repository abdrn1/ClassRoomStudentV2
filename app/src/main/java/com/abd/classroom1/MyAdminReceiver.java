package com.abd.classroom1;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAdminReceiver extends DeviceAdminReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}