package com.example.sysc_4907;

import android.annotation.SuppressLint;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.concurrent.Executor;

public class biometricVerification extends Fragment{

    private BiometricPrompt bioPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;
    TextView fingerprint;
    int i = 0;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View myView = inflater.inflate(R.layout.biometric_verification, container, false);
        fingerprint = (TextView) myView.findViewById(R.id.authPrint);
        return myView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        executor = ContextCompat.getMainExecutor(getContext());
        bioPrompt = new BiometricPrompt(biometricVerification.this, executor, new BiometricPrompt.AuthenticationCallback()
        {
            @SuppressLint("SetTextI18n")
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                fingerprint.setText("Error occurred while authenticating");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                fingerprint.setText("Authentication Successful");
                NavHostFragment.findNavController(biometricVerification.this)
                        .navigate(R.id.action_biometricVerification_to_signInScreen);

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                fingerprint.setText("Authentication Failed");
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Login using fingerprint")
                .setNegativeButtonText("Cancel")
                .build();

        view.findViewById(R.id.authenticate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bioPrompt.authenticate(promptInfo);
            }
        });

        view.findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(biometricVerification.this)
                        .navigate(R.id.action_biometricVerification_to_homeScreen);
            }
        });

    }

}
