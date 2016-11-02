package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.StringTokenizer;

public class Lecturer{

    public String lecturer_ID;
    public String emailAddress;
    public String password;
    public String fullName;
    public String username;
    public String point;
    public String date;
    public String uid;
    public String schoolName;
    public String schoolNameShort;

    private DatabaseReference ref;

    public Lecturer() {
    }

    public Lecturer(String lecturer_ID, String email, String password, String fullName, String username, String point, long date, String uid, String schoolName, String schoolNameShort) {
        this.lecturer_ID = lecturer_ID;
        this.fullName = fullName;
        this.username = username;
        this.emailAddress = email;
        this.password = password;
        this.point = point;
        this.date = String.valueOf(date);
        this.uid = uid;
        this.schoolName = schoolName;
        this.schoolNameShort = schoolNameShort;
    }

    public String getSchoolNameShort() {
        return schoolNameShort;
    }

    public void setSchoolNameShort(String schoolNameShort) {
        this.schoolNameShort = schoolNameShort;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLecturer_ID() {
        return lecturer_ID;
    }

    public void setLecturer_ID(String lecturer_ID) {
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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void createLecturer(final String emailEntry, final String passwordEntry,final String fullNameEntry, final String usernameEntry,final  String UID, String schoolName, String schoolNameShort) {

        StringTokenizer tokens = new StringTokenizer(emailEntry, "@");
        String lecturer_id = tokens.nextToken();

        ref = FirebaseDatabase.getInstance().getReference();

        Lecturer lecturer = new Lecturer(lecturer_id, emailEntry, passwordEntry, fullNameEntry, usernameEntry, "100", System.currentTimeMillis(), UID, schoolName, schoolNameShort);
        ref.child("users").child("lecturer").child(UID).setValue(lecturer);

    }
}