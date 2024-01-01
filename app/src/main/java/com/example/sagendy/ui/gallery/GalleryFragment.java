package com.example.sagendy.ui.gallery;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sagendy.R;
import com.example.sagendy.databinding.FragmentGalleryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    ImageView bathLamp, kitchenLamp, livingLamp;
    TextView lampText;
    SeekBar seekBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lampText = root.findViewById(R.id.lightText);
        seekBar = root.findViewById(R.id.seekBarlamp);
        bathLamp = root.findViewById(R.id.leftrooflamp);
        kitchenLamp = root.findViewById(R.id.rightrooflamp);
        livingLamp = root.findViewById(R.id.livingroomlamp);


        seekBar.setMax(255);

        // connect to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

/*
        // Read bathroom door value from the database
        DatabaseReference readbathdoorRef = database.getReference("bathroom/door");
        readbathdoorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean value = dataSnapshot.getValue(boolean.class);
                if (value)
                {
                    bathClose.setVisibility(View.VISIBLE);
                    bathOpen.setVisibility(View.INVISIBLE);
                }
                else
                {
                    bathClose.setVisibility(View.INVISIBLE);
                    bathOpen.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read bathroom door value.", error.toException());
            }
        });

*/
/*

        // Read kitchen door value from the database
        DatabaseReference readkitchendoorRef = database.getReference("kitchen/door");
        readkitchendoorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean value = dataSnapshot.getValue(boolean.class);
                if (value)
                {
                    kitchenClose.setVisibility(View.VISIBLE);
                    kitchenOpen.setVisibility(View.INVISIBLE);
                }
                else
                {
                    kitchenClose.setVisibility(View.INVISIBLE);
                    kitchenOpen.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read kitchen door value.", error.toException());
            }
        });

*/
        /*
        // Read livingroom door value from the database
        DatabaseReference readlivingroomdoorRef = database.getReference("livingroom/door");
        readlivingroomdoorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean value = dataSnapshot.getValue(boolean.class);
                if (value)
                {
                    livingClose.setVisibility(View.VISIBLE);
                    livingOpen.setVisibility(View.INVISIBLE);
                }
                else
                {
                    livingClose.setVisibility(View.INVISIBLE);
                    livingOpen.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read living room door value.", error.toException());
            }
        });

*/
        int colorToFilterGray = Color.GRAY;
        int colorToFilterlight = Color.rgb(255, 128, 0);
        int colorToFilteryellow = Color.YELLOW;



        int[] livinglampvalue = new int[1];
        Drawable drawable = livingLamp.getDrawable();
        // Read livingroom light value from the database
        DatabaseReference readlivingroomlightRef = database.getReference("livingroom/light");
        readlivingroomlightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                livinglampvalue[0] = value;
                if (value<10)
                {
                    drawable.setColorFilter(new PorterDuffColorFilter(colorToFilterGray, PorterDuff.Mode.SRC_IN));
                    livingLamp.setImageDrawable(drawable);
                }
                else if (value<150)
                {
                    drawable.setColorFilter(new PorterDuffColorFilter(colorToFilterlight, PorterDuff.Mode.SRC_IN));
                    livingLamp.setImageDrawable(drawable);
                }
                else
                {
                    drawable.setColorFilter(new PorterDuffColorFilter(colorToFilteryellow, PorterDuff.Mode.SRC_IN));
                    livingLamp.setImageDrawable(drawable);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read living room light value.", error.toException());
            }
        });


        int[] bathlampvalue = new int[1];
        Drawable drawablebath = bathLamp.getDrawable();
        // Read bath room light value from the database
        DatabaseReference readbathroomlightRef = database.getReference("kitchen/light");
        readbathroomlightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                bathlampvalue[0] = value;
                if (value<10)
                {
                    drawablebath.setColorFilter(new PorterDuffColorFilter(colorToFilterGray, PorterDuff.Mode.SRC_IN));
                    bathLamp.setImageDrawable(drawablebath);
                }
                else if (value<150)
                {
                    drawablebath.setColorFilter(new PorterDuffColorFilter(colorToFilterlight, PorterDuff.Mode.SRC_IN));
                    bathLamp.setImageDrawable(drawablebath);
                }
                else
                {
                    drawablebath.setColorFilter(new PorterDuffColorFilter(colorToFilteryellow, PorterDuff.Mode.SRC_IN));
                    bathLamp.setImageDrawable(drawablebath);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read bath room light value.", error.toException());
            }
        });


        int[] kitchenlampvalue = new int[1];
        Drawable drawablekitchen = kitchenLamp.getDrawable();
        // Read bath room light value from the database
        DatabaseReference readkitchenlightRef = database.getReference("bathroom/light");
        readkitchenlightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.getValue(int.class);
                kitchenlampvalue[0] = value;
                if (value<10)
                {
                    drawablekitchen.setColorFilter(new PorterDuffColorFilter(colorToFilterGray, PorterDuff.Mode.SRC_IN));
                    kitchenLamp.setImageDrawable(drawablekitchen);
                }
                else if (value<150)
                {
                    drawablekitchen.setColorFilter(new PorterDuffColorFilter(colorToFilterlight, PorterDuff.Mode.SRC_IN));
                    kitchenLamp.setImageDrawable(drawablekitchen);
                }
                else
                {
                    drawablekitchen.setColorFilter(new PorterDuffColorFilter(colorToFilteryellow, PorterDuff.Mode.SRC_IN));
                    kitchenLamp.setImageDrawable(drawablekitchen);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read kitchen light value.", error.toException());
            }
        });



        int[] whichLamp = new int[1];
        bathLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lampText.setVisibility(View.VISIBLE);
                lampText.setText(R.string.leftroofshow);
                seekBar.setVisibility(View.VISIBLE);
                //seekBar.setProgress(bathlampvalue[0]);
                whichLamp[0] = 1;
            }
        });
        kitchenLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lampText.setVisibility(View.VISIBLE);
                lampText.setText(R.string.rightroofshow);
                seekBar.setVisibility(View.VISIBLE);
                //seekBar.setProgress(kitchenlampvalue[0]);
                whichLamp[0] = 2;
            }
        });
        livingLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lampText.setVisibility(View.VISIBLE);
                lampText.setText(R.string.livingroomshow);
                seekBar.setVisibility(View.VISIBLE);
                //seekBar.setProgress(livinglampvalue[0]);
                whichLamp[0] = 3;
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (whichLamp[0] == 1)
                {
                    readbathroomlightRef.setValue(progress);
                    bathlampvalue[0] = progress;
                }
                else if (whichLamp[0] == 2)
                {
                    readkitchenlightRef.setValue(progress);
                    kitchenlampvalue[0] = progress;
                }
                else
                {
                    readlivingroomlightRef.setValue(progress);
                    livinglampvalue[0] = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                if (whichLamp[0] == 1)
//                {
//                    seekBar.setProgress(bathlampvalue[0]);
//                }
//                else if (whichLamp[0] == 2)
//                {
//                    seekBar.setProgress(kitchenlampvalue[0]);
//                }
//                else
//                {
//                    seekBar.setProgress(livinglampvalue[0]);
//                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    lampText.setVisibility(View.INVISIBLE);
                    seekBar.setVisibility(View.INVISIBLE);
            }
        });





        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}