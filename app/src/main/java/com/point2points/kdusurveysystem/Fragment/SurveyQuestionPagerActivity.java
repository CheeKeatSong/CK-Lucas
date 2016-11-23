package com.point2points.kdusurveysystem.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.datamodel.Survey;
import com.point2points.kdusurveysystem.datamodel.SurveyQuestion;

import java.util.ArrayList;

import static com.point2points.kdusurveysystem.adapter.student.StudentHomeRecyclerViewAdapter.SurveyDataSet;

public class SurveyQuestionPagerActivity extends AppCompatActivity{

    private static final String EXTRA_SURVEY_ID ="com.point2points.kdusurveysystem.survey_uid";

    private static ArrayList<Survey> surveyData = new ArrayList<>();

    public static ArrayList<SurveyQuestion> surveyQuestionSet = new ArrayList<>();

    private static ViewPager mViewPager;

    SurveyQuestion surveyQuestion;

    public static Intent newIntent(Context packageContext, String uid) {
        Intent intent = new Intent(packageContext, SurveyQuestionPagerActivity.class);
        intent.putExtra(EXTRA_SURVEY_ID, uid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        this.surveyData = SurveyDataSet;

        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(1, "The lecturer is punctual."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(2, "The lecturer is well organized."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(3, "The lecturer communicates well (explanation / instruction)."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(4, "The lecturer is knowledgeable about the subject."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(5, "The lecturer explained the learning outcomes / objectives clearly."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(6, "The lecturer is available for out-of-class consultations."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(7, "The lecturer encourages active participation in class."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(8, "The lecturer encourages students to ask subject related questions."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(9, "The lecturer provides relevant learning materials to the subject."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(10, "The lecturer uses suitable technologies to enhance learning."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(11, "The subject information (course outline) is clear."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(12, "The learning outcomes/objectives of this subject is appropriate."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(13, "The workload in this syllabus is appropriate for the demands of the subject."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(14, "The module promotes critical thinking."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(15, "The assessment addresses the subject content."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(16, "I am clear with what is expected of me from the assessments."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(17, "Feedback provided on my work, written and / or verbal, helps me improve."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(18, "Feedback was provided within an appropriate time frame."));
        surveyQuestionSet.add(surveyQuestion = new SurveyQuestion(19, "Subjective Respond (Feedback)\n"));

        final String uid = (String) getIntent().getSerializableExtra(EXTRA_SURVEY_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_fragment_view_pager);
        mViewPager.setOffscreenPageLimit(18);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                surveyQuestion = surveyQuestionSet.get(position);
                return SurveyQuestionFragment.newInstance(uid, surveyQuestion.getSurveyQuestionID());
            }
            @Override
            public int getCount() {
                return surveyQuestionSet.size();
            }
        });

        mViewPager.setCurrentItem(0);
    }

    public static void getItem(int i) {
        mViewPager.setCurrentItem(i);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Closing Survey")
                .setMessage("Are you sure you want to exit this survey?\n(The survey progress will not be saved)")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
