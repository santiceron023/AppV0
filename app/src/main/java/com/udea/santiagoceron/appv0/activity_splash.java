package com.udea.santiagoceron.appv0;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class activity_splash extends AppCompatActivity {
    private static final long SPLASH_DELAY = 2800;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //block screen rotation
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // SIN TITULO
        //FULLS CREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

//preferencias compartidas
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        //editor = prefs.edit();
        final int optlog = prefs.getInt("optlog",0); // 0 si vacio


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //load preferences
                if (optlog == 0) {
                    //ccomunicar dos actividad
                    intent = new Intent(activity_splash.this, activity_login.class);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(activity_splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        //commands
        Timer timer = new Timer();
        timer.schedule(task,SPLASH_DELAY);
    }
}
