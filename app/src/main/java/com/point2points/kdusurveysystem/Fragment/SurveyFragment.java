package com.point2points.kdusurveysystem.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.RecyclerSubjectTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerSurveyTabAdapter;
import com.point2points.kdusurveysystem.datamodel.Subject;
import com.point2points.kdusurveysystem.datamodel.Survey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.point2points.kdusurveysystem.adapter.RecyclerSubjectTabAdapter.SubjectDataset;
import static com.point2points.kdusurveysystem.adapter.RecyclerSurveyTabAdapter.SurveyDataset;

public class SurveyFragment extends Fragment {

    private static final String ARG_SURVEY_ID = "Survey_id";

    private Survey mSurvey;
    private TextView subjectDateTextView, subjectNameTextView, subjectCategoryTextView, subjectSchoolTextView, subjectCodeTextView; // subjectDepartmentTextView
    private EditText subjectNameEditText, subjectCodeEditText; // subjectDepartmentEditText
    private RadioGroup subjectCategoryRadioGroup;
    private RadioButton subjectCategoryRadioDiploma, subjectCategoryRadioDegree, subjectCategoryRadioOther;
    private Button subjectDataEditButton, subjectCancelButton;

    private static ArrayList<Survey> surveyData = new ArrayList<>();

    public static SurveyFragment newInstance(String uid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SURVEY_ID, uid);

        SurveyFragment fragment = new SurveyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.surveyData = SurveyDataset;

        String uid = (String) getArguments().getSerializable(ARG_SURVEY_ID);

        mSurvey = getSurvey(uid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    private Survey getSurvey(String uid) {
        for (Survey survey : surveyData) {
            if (survey.getSurveyUID().equals(uid)) {
                return survey;
            }
        }
        return null;
    }

    public void refreshAdapter(Survey changedSurvey) {

        for (Survey survey : surveyData) {
            if (survey.getSurveyUID().equals(changedSurvey.getSurveyUID())) {
                survey = changedSurvey;
                break;
            }
        }

        RecyclerSurveyTabAdapter.SurveyArrayListUpdate(surveyData);
    }
}
