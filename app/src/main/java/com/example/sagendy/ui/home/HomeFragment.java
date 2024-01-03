package com.example.sagendy.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sagendy.LogIn;
import com.example.sagendy.MainActivity;
import android.Manifest;
import android.widget.ToggleButton;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {


    private static final String CHANNEL_ID = "my_channel_01";
    private static final int NOTIFICATION_ID = 1;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;
    private FragmentHomeBinding binding;
    TextView people, temp, homeDistance, humidity;
    ImageView fireOk, fireError, gasOk, gasError, safeOk, safeError;
    FirebaseAuth mAuth;
    private LocationManager locationManager;
    private Location currentLocation;
    ToggleButton alarmButton;
    SharedPreferences latitudeSave, longitudeSave, gpsSave, historySave,firet,gast,safet;
    SharedPreferences.Editor historyEditor,firetEdit,gastEdit,safetEdit;
    float lat, lon;
    boolean gpsbool;
    String athome, farhome, km;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
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
        historySave = requireContext().getSharedPreferences("history", Context.MODE_PRIVATE);
        firet = requireContext().getSharedPreferences("firet", Context.MODE_PRIVATE);
        gast = requireContext().getSharedPreferences("gast", Context.MODE_PRIVATE);
        safet = requireContext().getSharedPreferences("safet", Context.MODE_PRIVATE);
        humidity = root.findViewById(R.id.humiditytext);
        alarmButton = root.findViewById(R.id.alarmbutton);
        MediaPlayer alarm = MediaPlayer.create(getContext(),R.raw.alarmf);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("test");
        //myRef.setValue(t);


        //correct.start();
        //correct.stop();

        // Read alarm from the database
        DatabaseReference readalarmRef = database.getReference("alarms");
        readalarmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Boolean value = dataSnapshot.getValue(boolean.class);
                if (value)
                {
                    alarmButton.setChecked(true);
                    alarm.start();
                }
                else
                {
                    alarmButton.setChecked(false);
                    if (alarm.isPlaying())
                    {
                        alarm.pause();
                        alarm.seekTo(0);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read alarm value.", error.toException());
            }
        });

        alarmButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference myRef = database.getReference("alarmr");
                DatabaseReference myReff = database.getReference("alarms");
                if (isChecked)
                {
                    myRef.setValue(1);
                    myReff.setValue(true);
                }
                else
                {
                    myRef.setValue(0);
                    myReff.setValue(false);
                }
            }
        });

        // Read Temperature from the database
        DatabaseReference readhumRef = database.getReference("humidity");
        readhumRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                humidity.setText(getResources().getString(R.string.humidity)+ " " + value + " " + getResources().getString(R.string.percentage));
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read humidity value.", error.toException());
            }
        });

        // Read Temperature from the database
        DatabaseReference readtempRef = database.getReference("temp");
        readtempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                temp.setText(getResources().getString(R.string.temp)+ " " + value + " " + getResources().getString(R.string.C));
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
                    firetEdit = firet.edit();
                    firetEdit.putBoolean("firet", true);
                    firetEdit.apply();
                }
                else
                {
                    fireOk.setVisibility(View.INVISIBLE);
                    fireError.setVisibility(View.VISIBLE);
                    boolean temp = firet.getBoolean("firet", true);
                    if (temp)
                    {
                        // Get the current time
                        long currentTimeMillis = System.currentTimeMillis();
                        Date currentDate = new Date(currentTimeMillis);

                        // Define the desired date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                        // Format the date as a string
                        String formattedDate = dateFormat.format(currentDate);
                        printError(getResources().getString(R.string.fireerror) + " " + formattedDate);
                    }
                    firetEdit = firet.edit();
                    firetEdit.putBoolean("firet", false);
                    firetEdit.apply();


                    createNotificationChannel();
                    // Build the notification
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "your_channel_id")
                            .setSmallIcon(R.drawable.baseline_local_fire_department_24) // Set your notification icon here
                            .setContentTitle(getResources().getString(R.string.firealert))
                            .setContentText(getResources().getString(R.string.therefire))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true); // Dismiss the notification when clicked

                    // Display the notification
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                    notificationManager.notify(1, mBuilder.build());






                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showFireAlertDialog(getContext());
                        }
                    }, 2000); // 1000 milliseconds = 1 second

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
                if (value<1500)
                {
                    gasOk.setVisibility(View.VISIBLE);
                    gasError.setVisibility(View.INVISIBLE);
                    gastEdit = gast.edit();
                    gastEdit.putBoolean("gast", true);
                    gastEdit.apply();
                }
                else
                {
                    gasOk.setVisibility(View.INVISIBLE);
                    gasError.setVisibility(View.VISIBLE);
                    boolean temp = gast.getBoolean("gast", true);
                    if (temp)
                    {
                        // Get the current time
                        long currentTimeMillis = System.currentTimeMillis();
                        Date currentDate = new Date(currentTimeMillis);

                        // Define the desired date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                        // Format the date as a string
                        String formattedDate = dateFormat.format(currentDate);
                        printError(getResources().getString(R.string.gaserror) + " " + formattedDate);
                    }
                    gastEdit = gast.edit();
                    gastEdit.putBoolean("gast", false);
                    gastEdit.apply();

                    createNotificationChannel();
                    // Build the notification
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "your_channel_id")
                            .setSmallIcon(R.drawable.gaserror) // Set your notification icon here
                            .setContentTitle(getResources().getString(R.string.gasalert))
                            .setContentText(getResources().getString(R.string.theregas))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true); // Dismiss the notification when clicked

                    // Display the notification
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                    notificationManager.notify(1, mBuilder.build());


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showGasAlertDialog(getContext());
                        }
                    }, 2000); // 1000 milliseconds = 1 second
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
                    safetEdit = safet.edit();
                    safetEdit.putBoolean("safet", true);
                    safetEdit.apply();
                }
                else
                {
                    safeOk.setVisibility(View.INVISIBLE);
                    safeError.setVisibility(View.VISIBLE);
                    boolean temp = safet.getBoolean("safet", true);
                    if (temp)
                    {
                        // Get the current time
                        long currentTimeMillis = System.currentTimeMillis();
                        Date currentDate = new Date(currentTimeMillis);

                        // Define the desired date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                        // Format the date as a string
                        String formattedDate = dateFormat.format(currentDate);
                        printError(getResources().getString(R.string.safeerror) + " " + formattedDate);
                    }
                    safetEdit = safet.edit();
                    safetEdit.putBoolean("safet", false);
                    safetEdit.apply();


                    createNotificationChannel();
                    // Build the notification
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "your_channel_id")
                            .setSmallIcon(R.drawable.download__1_) // Set your notification icon here
                            .setContentTitle(getResources().getString(R.string.safealert))
                            .setContentText(getResources().getString(R.string.safethere))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true); // Dismiss the notification when clicked

                    // Display the notification
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                    notificationManager.notify(1, mBuilder.build());


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showSafeAlertDialog(getContext());
                        }
                    }, 2000); // 1000 milliseconds = 1 second
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
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("athome");
                    if (distance < 30)
                    {
                        homeDistance.setText(athome);
                        myRef.setValue(1);
                    }
                    else
                    {
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        String formattedNumber = decimalFormat.format(distance/1000);
                        homeDistance.setText(farhome + " " + formattedNumber + " " + km);
                        myRef.setValue(0);
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

    void printError(String s)
    {
        String history = historySave.getString("history", "");
        history = s + "\n---------------\n" + history;
        historyEditor = historySave.edit();
        historyEditor.putString("history", history);
        historyEditor.apply();
    }


    private void showFireAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the dialog title and message
        builder.setTitle(getResources().getString(R.string.firealert))
                .setMessage(getResources().getString(R.string.firee) + "\n" + getResources().getString(R.string.fireee));

        // Set positive button and its click listener
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String phoneNumber = "180";
                // Check if the app has permission to make a phone call
                boolean check = true;
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // If permission is granted, start the phone call
                    initiatePhoneCall(phoneNumber);
                } else {
                    // If permission is not granted, request it
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PHONE_PERMISSION_REQUEST_CODE
                    );
                    check = false;
                }
                if (!check)
                {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        // If permission is granted, start the phone call
                        initiatePhoneCall(phoneNumber);
                    }
                }

                dialog.dismiss(); // Close the dialog
            }
        });

        // Set negative button and its click listener
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showGasAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the dialog title and message
        builder.setTitle(getResources().getString(R.string.gasalert))
                .setMessage(getResources().getString(R.string.gass) + "\n" + getResources().getString(R.string.gasss));

        // Set positive button and its click listener
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String phoneNumber = "129";
                // Check if the app has permission to make a phone call
                boolean check = true;
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // If permission is granted, start the phone call
                    initiatePhoneCall(phoneNumber);
                } else {
                    // If permission is not granted, request it
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PHONE_PERMISSION_REQUEST_CODE
                    );
                    check = false;
                }
                if (!check)
                {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        // If permission is granted, start the phone call
                        initiatePhoneCall(phoneNumber);
                    }
                }

                dialog.dismiss(); // Close the dialog
            }
        });

        // Set negative button and its click listener
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showSafeAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the dialog title and message
        builder.setTitle(getResources().getString(R.string.safealert))
                .setMessage(getResources().getString(R.string.safee) + "\n" + getResources().getString(R.string.safee));

        // Set positive button and its click listener
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String phoneNumber = "122";
                // Check if the app has permission to make a phone call
                boolean check = true;
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // If permission is granted, start the phone call
                    initiatePhoneCall(phoneNumber);
                } else {
                    // If permission is not granted, request it
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PHONE_PERMISSION_REQUEST_CODE
                    );
                    check = false;
                }
                if (!check)
                {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        // If permission is granted, start the phone call
                        initiatePhoneCall(phoneNumber);
                    }
                }

                dialog.dismiss(); // Close the dialog
            }
        });

        // Set negative button and its click listener
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void initiatePhoneCall(String phoneNumber) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(phoneIntent);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("your_channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }





}