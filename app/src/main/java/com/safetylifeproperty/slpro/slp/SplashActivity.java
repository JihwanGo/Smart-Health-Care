package com.safetylifeproperty.slpro.slp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView SP_TEXT = (TextView) findViewById(R.id.sp1);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/tremble_ultra.ttf");
        SP_TEXT.setTypeface(typeFace);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2000);

    }

    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class));
            SplashActivity.this.finish();
        }
    }
}
