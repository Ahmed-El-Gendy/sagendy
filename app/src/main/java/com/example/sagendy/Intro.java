package com.example.sagendy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sagendy.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Intro extends AppCompatActivity {
    private TextView text50;
    private ImageView h;
    private Animation topanim,bottomanim;
    private static int SPLASH_SCREEN =5000;
    SharedPreferences langSave;
    SharedPreferences.Editor langEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView((R.layout.activity_intro));
        text50=findViewById(R.id.txttt);
        h=findViewById(R.id.homeee);


        // check lang
        langSave = getSharedPreferences("lang", Context.MODE_PRIVATE);
        String langValue = langSave.getString("lang", null).toString();
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (langValue != null && !"".equals(langValue) && !config.locale.getLanguage().equals(langValue))
        {
            changeLang(langValue);
            showSnackbar(langValue);
            recreate();
        }


        topanim= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomanim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        h.setAnimation(topanim);
        text50.setAnimation(bottomanim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //startActivity(new Intent(Intro.this,MainActivity.class));
            }
        },SPLASH_SCREEN);
        Thread thread=new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3000);
                    Intent russplash=new Intent(getApplicationContext(),MainActivity.class);
                    startActivities(new Intent[]{russplash});
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}};
        thread.start();

    }

    public void changeLang(String lang) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {

            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration conf = new Configuration(config);
            conf.locale = locale;
            getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


}
