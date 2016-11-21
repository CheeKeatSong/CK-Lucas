package com.point2points.kdusurveysystem.datamodel;

import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Student implements Serializable{

    public String studentName; // eg. John Smith
    public String studentID; // eg. 0101234
    public String studentEmail; // eg. 01012345
    public String studentUsername;
    public String studentPassword;
    public String studentCategory; // eg. Diploma
    public String studentProgramme;
    //public String studentDepartment; // eg. Department of Computing
    public String studentSchool; // eg. School of Computing and Creative Media
    public String studentSchoolShort;
    public String studentPoint;
    public String studentUid;
    public String date;

    private DatabaseReference ref;

    public Student(){}

    //public Student(String studentName, String studentID, String studentCategory, String studentDepartment, String studentSchool, String studentUid, long date){
    public Student(String studentName, String studentID, String studentEmail, String studentUsername, String studentPassword, String studentCategory,
                   String studentSchool, String studentSchoolShort, String studentPoint, String studentUid, long date, String studentProgramme) {
        this.studentName = studentName;
        this.studentID = studentID;
        this.studentEmail = studentEmail;
        this.studentUsername = studentUsername;
        this.studentPassword = studentPassword;
        this.studentCategory = studentCategory;
        this.studentSchool = studentSchool;
        this.studentSchoolShort = studentSchoolShort;
        this.studentPoint = studentPoint;
        this.studentUid = studentUid;
        this.date = String.valueOf(date);
        this.studentProgramme = studentProgramme;
    }

    public String getStudentProgramme() {
        return studentProgramme;
    }

    public void setStudentProgramme(String studentProgramme) {
        this.studentProgramme = studentProgramme;
    }

    public String getStudentUid() {
        return studentUid;
    }

    public void setStudentUid(String studentUid) {
        this.studentUid = studentUid;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getStudentCategory() {
        return studentCategory;
    }

    public void setStudentCategory(String studentCategory) {
        this.studentCategory = studentCategory;
    }


    public String getStudentSchool() {
        return studentSchool;
    }

    public void setStudentSchool(String studentSchool) {
        this.studentSchool = studentSchool;
    }

    public String getStudentSchoolShort() {
        return studentSchoolShort;
    }

    public void setStudentSchoolShort(String studentSchoolShort) {
        this.studentSchoolShort = studentSchoolShort;
    }

    public String getStudentPoint() {
        return studentPoint;
    }

    public void setStudentPoint(String studentPoint) {
        this.studentPoint = studentPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //public void createStudent(final String studentName, final String studentID, final String studentCategory, final String studentDepartment, final String studentSchool) {
    //public void createStudent(final String studentName, final String studentID, final String studentEmail, final String studentPassword, final String studentCategory, final String studentSchool) {
    public void createStudent(final String studentName, final String studentID, final String studentPassword, final String studentCategory, final String studentSchool,
                              final String studentSchoolShort, final String UID, final String studentProgramme) {

        ref = FirebaseDatabase.getInstance().getReference();

        String studentEmail = studentID + "@kdu-online.com";
        String studentUsername = studentID;

        Student student = new Student(studentName, studentID, studentEmail, studentUsername, studentPassword, studentCategory,
                studentSchool, studentSchoolShort, "100", UID, System.currentTimeMillis(), studentProgramme);

        ref.child("users").child("student").child(UID).setValue(student);
    }
}

