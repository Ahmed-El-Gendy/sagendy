package com.example.sagendy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagendy.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    EditText email, password;
    TextView loginText;
    Button logInButton;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        loginText = findViewById(R.id.logintext);
        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        logInButton = findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();

        Toast.makeText(getApplicationContext(),"yes",Toast.LENGTH_LONG).show();
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInUser();
            }
        });

    }

    void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void logInUser()
    {
        String Useremail = email.getText().toString();
        String Userpassword = password.getText().toString();
        if (TextUtils.isEmpty(Useremail))
        {
            showSnackbar("Email Can't be Empty");
        }
        else if (TextUtils.isEmpty(Userpassword))
        {
            showSnackbar("Password Can't be Empty");
        }
        else
        {
            mAuth.signInWithEmailAndPassword(Useremail,Userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        showSnackbar("Log In Successfully");
                        startActivity(new Intent(LogIn.this, MainActivity.class));
                    }
                    else
                    {
                        showSnackbar("Check Email and Password and try again");
                    }
                }
            });
        }




    }
}