package com.point2points.kdusurveysystem.datamodel;

public class SurveyStudent {

    public String SubjectiveRespond;
    public String SurveyStudentUID;
    public String SurveyStudentName;
    public boolean SurveyStudentStatus;

    public SurveyStudent(){}

    public SurveyStudent(String surveyStudentName, boolean surveyStudentStatus, String surveyStudentUID, String subjectiveRespond) {
        SurveyStudentName = surveyStudentName;
        SurveyStudentStatus = surveyStudentStatus;
        SurveyStudentUID = surveyStudentUID;
        SubjectiveRespond = subjectiveRespond;
    }

    public String getSubjectiveRespond() {
        return SubjectiveRespond;
    }

    public void setSubjectiveRespond(String subjectiveRespond) {
        SubjectiveRespond = subjectiveRespond;
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

}
