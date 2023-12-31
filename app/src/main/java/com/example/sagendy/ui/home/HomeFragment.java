package com.example.sagendy.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sagendy.LogIn;
import com.example.sagendy.MainActivity;
import android.Manifest;
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

import java.text.DecimalFormat;
import java.util.Locale;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    TextView people, temp, homeDistance;
    ImageView fireOk, fireError, gasOk, gasError, safeOk, safeError;
    FirebaseAuth mAuth;
    private LocationManager locationManager;
    private Location currentLocation;
    SharedPreferences latitudeSave, longitudeSave, gpsSave;
    float lat, lon;
    boolean gpsbool;
    String athome, farhome, km;
    SharedPreferences introSave;
    SharedPreferences.Editor introEditor;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),R.raw.saged);
        mAuth = FirebaseAuth.getInstance();
        fireOk = root.findViewById(R.id.fireok);
        fireError = root.findViewById(R.id.fireerrrpic);
        gasOk = root.findViewById(R.id.gasok);
        gasError = root.findViewById(R.id.gaserrrpic);
        safeOk = root.findViewById(R.id.safeok);
        safeError = root.findViewById(R.id.safeerrrpic);
        people = root.findViewById(R.id.peopleText);
        temp = root.findViewById(R.id.temptext);
        homeDistance = root.findViewById(R.id.homechecktext);
        latitudeSave = requireContext().getSharedPreferences("latitude", Context.MODE_PRIVATE);
        longitudeSave = requireContext().getSharedPreferences("longitude", Context.MODE_PRIVATE);
        lat = latitudeSave.getFloat("latitude", 31.258601F);
        lon = longitudeSave.getFloat("longitude", 32.261924F);
        gpsSave = requireContext().getSharedPreferences("gps", Context.MODE_PRIVATE);
        gpsbool = gpsSave.getBoolean("gps", false);
        athome = getResources().getString(R.string.youhome);
        farhome = getResources().getString(R.string.youaway);
        km = getResources().getString(R.string.km);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("test");
        introSave = getContext().getSharedPreferences("intro", Context.MODE_PRIVATE);
        introEditor = introSave.edit();
        //myRef.setValue(t);
        //mediaPlayer.start();
        //mediaPlayer.stop();

        // Read Temperature from the database
        DatabaseReference readtempRef = database.getReference("temp");
        readtempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                temp.setText(getResources().getString(R.string.temp)+ value + getResources().getString(R.string.C));
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read temp value.", error.toException());
            }
        });

        // Read people numper from the database
        DatabaseReference readpeopleRef = database.getReference("people");
        readpeopleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                people.setText(getResources().getString(R.string.numofpeople) + " " + value);
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

        // GPS
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Location was changed
                if (gpsbool)
                {
                    homeDistance.setVisibility(View.VISIBLE);
                    currentLocation = location;
                    double userLat = location.getLatitude();
                    double userLong = location.getLongitude();
                    Location locationA = new Location("Point A");
                    locationA.setLatitude(lat); // Replace with the actual latitude of Point A
                    locationA.setLongitude(lon); // Replace with the actual longitude of Point A
                    Location locationB = new Location("Point B");
                    locationB.setLatitude(userLat); // Replace with the actual latitude of Point B
                    locationB.setLongitude(userLong); // Replace with the actual longitude of Point B
                    float distance = getDistance(locationA, locationB);
                    if (distance < 30)
                    {
                        homeDistance.setText(athome);
                    }
                    else
                    {
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        String formattedNumber = decimalFormat.format(distance/1000);
                        homeDistance.setText(farhome + " " + formattedNumber + " " + km);
                    }
                }
                else {
                    homeDistance.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private float getDistance(Location locationA, Location locationB) {
        return locationA.distanceTo(locationB);
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

    @Override
    public void onResume() {
        super.onResume();
        // Check and request location updates when the activity is resumed
        requestLocationUpdates();
    }


}