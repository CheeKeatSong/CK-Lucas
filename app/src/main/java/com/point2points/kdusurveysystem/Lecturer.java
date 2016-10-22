package com.point2points.kdusurveysystem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

import static android.content.ContentValues.TAG;

public class Lecturer{

    private String emailAddress;
    private String password;
    private String fullName;
    private String username;
    private int point;

    Firebase ref = new Firebase("https://kdu-survey-system.firebaseio.com/");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Lecturer() {
    }

    public Lecturer(String email, String password, String fullName, String username, int point) {
        this.fullName = fullName;
        this.username = username;
        this.emailAddress = email;
        this.password = password;
        this.point = point;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void createLecturer(String emailEntry, String passwordEntry, String fullNameEntry, String usernameEntry) {

            Firebase lecturerRef = ref.child("users/lecturer/" + user.getUid());
            Lecturer mLecturer = new Lecturer(emailEntry, passwordEntry, fullNameEntry, usernameEntry, 0);
            lecturerRef.setValue(mLecturer);
    }

}