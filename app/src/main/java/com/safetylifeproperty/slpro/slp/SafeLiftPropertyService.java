package com.safetylifeproperty.slpro.slp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class SafeLiftPropertyService extends Service {
    BroadcastReceiver Receiver_sms = new BroadCastForSms();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        Receiver_sms = new BroadCastForSms();
        registerReceiver(Receiver_sms, filter); //동적 리시버 등록
        Log.d("test", "서비스의 onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");
        if (intent == null) {
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            Receiver_sms = new BroadCastForSms();
            registerReceiver(Receiver_sms, filter); //동적 리시버 등록

        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        unregisterReceiver(Receiver_sms);
        Log.d("test", "서비스의 onDestroy");
    }
}

