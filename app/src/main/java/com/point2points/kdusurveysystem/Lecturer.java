package com.point2points.kdusurveysystem;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class Lecturer {

    private String emailAddress;
    private String password;
    private String fullName;
    private String username;
    private int point;
    private FirebaseAuth mAuth;

    Firebase ref = new Firebase("https://kdu-survey-system.firebaseio.com/");

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

        AuthData authData = ref.getAuth();
        if (authData != null) {
            Firebase lecturerRef = ref.child("users/lecturer/" + ref.getAuth().getUid());
            Lecturer mLecturer = new Lecturer(emailEntry, passwordEntry, fullNameEntry, usernameEntry, 0);
            lecturerRef.setValue(mLecturer);
        } else {
            // no user authenticated
        }

    }

}