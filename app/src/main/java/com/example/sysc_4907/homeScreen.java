package com.example.sysc_4907;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;


public class homeScreen extends Fragment {

    public static Editable name;
    EditText user;
    EditText pass;
    WebView web;
    private static final String Thingspeak_URL = "https://api.thingspeak.com/update.json?api_key=GKVWP488L3SWEB7M";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View myView = inflater.inflate(R.layout.home_screen, container, false);
        user = (EditText) myView.findViewById(R.id.username);
        pass = (EditText) myView.findViewById(R.id.password);
        web = (WebView) myView.findViewById(R.id.signingIn);
        web.setWebViewClient(new WebViewClient());

        return myView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            view.findViewById(R.id.loginbtn).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    //Need to verify that the user has registered their account
                    String u = user.getText().toString();
                    String p = pass.getText().toString();
                    String fullURL = Thingspeak_URL.concat("&field1=").concat(u).concat("&field2=").concat(p);

                    //Update thingspeak channel with the given information
                    web.loadUrl(fullURL);
                    WebSettings webSettings = web.getSettings();
                    webSettings.setJavaScriptEnabled(true);

                    //Now we need to check if the user and pass were valid
                    // The following is used to get the most recent information from a thingspeak channel.
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    try (java.util.Scanner s = new java.util.Scanner(new URL("https://api.thingspeak.com/channels/1655931/fields/1.json?api_key=H7C4A5GLGL0IEH5N&results=1").openStream())) {
                                        String result = s.useDelimiter("\\A").next();
                                        Log.d("String value", result);
                                        Scanner scanner = new Scanner(result);
                                        scanner.useDelimiter("field1");
                                        String parsedResult = "";
                                        while (scanner.hasNext()) {
                                            parsedResult = scanner.next();
                                        }
                                        //Now we need to remove the junk at the beginning and end of the field.
                                        //There are 3 characters in the beginning to remove and 4 at the back.
                                        parsedResult = parsedResult.substring(3, parsedResult.length() - 4);
                                        Log.d("Parsed data", parsedResult);
                                        if (Integer.valueOf(parsedResult) == 2) {
                                            NavHostFragment.findNavController(homeScreen.this)
                                                    .navigate(R.id.action_homeScreen_to_biometricVerification);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 5000);

                    Log.d("Error", "The user was not signed in.");
                }
                }
            });

            view.findViewById(R.id.registerbtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(homeScreen.this)
                            .navigate(R.id.action_homeScreen_to_registerScreen);
                }
            });

    }


}
