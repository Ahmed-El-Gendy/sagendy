package com.example.sagendy;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class settingpage extends AppCompatActivity {

    private Button logoutButton;
    private Switch darkmodeSwitch;
    boolean nightMode;
    SharedPreferences nightModeShared;
    SharedPreferences.Editor darkModeEditor;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingpage);
        logoutButton = findViewById(R.id.logoutButton);
        darkmodeSwitch = findViewById(R.id.darkmodeswitch);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        nightModeShared = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = nightModeShared.getBoolean("night", false);
        if (nightMode)
        {
            darkmodeSwitch.setChecked(true);
        }
        darkmodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the switch state change
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkModeEditor = nightModeShared.edit();
                    darkModeEditor.putBoolean("night", false);
                    showSnackbar("Dark Mode");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkModeEditor = nightModeShared.edit();
                    darkModeEditor.putBoolean("night", true);
                    showSnackbar("Light Mode");
                }
                darkModeEditor.apply();
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(settingpage.this, LogIn.class));
            }
        });
    }

    void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}