package com.example.sagendy.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sagendy.LogIn;
import com.example.sagendy.MainActivity;
import com.example.sagendy.R;
import com.example.sagendy.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.Locale;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    TextView people;
    ImageView fireOk, fireError, gasOk, gasError, safeOk, safeError;
    FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),R.raw.saged);
        mAuth = FirebaseAuth.getInstance();
        fireOk = root.findViewById(R.id.fireok);
        fireError = root.findViewById(R.id.fireerrrpic);
        gasOk = root.findViewById(R.id.gasok);
        gasError = root.findViewById(R.id.gaserrrpic);
        safeOk = root.findViewById(R.id.safeok);
        safeError = root.findViewById(R.id.safeerrrpic);
        people = root.findViewById(R.id.peopleText);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test");

        //myRef.setValue(t);
        //mediaPlayer.start();
        //mediaPlayer.stop();


        // Read people numper from the database
        DatabaseReference readpeopleRef = database.getReference("people");
        readpeopleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                people.setText("Numper of people is: "+value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read people value.", error.toException());
            }
        });


        // Read fire value from the database
        DatabaseReference readfireRef = database.getReference("fire");
        readfireRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean value = dataSnapshot.getValue(boolean.class);
                if (value)
                {
                    fireOk.setVisibility(View.VISIBLE);
                    fireError.setVisibility(View.INVISIBLE);
                }
                else
                {
                    fireOk.setVisibility(View.INVISIBLE);
                    fireError.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read fire value.", error.toException());
            }
        });


        // Read fire value from the database
        DatabaseReference readgasRef = database.getReference("gas");
        readgasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                if (value<1000)
                {
                    gasOk.setVisibility(View.VISIBLE);
                    gasError.setVisibility(View.INVISIBLE);
                }
                else
                {
                    gasOk.setVisibility(View.INVISIBLE);
                    gasError.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read gas value.", error.toException());
            }
        });



        // Read safety value from the database
        DatabaseReference readsafeRef = database.getReference("safe");
        readsafeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean value = dataSnapshot.getValue(boolean.class);
                if (value)
                {
                    safeOk.setVisibility(View.VISIBLE);
                    safeError.setVisibility(View.INVISIBLE);
                }
                else
                {
                    safeOk.setVisibility(View.INVISIBLE);
                    safeError.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read safe value.", error.toException());
            }
        });



        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}