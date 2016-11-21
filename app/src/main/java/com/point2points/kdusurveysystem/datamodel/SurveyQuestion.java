package com.point2points.kdusurveysystem.datamodel;

public class SurveyQuestion {

    long surveyQuestionID;
    String surveyQuestion;

    public SurveyQuestion(){}

    public SurveyQuestion(long surveyQuestionID, String surveyQuestion) {
        this.surveyQuestionID = surveyQuestionID;
        this.surveyQuestion = surveyQuestion;
    }

    public long getSurveyQuestionID() {
        return surveyQuestionID;
    }

    public void setSurveyQuestionID(long surveyQuestionID) {
        this.surveyQuestionID = surveyQuestionID;
    }

    public String getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(String surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }
}
