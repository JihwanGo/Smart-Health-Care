package com.safetylifeproperty.slpro.slp;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//앱에서 공통적으로 쓰는 공통 라이브러리 - SharedPreferences,  SharedPreferences.Editor editor
    //1. popup_stats(flag) == 팝업이 최초 여부 확인 플래그, 최초일 경우 1을 저장, 이후 체크하여 출력여부 결정
    //2. Medical_Staff(flag) ==  2은 의료진, 1은 환자, 0은 일반인
    //2-1. 의료진일 경우 특정 문자가 올경우 감지하여 자동으로 지도 표시
    //2-1-1. 리시버 (문자 감지 기능) - 1. 동적리시버: 등록과해제 자유로움,어플종료시 문자수신못함 2. 정적리시버 : 해제불가, 어플 종료시 문자수신가능
    //해결법 : 서비스를 이용하여 동적리시버 사용, 서비스는 어플이 종료해도 백그라운드로 동작 // 문자 수신사용안할경우, 서비스종료 와 함께 리시버 해제
    //2-2. 환자일 경우 문자 감지 기능 정지
    //3. 의료진일경우 자동으로 환자 목록 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button patient_btn = (Button) findViewById(R.id.button);
        Button medical_staff_btn = (Button) findViewById(R.id.button2);
        //커스텀 액션바 생성
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);
        actionBar.setCustomView(actionbar);
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);




        airplane_ck();
        permition_ck();
        popup_show();
        String str = app_lib_access_output("popup_stats", "flag");
        if (str.equals("1")) {
            app_lib_access_input("Medical_Staff", "flag", "2"); //임시 등급 변경 //2 == 의료진, 1== 환자, 0== 일반인
            Medical_or_Patient_Check();
        }


        patient_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PatientActivity.class);
                startActivity(intent);
            }
        });
        medical_staff_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, medical_staff_Activity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServiceRunningCheck() { //서비스 실행 확인
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("SafeLiftPropertyService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean getServiceTaskName(String pk_name) { //서비스 체크
        boolean checked = true;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info;
        info = am.getRunningServices(30);
        for (Iterator iterator = info.iterator(); iterator.hasNext(); ) {
            ActivityManager.RunningServiceInfo runningTaskInfo = (ActivityManager.RunningServiceInfo) iterator.next();

            if (runningTaskInfo.service.getClassName().equals(pk_name)) {
                checked = false;
            }
        }
        return checked;
    }


    public void ServiceStart(String Service_Name) {
        if (getServiceTaskName("com.safetylifeproperty.slpro.slp")) {
            Intent intent = new Intent(getApplicationContext(), SafeLiftPropertyService.class);
            startService(intent); // 문자 감지 시작
        }
    }

    public void ServiceEnd(String Service_Name) {
        if (!getServiceTaskName("com.safetylifeproperty.slpro.slp")) {
            Intent intent = new Intent(getApplicationContext(), SafeLiftPropertyService.class);
            stopService(intent); //sms 문자 감지 종료(해제)
        }
    }

    public void permition_ck() {//권한허용 (메시지, 전화, 위치)

        if (checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WAKE_LOCK, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    public void Medical_or_Patient_Check() { //의료진 인지 아닌지 검사 // 이부분은 의료진 회원가입하고 서버로 부터 오는 값에 의해 플래그 수정됨
        //2은 의료진, 1은 환자, 0은 일반인
        String str = app_lib_access_output("Medical_Staff", "flag");
        if (str.equals("2")) { //의료진 페이지(차트)

            ServiceStart("SafeLiftPropertyService");

        } else if (str.equals("1")) { //환자 페이지

            ServiceEnd("SafeLiftPropertyService");//sms 문자 감지 종료(해제)
            startActivity(new Intent(getApplication(), PatientMainActivity.class));
            MainActivity.this.finish();

        } else if (str.equals("0")) { //일반인 페이지

            ServiceEnd("SafeLiftPropertyService");//sms 문자 감지 종료(해제)

        }
    }

    public void airplane_ck() { //비행기 모드 체크
        int result;
        try {
            if (android.os.Build.VERSION.SDK_INT > 16) {
                result = Settings.Global.getInt(
                        getContentResolver(), Settings.Global.AIRPLANE_MODE_ON);
            } else {
                result = Settings.System.getInt(
                        getContentResolver(), Settings.Global.AIRPLANE_MODE_ON);
            }
            if (result != 0) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage("현재 네트워크 사용이 불가능합니다.\n 비행기모드를 해제해주세요.")
                        .setCancelable(false)
                        .setPositiveButton("비행기 모드해제하기",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                                        startActivity(intent);
                                    }
                                });
                android.app.AlertDialog alert = builder.create();
                alert.show();

            }
        } catch (Exception e) {
        }


    }

    public String app_lib_access_output(String shared_preferences_name, String output) { //공통 라이브러리 심플 함수
        SharedPreferences sf = getSharedPreferences(shared_preferences_name, 0);
        String str = sf.getString(output, "");
        return str;
    }

    public void app_lib_access_input(String shared_preferences_name, String arr, String input) {//공통 라이브러리 심플 함수
        SharedPreferences sf = getSharedPreferences(shared_preferences_name, 0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(arr, input);
        editor.commit();
    }

    public void popup_show() {
        String str = app_lib_access_output("popup_stats", "flag");
        if (str.equals("")) {
            final ScrollView linear = (ScrollView) View.inflate(MainActivity.this, R.layout.activity_main_sc, null);
            TextView tv = (TextView)linear.findViewById(R.id.txview_sc);
            tv.setText("-Argues With Officials : 애매한 판정일 경우 심판과 언쟁을 함\n\n -Avoids Using Weaker Foot : 약한발은 잘 사용하지 않음\n\n -Cautious With Crosses : 코너킥 및 크로스에 대해 인터셉트 시도 보단 골 라인을 지키려 한다\n\n -Comes For Crosses : 코너킥 및 크로스에 대해 적극적으로 인터셉트를 시도한다\n\n -Corner Specialist : 코너킥 시 좀더 많은 골 찬스를 발생시킴 \n\n -Counter Attacker : 역습상황으로 전환할때의 반응속도가 향상됨\n\n -Diver : 태클을 당하면 넘어지면서 반칙을 유도함\n\n -Dives Into Tackles : 슬라이딩 태클을 자주 시도함\n\n -Early Crosser : 얼리크로스 능력치 뛰어남\n\n -Fancy Feet : 보다 정교하고 화려한 퍼스트 터치 발동\n\n -Finesse Header : 정확한 헤딩을 시도함\n\n -Finesse Shot : 정확한 슛팅을 시도함\n\n -Flair : 공을 받거나 받은 후에 일정한 공간과 시간이 있다면 자발적으로 트릭을 사용함\n\n -Forward pushes wide left : 공격시 왼쪽을 선호하는 움직임\n\n -Forward pushes wide Right : 공격시 오른쪽을 선호하는 움직임\n\n -Giant Thorw in : 드로잉을 아주 멀리 던짐 \n\n -GK Long Throw : 골킥을 멀리 찰 수 있음\n\n -GK One On One : 1:1상황에서의 방어능력이 뛰어남\n\n -GK Puncher : 펀칭의 능력이 뛰어나고 자주시도함\n\n -GK up for Corners : 키퍼가 경기종료시간이 얼마 안남았을때 코너킥 상황에 공격하러 올라감\n\n -Heel Passer : 힐패스능력이 뛰어남\n\n -High Determination : 지고있는 상황에서 일관성 상향\n\n -Holds Up : 공격상황에서 몸싸움 경합시 밸런스 상향...");

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("처음 사용자 설명서");
            builder.setView(linear);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    app_lib_access_input("popup_stats", "flag", "1"); //1로 상태변경
                }
            });
            builder.show();


        }

    }


}
