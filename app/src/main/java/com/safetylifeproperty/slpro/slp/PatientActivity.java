package com.safetylifeproperty.slpro.slp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        TextView SP_TEXT = (TextView) findViewById(R.id.pa1);
        TextView SP_TEXT_2 = (TextView) findViewById(R.id.textView);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/tremble_ultra.ttf");
        SP_TEXT.setTypeface(typeFace);
        SP_TEXT_2.setTypeface(typeFace);
        Button Find_id_pw = (Button) findViewById(R.id.Find_id_pw);
        Button Sign_Up = (Button) findViewById(R.id.Sign_Up);
        Button login_btn = (Button) findViewById(R.id.p_login_btn);
        login_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientActivity.this, PatientMainActivity.class);
                startActivity(intent);
            }
        });
        Sign_Up.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(), "Clicked2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatientActivity.this, Patient_Registration_Activity.class);
                startActivity(intent);


            }
        });
    }
}
