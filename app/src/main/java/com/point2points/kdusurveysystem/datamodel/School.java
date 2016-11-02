package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class School {

    public String schoolName;
    public String schoolNameShort;
    public String schoolUid;
    public String date;

    private DatabaseReference ref;

    public School(){}

    public School(String schoolName, String schoolNameShort, String schoolUid, long date){
        this.schoolName = schoolName;
        this.schoolNameShort = schoolNameShort;
        this.schoolUid = schoolUid;
        this.date = String.valueOf(date);
    }

    public String getSchoolUid() {
        return schoolUid;
    }

    public void setSchoolUid(String schoolUid) {
        this.schoolUid = schoolUid;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void createSchool(final String schoolName) {

        StringBuilder schoolNameBuilder = new StringBuilder();
        for (String s : schoolName.split(" ")) {
            if (s != "Of" && s != "of" && s != "And" && s != "and" && s != "&") {
                schoolNameBuilder.append(s.charAt(0));
            }
        }

        String schoolNameShort = schoolNameBuilder.toString().toUpperCase();

        ref = FirebaseDatabase.getInstance().getReference().child("school");
        DatabaseReference keyref = ref.push();

        School school = new School(schoolName, schoolNameShort, keyref.getKey(), System.currentTimeMillis());
        keyref.setValue(school);
    }
}
