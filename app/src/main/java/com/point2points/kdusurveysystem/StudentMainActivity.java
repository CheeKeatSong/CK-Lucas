package com.point2points.kdusurveysystem;

import android.app.Activity;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.point2points.kdusurveysystem.R;

public class StudentMainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.student_main_activity_screen);
    }
}
