
package com.example.sysc_4907;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class registerScreen extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View myView = inflater.inflate(R.layout.register_screen, container, false);

        return myView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.finishRegistering).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(registerScreen.this)
                        .navigate(R.id.action_registerScreen_to_faceScanScreen);
            }
        });

        view.findViewById(R.id.goHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(registerScreen.this)
                        .navigate(R.id.action_registerScreen_to_homeScreen);
            }
        });
    }


}