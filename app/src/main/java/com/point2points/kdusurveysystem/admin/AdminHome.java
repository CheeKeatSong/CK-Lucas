// Added this comment for testing. 5.41PM

package com.point2points.kdusurveysystem.admin;

import android.app.Activity;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.point2points.kdusurveysystem.R;

import static com.point2points.kdusurveysystem.admin.AdminToolbarDrawer.tabIdentifier;

public class AdminHome extends AdminToolbarDrawer{
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.loadUserProfileInfo(savedInstanceState);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.admin_home);

        tabIdentifier = 1;
    }
}
