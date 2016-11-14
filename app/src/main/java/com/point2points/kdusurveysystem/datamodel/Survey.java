package com.point2points.kdusurveysystem.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Survey {

    private DatabaseReference ref;

    private String SurveyUID;
    private String SurveySubject;
    private String SurveySubjectCode;
    private String SurveySubjectCategory;
    private String SurveyLecturer;
    private String SurveyLecturerId;
    private String SurveySchool;
    private String SurveySchoolShort;
    private boolean SurveyStatus;
    private String Surveydate;
    private long SurveyQ1;
    private long SurveyQ2;
    private long SurveyQ3;
    private long SurveyQ4;
    private long SurveyQ5;
    private long SurveyQ6;
    private long SurveyQ7;
    private long SurveyQ8;
    private long SurveyQ9;
    private long SurveyQ10;
    private long SurveyQ11;
    private long SurveyQ12;
    private long SurveyQ13;
    private long SurveyQ14;
    private long SurveyQ15;
    private long SurveyQ16;
    private long SurveyQ17;
    private long SurveyQ18;
    private long SurveyRatingScale1;
    private long SurveyRatingScale2;
    private long SurveyRatingScale3;
    private long SurveyRatingScale4;
    private long SurveyRatingScale5;

    public Survey(){}

    public Survey(String surveyUID, String surveySubject, String surveySubjectCode, String SubjectCategory, String surveyLecturerName, String LecturerID, String surveySchool, String surveySchoolShort, boolean surveyStatus, long surveyDate, long surveyQ1, long surveyQ2, long surveyQ3, long surveyQ4, long surveyQ5, long surveyQ6, long surveyQ7, long surveyQ8, long surveyQ9, long surveyQ10, long surveyQ11, long surveyQ12, long surveyQ13, long surveyQ14, long surveyQ15, long surveyQ16, long surveyQ17, long surveyQ18, long surveyRatingScale1, long surveyRatingScale2, long surveyRatingScale3, long surveyRatingScale4, long surveyRatingScale5) {
        SurveyUID = surveyUID;
        SurveySubject = surveySubject;
        SurveySubjectCode = surveySubjectCode;
        SurveySubjectCategory = SubjectCategory;
        SurveyLecturer = surveyLecturerName;
        SurveyLecturerId = LecturerID;
        Surveydate = String.valueOf(surveyDate);
        SurveyQ1 = surveyQ1;
        SurveyQ2 = surveyQ2;
        SurveyQ3 = surveyQ3;
        SurveyQ4 = surveyQ4;
        SurveyQ5 = surveyQ5;
        SurveyQ6 = surveyQ6;
        SurveyQ7 = surveyQ7;
        SurveyQ8 = surveyQ8;
        SurveyQ9 = surveyQ9;
        SurveyQ10 = surveyQ10;
        SurveyQ11 = surveyQ11;
        SurveyQ12 = surveyQ12;
        SurveyQ13 = surveyQ13;
        SurveyQ14 = surveyQ14;
        SurveyQ15 = surveyQ15;
        SurveyQ16 = surveyQ16;
        SurveyQ17 = surveyQ17;
        SurveyQ18 = surveyQ18;
        SurveyRatingScale1 = surveyRatingScale1;
        SurveyRatingScale2 = surveyRatingScale2;
        SurveyRatingScale3 = surveyRatingScale3;
        SurveyRatingScale4 = surveyRatingScale4;
        SurveyRatingScale5 = surveyRatingScale5;
        SurveyStatus = surveyStatus;
        SurveySchool = surveySchool;
        SurveySchoolShort = surveySchoolShort;
    }

    public String getSurveyLecturerId() {
        return SurveyLecturerId;
    }

    public void setSurveyLecturerId(String surveyLecturerId) {
        SurveyLecturerId = surveyLecturerId;
    }

    public String getSurveydate() {
        return Surveydate;
    }

    public void setSurveydate(String surveydate) {
        Surveydate = surveydate;
    }

    public String getSurveySubjectCategory() {
        return SurveySubjectCategory;
    }

    public void setSurveySubjectCategory(String surveySubjectCategory) {
        SurveySubjectCategory = surveySubjectCategory;
    }

    public String getSurveyUID() {
        return SurveyUID;
    }

    public void setSurveyUID(String surveyUID) {
        SurveyUID = surveyUID;
    }

    public String getSurveySubject() {
        return SurveySubject;
    }

    public void setSurveySubject(String surveySubject) {
        SurveySubject = surveySubject;
    }

    public String getSurveySubjectCode() {
        return SurveySubjectCode;
    }

    public void setSurveySubjectCode(String surveySubjectCode) {
        SurveySubjectCode = surveySubjectCode;
    }

    public String getSurveyLecturer() {
        return SurveyLecturer;
    }

    public void setSurveyLecturer(String surveyLecturerName) {
        SurveyLecturer = surveyLecturerName;
    }

    public long getSurveyQ1() {
        return SurveyQ1;
    }

    public void setSurveyQ1(long surveyQ1) {
        SurveyQ1 = surveyQ1;
    }

    public long getSurveyQ2() {
        return SurveyQ2;
    }

    public void setSurveyQ2(long surveyQ2) {
        SurveyQ2 = surveyQ2;
    }

    public long getSurveyQ3() {
        return SurveyQ3;
    }

    public void setSurveyQ3(long surveyQ3) {
        SurveyQ3 = surveyQ3;
    }

    public long getSurveyQ4() {
        return SurveyQ4;
    }

    public void setSurveyQ4(long surveyQ4) {
        SurveyQ4 = surveyQ4;
    }

    public long getSurveyQ5() {
        return SurveyQ5;
    }

    public void setSurveyQ5(long surveyQ5) {
        SurveyQ5 = surveyQ5;
    }

    public long getSurveyQ6() {
        return SurveyQ6;
    }

    public void setSurveyQ6(long surveyQ6) {
        SurveyQ6 = surveyQ6;
    }

    public long getSurveyQ7() {
        return SurveyQ7;
    }

    public void setSurveyQ7(long surveyQ7) {
        SurveyQ7 = surveyQ7;
    }

    public long getSurveyQ8() {
        return SurveyQ8;
    }

    public void setSurveyQ8(long surveyQ8) {
        SurveyQ8 = surveyQ8;
    }

    public long getSurveyQ9() {
        return SurveyQ9;
    }

    public void setSurveyQ9(long surveyQ9) {
        SurveyQ9 = surveyQ9;
    }

    public long getSurveyQ10() {
        return SurveyQ10;
    }

    public void setSurveyQ10(long surveyQ10) {
        SurveyQ10 = surveyQ10;
    }

    public long getSurveyQ11() {
        return SurveyQ11;
    }

    public void setSurveyQ11(long surveyQ11) {
        SurveyQ11 = surveyQ11;
    }

    public long getSurveyQ12() {
        return SurveyQ12;
    }

    public void setSurveyQ12(long surveyQ12) {
        SurveyQ12 = surveyQ12;
    }

    public long getSurveyQ13() {
        return SurveyQ13;
    }

    public void setSurveyQ13(long surveyQ13) {
        SurveyQ13 = surveyQ13;
    }

    public long getSurveyQ14() {
        return SurveyQ14;
    }

    public void setSurveyQ14(long surveyQ14) {
        SurveyQ14 = surveyQ14;
    }

    public long getSurveyQ15() {
        return SurveyQ15;
    }

    public void setSurveyQ15(long surveyQ15) {
        SurveyQ15 = surveyQ15;
    }

    public long getSurveyQ16() {
        return SurveyQ16;
    }

    public void setSurveyQ16(long surveyQ16) {
        SurveyQ16 = surveyQ16;
    }

    public long getSurveyQ17() {
        return SurveyQ17;
    }

    public void setSurveyQ17(long surveyQ17) {
        SurveyQ17 = surveyQ17;
    }

    public long getSurveyQ18() {
        return SurveyQ18;
    }

    public void setSurveyQ18(long surveyQ18) {
        SurveyQ18 = surveyQ18;
    }

    public long getSurveyRatingScale1() {
        return SurveyRatingScale1;
    }

    public void setSurveyRatingScale1(long surveyRatingScale1) {
        SurveyRatingScale1 = surveyRatingScale1;
    }

    public long getSurveyRatingScale2() {
        return SurveyRatingScale2;
    }

    public void setSurveyRatingScale2(long surveyRatingScale2) {
        SurveyRatingScale2 = surveyRatingScale2;
    }

    public long getSurveyRatingScale3() {
        return SurveyRatingScale3;
    }

    public void setSurveyRatingScale3(long surveyRatingScale3) {
        SurveyRatingScale3 = surveyRatingScale3;
    }

    public long getSurveyRatingScale4() {
        return SurveyRatingScale4;
    }

    public void setSurveyRatingScale4(long surveyRatingScale4) {
        SurveyRatingScale4 = surveyRatingScale4;
    }

    public long getSurveyRatingScale5() {
        return SurveyRatingScale5;
    }

    public void setSurveyRatingScale5(long surveyRatingScale5) {
        SurveyRatingScale5 = surveyRatingScale5;
    }

    public boolean isSurveyStatus() {
        return SurveyStatus;
    }

    public void setSurveyStatus(boolean surveyStatus) {
        SurveyStatus = surveyStatus;
    }

    public String getSurveySchool() {
        return SurveySchool;
    }

    public void setSurveySchool(String surveySchool) {
        SurveySchool = surveySchool;
    }

    public String getSurveySchoolShort() {
        return SurveySchoolShort;
    }

    public void setSurveySchoolShort(String surveySchoolShort) {
        SurveySchoolShort = surveySchoolShort;
    }

    public void createSurvey(String SubjectName, String SubjectCode, String SubjectCategory, String Lecturer, String LecturerId, String School, String SchoolShort){
        ref = FirebaseDatabase.getInstance().getReference().child("survey");
        DatabaseReference keyref = ref.push();

        Survey survey = new Survey(keyref.getKey(), SubjectName, SubjectCode, SubjectCategory, Lecturer, LecturerId, School, SchoolShort, false, System.currentTimeMillis(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        keyref.setValue(survey);
    }
}
