package com.example.sysc_4907;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;


public class signInScreen extends Fragment {

    private static final String Thingspeak_URL = "https://api.thingspeak.com/update.json?api_key=2NWCQNQ8YK14USZ0";
    private static final String Thingspeak_GET = "https://api.thingspeak.com/channels/1250054/fields/1.json?api_key=9LUVISPMRDQXBZAQ&results=1";
    EditText email;
    WebView DEE;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View myView = inflater.inflate(R.layout.sign_in_screen, container, false);
        email = (EditText) myView.findViewById(R.id.username);
        DEE = (WebView) myView.findViewById(R.id.displayEnteredEmail);
        DEE.setWebViewClient(new WebViewClient());

        return myView;
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.Testing);

        // The following is used to get the most recent information from a thingspeak channel.
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            view.findViewById(R.id.testingButton).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {

                    //Get email entered in EditText, convert to string.
                    String fullURL = "https://api.thingspeak.com/update.json?api_key=WZHU6SA35W68WB9K&field1=1";

                    DEE.loadUrl(fullURL);
                    WebSettings webSettings = DEE.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    /*
                    try {
                        try (java.util.Scanner s = new java.util.Scanner(new URL("https://api.thingspeak.com/channels/1250054/fields/1.json?api_key=9LUVISPMRDQXBZAQ&results=1").openStream())) {
                            String result = s.useDelimiter("\\A").next();
                            Log.d("String value", result);
                            Scanner scanner = new Scanner(result);
                            scanner.useDelimiter("field1");
                            String parsedResult = "";
                            while(scanner.hasNext())
                            {
                                 parsedResult = scanner.next();
                            }
                            //Now we need to remove the junk at the beginning and end of the field.
                            //There are 3 characters in the beginning to remove and 4 at the back.
                            parsedResult = parsedResult.substring(3, parsedResult.length()-4);
                            Log.d("Parsed data", parsedResult);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                     */
                }
            });
        }
        view.findViewById(R.id.signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(signInScreen.this)
                        .navigate(R.id.action_signInScreen_to_homeScreen);
            }
        });
        view.findViewById(R.id.registerEmail).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //Get email entered in EditText, convert to string.
                String em = email.getText().toString();
                String fullURL = Thingspeak_URL.concat("&field1=").concat(em);

                DEE.loadUrl(fullURL);
                WebSettings webSettings = DEE.getSettings();
                webSettings.setJavaScriptEnabled(true);

            }
        });


    }




}
