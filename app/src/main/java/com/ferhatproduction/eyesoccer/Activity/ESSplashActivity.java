package com.ferhatproduction.eyesoccer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ferhatproduction.eyesoccer.R;


public class ESSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //

        /*** Read extras from Notification (Background) ***/
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            try{
                String message = getIntent().getExtras().get("data").toString();
                Log.d("log", "---> message : "+message);
            } catch (Exception e){
                Log.d("log", "---> message : "+e.getMessage());
            }

        }



        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(ESSplashActivity.this, ESHome.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }
}
