package com.point2points.kdusurveysystem;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.point2points.kdusurveysystem.network.CheckNetwork;

public class LaunchingScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 500;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_launching_screen);

        if(CheckNetwork.isInternetAvailable(LaunchingScreen.this)) //returns true if internet available
        {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                                startActivity(new Intent(LaunchingScreen.this, Login.class));
                                finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}

