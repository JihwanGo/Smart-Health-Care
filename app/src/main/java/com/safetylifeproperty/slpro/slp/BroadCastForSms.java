package com.safetylifeproperty.slpro.slp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by admin on 2017-09-02.
 */

public class BroadCastForSms extends BroadcastReceiver { //문자 감지

    @Override
    public void onReceive(Context context, Intent intent) {

        // 수신한 액션을 이 onReceive메소드에서 처리하게 됩니다
        if (Intent.ACTION_SCREEN_ON == intent.getAction()) {

            // 화면 켜짐

        }

        if (Intent.ACTION_SCREEN_OFF == intent.getAction()) {

            // 화면 꺼짐

        }

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {


            //sms_view(context,intent);
            test(context, intent);
        }

    }


    public void test(Context context, Intent intent) {
        Log.d("onReceive()", "문자가 수신되었습니다");
        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        //String format = intent.getStringExtra("format");

        SmsMessage sms = msgs[0];
        String message = sms.getMessageBody(); //메시지 내용
        String addr = sms.getOriginatingAddress(); //보낸이
        Date rev_time = new Date(sms.getTimestampMillis()); //받은시간 rev_time.toString();

        // if(message.contains("환자") && message.contains("요청") && message.contains("위도") && message.contains("경도") && message.contains("SafetyLifeProperty")){
        if (message.contains("환자") && message.contains("발생") && message.contains("위도") && message.contains("경도")) {

            Log.d("SMS sender ", message.toString());

            int x = message.indexOf("위도");
            int y = message.indexOf("경도");
            int z = message.indexOf("S.L.P");

            String result = message.substring(x + 5, y);
            result = result.trim();
            x = result.indexOf(",");
            result = result.substring(0, x);
            result = result.trim();//위도

            String result2 = message.substring(y + 4, z - 1);
            result2 = result2.trim();


            Intent i = new Intent(context, PatientMapsActivity.class);
            i.putExtra("x", result);
            i.putExtra("y", result2);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
            try {
                pi.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }


        }
        this.abortBroadcast();
    }

}

