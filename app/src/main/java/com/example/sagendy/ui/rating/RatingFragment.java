package com.example.sagendy.ui.rating;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sagendy.R;
import com.example.sagendy.databinding.FragmentSlideshowBinding;
import com.example.sagendy.ui.slideshow.SlideshowViewModel;

public class RatingFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    RatingBar rateBar;
    TextView ratevalue;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rating, container, false);

        rateBar = root.findViewById(R.id.ratingBar);
        ratevalue = root.findViewById(R.id.ratevalue);
        // Set a listener to respond to rating changes
        rateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Do something with the new rating, e.g., display a Toast
                Toast.makeText(getContext(), "Rating: " + rating, Toast.LENGTH_SHORT).show();
                ratevalue.setVisibility(View.VISIBLE);
                ratevalue.setText(rating + "");
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}