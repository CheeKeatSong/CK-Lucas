package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SurveyStudent {

    private String SubjectiveRespond;
    private String SurveyStudentUID;
    private String SurveyStudentName;
    private boolean SurveyStudentStatus;
    private String SurveyStudentID;

    private DatabaseReference ref;

    public SurveyStudent(){}

    public SurveyStudent(String surveyStudentID, String surveyStudentName, boolean surveyStudentStatus, String subjectiveRespond, String surveyStudentUID) {
        SurveyStudentName = surveyStudentName;
        SurveyStudentStatus = surveyStudentStatus;
        SurveyStudentUID = surveyStudentUID;
        SubjectiveRespond = subjectiveRespond;
        SurveyStudentID = surveyStudentID;
    }

    public String getSubjectiveRespond() {
        return SubjectiveRespond;
    }

    public void setSubjectiveRespond(String subjectiveRespond) {
        SubjectiveRespond = subjectiveRespond;
    }

    public String getSurveyStudentID() {
        return SurveyStudentID;
    }

    public void setSurveyStudentID(String surveyStudentID) {
        SurveyStudentID = surveyStudentID;
    }

    public boolean isSurveyStudentStatus() {
        return SurveyStudentStatus;
    }

    public void setSurveyStudentStatus(boolean surveyStudentStatus) {
        SurveyStudentStatus = surveyStudentStatus;
    }

    public String getSurveyStudentName() {
        return SurveyStudentName;
    }

    public void setSurveyStudentName(String surveyStudentName) {
        SurveyStudentName = surveyStudentName;
    }

    public String getSurveyStudentUID() {
        return SurveyStudentUID;
    }

    public void setSurveyStudentUID(String surveyStudentUID) {
        SurveyStudentUID = surveyStudentUID;
    }

    public void createSurveyStudent(final String ID, final String Name, final  String studentUID, final String surveyUID) {

        ref = FirebaseDatabase.getInstance().getReference();

        SurveyStudent surveyStudent = new SurveyStudent(ID, Name, false, null, studentUID);
        ref.child("survey").child(surveyUID).child("student").child(studentUID).setValue(surveyStudent);

    }
}
