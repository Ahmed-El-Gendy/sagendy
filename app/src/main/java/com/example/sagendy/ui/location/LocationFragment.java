package com.example.sagendy.ui.location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sagendy.R;
import com.example.sagendy.databinding.FragmentSlideshowBinding;
import com.google.android.material.snackbar.Snackbar;


public class LocationFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    ToggleButton gpsButton;
    EditText latitudeEdit, longitudeEdit;
    Button locationSaveButton;
    TextView locationInfoText;
    SharedPreferences latitudeSave, longitudeSave, gpsSave;
    SharedPreferences.Editor latitudeEditor, longitudeEditor, gpsEditor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        gpsButton = root.findViewById(R.id.locationbutton);
        latitudeEdit = root.findViewById(R.id.latitudeedit);
        longitudeEdit = root.findViewById(R.id.longitudeedit);
        locationSaveButton = root.findViewById(R.id.savelocationButton);
        locationInfoText = root.findViewById(R.id.locationinfo);
        Resources resources = getResources();
        // Get the string using its resource ID
        String infostr = resources.getString(R.string.locationinfo);
        SpannableString content = new SpannableString(infostr);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        locationInfoText.setText(content);
        locationInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder locationInfoDialog = new AlertDialog.Builder(requireContext());
                locationInfoDialog.setTitle("How to use");
                locationInfoDialog.setCancelable(true);
                // Inflate the custom layout
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_location_info, null);

                // Get references to the views in the custom layout
                ImageView dialogImage = dialogView.findViewById(R.id.mapusepic);
                TextView dialogText = dialogView.findViewById(R.id.locationtext);

                // Set the image and text in the custom layout
                dialogImage.setImageResource(R.drawable.maps); // Replace with your image resource
                dialogText.setText("Take those 2 numbers and put them in the boxes");

                // Set the custom layout as the view for the dialog
                locationInfoDialog.setView(dialogView);

                locationInfoDialog.setCancelable(true);
                locationInfoDialog.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog infoDialog = locationInfoDialog.create();
                infoDialog.show();
            }
        });
        latitudeSave = requireContext().getSharedPreferences("latitude", Context.MODE_PRIVATE);
        longitudeSave = requireContext().getSharedPreferences("longitude", Context.MODE_PRIVATE);
        gpsSave = requireContext().getSharedPreferences("gps", Context.MODE_PRIVATE);
        boolean gpsbool = gpsSave.getBoolean("gps", false);
        if (gpsbool)
        {
            gpsButton.setChecked(true);
        }
        else
        {
            gpsButton.setChecked(false);
        }
        latitudeEditor = latitudeSave.edit();
        longitudeEditor = longitudeSave.edit();
        gpsEditor = gpsSave.edit();

        gpsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    showSnackbar(getResources().getString(R.string.gpson));
                    gpsEditor.putBoolean("gps", true);
                } else {
                    // Toggle is OFF
                    showSnackbar(getResources().getString(R.string.gpsoff));
                    gpsEditor.putBoolean("gps", false);
                }
                gpsEditor.apply();
            }
        });

        locationSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude = latitudeEdit.getText().toString();
                String longitude = longitudeEdit.getText().toString();
                if (TextUtils.isEmpty(latitude))
                {
                    showSnackbar(getResources().getString(R.string.latitudeempty));
                }
                else if (TextUtils.isEmpty(longitude))
                {
                    showSnackbar(getResources().getString(R.string.longitudeempty));
                }
                else
                {
                    float lat = Float.parseFloat(latitude);
                    float lon = Float.parseFloat(longitude);
                    latitudeEditor.putFloat("latitude", lat);
                    longitudeEditor.putFloat("longitude", lon);
                    latitudeEditor.apply();
                    longitudeEditor.apply();
                }
            }
        });



        return root;
    }

    void showSnackbar(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}