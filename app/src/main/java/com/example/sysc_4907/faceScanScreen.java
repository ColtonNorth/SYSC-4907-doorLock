package com.example.sysc_4907;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class faceScanScreen extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View myView = inflater.inflate(R.layout.face_scan_screen, container, false);

        return myView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //This will eventually start the face scan feature, instead of just currently going back to
        //the home page.
        view.findViewById(R.id.FaceScan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(faceScanScreen.this)
                        .navigate(R.id.action_faceScanScreen_to_homeScreen);
            }
        });

        view.findViewById(R.id.Home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(faceScanScreen.this)
                        .navigate(R.id.action_faceScanScreen_to_homeScreen);
            }
        });
    }


}