package com.safetylifeproperty.slpro.slp;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PatientMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
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

        TextView p_tx_1 = (TextView) findViewById(R.id.p_tx_1);
        TextView p_tx_2 = (TextView) findViewById(R.id.p_tx_2);
        TextView p_tx_3 = (TextView) findViewById(R.id.p_tx_3);
        TextView p_tx_4 = (TextView) findViewById(R.id.p_tx_4);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/tremble_ultra.ttf");
        p_tx_1.setTypeface(typeFace);
        p_tx_2.setTypeface(typeFace);
        p_tx_3.setTypeface(typeFace);
        p_tx_4.setTypeface(typeFace);

    }
}
