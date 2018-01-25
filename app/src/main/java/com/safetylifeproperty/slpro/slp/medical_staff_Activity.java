package com.safetylifeproperty.slpro.slp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class medical_staff_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_staff_);
        TextView SP_TEXT = (TextView) findViewById(R.id.medical_textview);
        Button btn_s = (Button)findViewById(R.id.button5);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/tremble_ultra.ttf");
        SP_TEXT.setTypeface(typeFace);

        btn_s.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medical_staff_Activity.this, medical_staff_main_Activity.class);
                startActivity(intent);
            }
        });

    }
}
