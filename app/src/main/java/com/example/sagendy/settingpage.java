package com.example.sagendy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static java.security.AccessController.getContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class settingpage extends AppCompatActivity {

    private Button logoutButton;
    private Switch darkmodeSwitch;
    boolean nightMode;
    private ImageView personView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView firstName, lastName, age, subDate;
    private EditText firstnameEdit, lastnameEdit, ageEdit;
    SharedPreferences personSave, firstnameSave, lastnameSave, ageSave;
    SharedPreferences.Editor personEditor, firstnameEditor, lastnameEditor, ageEditor;


    SharedPreferences nightModeShared;
    SharedPreferences.Editor darkModeEditor;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingpage);
        logoutButton = findViewById(R.id.logoutButton);
        darkmodeSwitch = findViewById(R.id.darkmodeswitch);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();


        // account data
        personView = findViewById(R.id.personpic);
        personSave = getPreferences(MODE_PRIVATE);
        // Load the previously selected image path
        String imagePath = personSave.getString("imagePath", null);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            personView.setImageBitmap(bitmap);
        }
        personView.setOnClickListener(v -> openGallery());
        firstName = findViewById(R.id.firstnametext);
        lastName = findViewById(R.id.lastnametext);
        age = findViewById(R.id.agetext);
        subDate = findViewById(R.id.subdatetext);
        firstnameEdit = findViewById(R.id.firstnameedit);
        lastnameEdit = findViewById(R.id.lastnameedit);
        ageEdit = findViewById(R.id.ageedit);
        firstnameSave = getPreferences(MODE_PRIVATE);
        lastnameSave = getPreferences(MODE_PRIVATE);
        ageSave = getPreferences(MODE_PRIVATE);
        firstnameEditor = firstnameSave.edit();
        lastnameEditor = lastnameSave.edit();
        ageEditor = ageSave.edit();

        if (firstnameSave != null) {
            String firstnameValue = firstnameSave.getString("firstname", null);
            firstName.setText(firstnameValue);
        }
        if (lastnameSave != null) {
            String lastnameValue = lastnameSave.getString("lastname", null);
            lastName.setText(lastnameValue);
        }
        if (ageSave != null) {
            String ageValue = ageSave.getString("age", null);
            age.setText("Age: " + ageValue);
        }

        // Read subscribe date from the database
        DatabaseReference readdataRef = database.getReference("date");
        readdataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                subDate.setText("Subscribe date: " + value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read sub data value.", error.toException());
            }
        });

        firstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide TextView
                firstName.setVisibility(View.INVISIBLE);
                // Show EditText
                firstnameEdit.setVisibility(View.VISIBLE);
                // Set EditText text to TextView text
                firstnameEdit.setText(firstName.getText());
                // Set focus to EditText and open keyboard
                firstnameEdit.requestFocus();
            }
        });

        firstnameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide EditText
                    firstnameEdit.setVisibility(View.INVISIBLE);
                    // Show TextView
                    firstName.setVisibility(View.VISIBLE);
                    // Update TextView text with EditText text
                    firstName.setText(firstnameEdit.getText());
                    firstnameEditor.putString("firstname", firstName.getText().toString());
                    firstnameEditor.apply();
                }
            }
        });

        lastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide TextView
                lastName.setVisibility(View.INVISIBLE);
                // Show EditText
                lastnameEdit.setVisibility(View.VISIBLE);
                // Set EditText text to TextView text
                lastnameEdit.setText(lastName.getText());
                // Set focus to EditText and open keyboard
                lastnameEdit.requestFocus();
            }
        });

        lastnameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide EditText
                    lastnameEdit.setVisibility(View.INVISIBLE);
                    // Show TextView
                    lastName.setVisibility(View.VISIBLE);
                    // Update TextView text with EditText text
                    lastName.setText(lastnameEdit.getText());
                    lastnameEditor.putString("lastname", lastName.getText().toString());
                    lastnameEditor.apply();
                }
            }
        });

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide TextView
                age.setVisibility(View.INVISIBLE);
                // Show EditText
                ageEdit.setVisibility(View.VISIBLE);
                // Set EditText text to TextView text
                if (ageSave != null) {
                    String ageValue = ageSave.getString("age", null);
                    ageEdit.setText(ageValue);
                }
                else
                {
                    ageEdit.setText("");
                }

                // Set focus to EditText and open keyboard
                ageEdit.requestFocus();
            }
        });

        ageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide EditText
                    ageEdit.setVisibility(View.INVISIBLE);
                    // Show TextView
                    age.setVisibility(View.VISIBLE);
                    // Update TextView text with EditText text
                    ageEditor.putString("age", ageEdit.getText().toString());
                    ageEditor.apply();
                    age.setText("Age: " + ageEdit.getText());
                }
            }
        });



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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                // Get the selected image URI
                Uri uri = data.getData();

                // Save the image to external storage
                String imagePath = saveImageToInternalStorage(uri);

                // Save the image path to SharedPreferences
                SharedPreferences.Editor editor = personSave.edit();
                editor.putString("imagePath", imagePath);
                editor.apply();

                // Set the selected image to your ImageView
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                personView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String saveImageToInternalStorage(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        File internalStorageDir = getDir("images", Context.MODE_PRIVATE);
        File internalImageFile = new File(internalStorageDir, "selected_image.jpg");

        FileOutputStream outputStream = new FileOutputStream(internalImageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();

        return internalImageFile.getAbsolutePath();
    }

}