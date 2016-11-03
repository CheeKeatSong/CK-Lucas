package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Subject {

    public String subjectName; // eg. Fundamentals of Programming
    public String subjectCategory; // eg. Diploma
    //public String subjectDepartment; // eg. Department of Computing
    public String subjectCode;
    public String subjectSchool; // eg. School of Computing and Creative Media
    public String subjectSchoolShort;
    public String subjectUid;
    public String date;

    private DatabaseReference ref;

    public Subject(){}

    //public Subject(String subjectName, String subjectCategory, String subjectDepartment, String subjectSchool, String subjectUid, long date){
    public Subject(String subjectName, String subjectCategory, String subjectSchool, String subjectUid, long date, String subjectSchoolShort, String subjectCode){
        this.subjectName = subjectName;
        this.subjectCategory = subjectCategory;
        //this.subjectDepartment = subjectDepartment;
        this.subjectSchool = subjectSchool;
        this.subjectUid = subjectUid;
        this.date = String.valueOf(date);
        this.subjectCode = subjectCode;
        this.subjectSchoolShort = subjectSchoolShort;
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

    public String getSubjectSchoolShort() {
        return subjectSchoolShort;
    }

    public void setSubjectSchoolShort(String subjectSchoolShort) {
        this.subjectSchoolShort = subjectSchoolShort;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
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
    public void createSubject(final String subjectName, final String subjectCategory, final String subjectSchool, final String subjectSchoolShort, final String subjectCode) {

        String subjectNameReformatted = subjectName.toLowerCase();
        StringBuffer res = new StringBuffer();

        String[] strArr = subjectNameReformatted.split(" ");
        for (String str : strArr) {
            if (str.equals("&")){
                str = new String("and");
            }
            if (str.equals("of") || str.equals("and")){
                res.append(str).append(" ");
            }
            else{
                char[] stringArray = str.trim().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);
                res.append(str).append(" ");
            }
        }
        subjectNameReformatted = res.toString().trim();

        String subjectCodeReformatted = subjectCode.toUpperCase();

        ref = FirebaseDatabase.getInstance().getReference().child("subject");
        DatabaseReference keyref = ref.push();

        //Subject subject = new Subject(subjectName, subjectCategory, subjectDepartment, subjectSchool, keyref.getKey(), System.currentTimeMillis());
        Subject subject = new Subject(subjectNameReformatted, subjectCategory, subjectSchool, keyref.getKey(), System.currentTimeMillis(), subjectSchoolShort, subjectCodeReformatted);
        keyref.setValue(subject);
    }
}
