package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Subject {

    public String subjectName; // eg. Fundamentals of Programming
    public String subjectCategory; // eg. Diploma
    //public String subjectDepartment; // eg. Department of Computing
    public String subjectSchool; // eg. School of Computing and Creative Media
    public String subjectUid;
    public String date;

    private DatabaseReference ref;

    public Subject(){}

    //public Subject(String subjectName, String subjectCategory, String subjectDepartment, String subjectSchool, String subjectUid, long date){
    public Subject(String subjectName, String subjectCategory, String subjectSchool, String subjectUid, long date){
        this.subjectName = subjectName;
        this.subjectCategory = subjectCategory;
        //this.subjectDepartment = subjectDepartment;
        this.subjectSchool = subjectSchool;
        this.subjectUid = subjectUid;
        this.date = String.valueOf(date);
    }

    public String getSubjectUid() {
        return subjectUid;
    }

    public void setSubjectUid(String subjectUid) {
        this.subjectUid = subjectUid;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCategory() {
        return subjectCategory;
    }

    public void setSubjectCategory(String subjectCategory) {
        this.subjectCategory = subjectCategory;
    }

    /*public String getSubjectDepartment() {
        return subjectDepartment;
    }

    public void setSubjectDepartment(String subjectDepartment) {
        this.subjectDepartment = subjectDepartment;
    }*/

    public String getSubjectSchool() {
        return subjectSchool;
    }

    public void setSubjectSchool(String subjectSchool) {
        this.subjectSchool = subjectSchool;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //public void createSubject(final String subjectName, final String subjectCategory, final String subjectDepartment, final String subjectSchool) {
    public void createSubject(final String subjectName, final String subjectCategory, final String subjectSchool) {

        ref = FirebaseDatabase.getInstance().getReference().child("subject");
        DatabaseReference keyref = ref.push();

        //Subject subject = new Subject(subjectName, subjectCategory, subjectDepartment, subjectSchool, keyref.getKey(), System.currentTimeMillis());
        Subject subject = new Subject(subjectName, subjectCategory, subjectSchool, keyref.getKey(), System.currentTimeMillis());
        keyref.setValue(subject);
    }
}
