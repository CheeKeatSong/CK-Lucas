// Added this comment for testing. 5.41PM

package com.point2points.kdusurveysystem.admin;

import android.app.Activity;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.point2points.kdusurveysystem.R;

public class AdminHome extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.admin_home);
    }
}
