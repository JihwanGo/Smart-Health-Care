package com.safetylifeproperty.slpro.slp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Patient_Registration_Activity extends AppCompatActivity {
    Fragment01 fragment01;
    Fragment02 fragment02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__registration_);
        //커스텀 액션바 생성
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);
        TextView title_tx = (TextView) actionbar.findViewById(R.id.custom_title_textview);
        title_tx.setText("S.L.P 회원가입");

        fragment01 = (Fragment01) getSupportFragmentManager().findFragmentById(R.id.frag01);
        fragment02 = new Fragment02();

        actionBar.setCustomView(actionbar);
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);



    }
    public void onFragmentChanged(int index){
        if(index == 0){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out,R.anim.fade_in, R.anim.fade_out).replace(R.id.parentView,fragment02).commit();
        } else if(index == 1){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out,R.anim.fade_in, R.anim.fade_out).replace(R.id.parentView,fragment01).commit();
        }
    }
}
