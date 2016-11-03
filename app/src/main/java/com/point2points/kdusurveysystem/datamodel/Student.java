package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Student {

    public String studentName; // eg. John Smith
    public String studentID; // eg. 0101234
    public String studentCategory; // eg. Diploma
    //public String studentDepartment; // eg. Department of Computing
    public String studentSchool; // eg. School of Computing and Creative Media
    public String studentUid;
    public String date;

    private DatabaseReference ref;

    public Student(){}

    //public Student(String studentName, String studentID, String studentCategory, String studentDepartment, String studentSchool, String studentUid, long date){
    public Student(String studentName, String studentID, String studentCategory, String studentSchool, String studentUid, long date){
        this.studentName = studentName;
        this.studentID = studentID;
        this.studentCategory = studentCategory;
        //this.studentDepartment = studentDepartment;
        this.studentSchool = studentSchool;
        this.studentUid = studentUid;
        this.date = String.valueOf(date);
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

    public String getStudentCategory() {
        return studentCategory;
    }

    public void setStudentCategory(String studentCategory) {
        this.studentCategory = studentCategory;
    }

    /*public String getStudentDeparment() {
        return studentDepartment;
    }

    public void setStudentDepartment(String studentDepartment) {
        this.studentDepartment = studentDepartment;
    }*/

    public String getStudentSchool() {
        return studentSchool;
    }

    public void setStudentSchool(String studentSchool) {
        this.studentSchool = studentSchool;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //public void createStudent(final String studentName, final String studentID, final String studentCategory, final String studentDepartment, final String studentSchool) {
    public void createStudent(final String studentName, final String studentID, final String studentCategory, final String studentSchool) {

        ref = FirebaseDatabase.getInstance().getReference().child("student");
        DatabaseReference keyref = ref.push();

        //Student student = new Student(studentName, studentID, studentCategory, studentDepartment, studentSchool, keyref.getKey(), System.currentTimeMillis());
        Student student = new Student(studentName, studentID, studentCategory, studentSchool, keyref.getKey(), System.currentTimeMillis());
        keyref.setValue(student);
    }
}
