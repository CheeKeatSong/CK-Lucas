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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class Lecturer{

    public String lecturer_ID;
    public String emailAddress;
    public String password;
    public String fullName;
    public String username;
    public String point;

    private DatabaseReference ref;

    public Lecturer() {
    }

    public Lecturer(String lecturer_ID, String email, String password, String fullName, String username, double point) {
        this.lecturer_ID = lecturer_ID;
        this.fullName = fullName;
        this.username = username;
        this.emailAddress = email;
        this.password = password;
        this.point = Double.toString(point);
    }

    public String getLecturer_ID() {
        return lecturer_ID;
    }

    public void setLecuter_ID(String lecturer_ID) {
        this.lecturer_ID = lecturer_ID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public double getPoint() {
        return Double.parseDouble(point);
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void createLecturer(final String emailEntry, final String passwordEntry,final String fullNameEntry, final String usernameEntry,final  String UID) {

        StringTokenizer tokens = new StringTokenizer(emailEntry, "@");
        String lecturer_id = tokens.nextToken();

        ref = FirebaseDatabase.getInstance().getReference();

        Lecturer lecturer = new Lecturer(lecturer_id, emailEntry, passwordEntry, fullNameEntry, usernameEntry, 0.00);
        ref.child("users").child("lecturer").child(UID).setValue(lecturer);

    }

}