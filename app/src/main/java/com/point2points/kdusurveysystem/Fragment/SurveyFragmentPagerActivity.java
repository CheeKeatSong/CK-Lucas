package com.point2points.kdusurveysystem.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.datamodel.Survey;

import java.util.ArrayList;

import static com.point2points.kdusurveysystem.adapter.admin.RecyclerSurveyTabAdapter.SurveyDataset;

public class SurveyFragmentPagerActivity extends AppCompatActivity {

    private static final String EXTRA_SURVEY_ID = "com.point2points.kdusurveysystem.survey_uid";

    private static ArrayList<Survey> surveyData = new ArrayList<>();

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, String uid) {
        Intent intent = new Intent(packageContext, SurveyFragmentPagerActivity.class);
        intent.putExtra(EXTRA_SURVEY_ID, uid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        this.surveyData = SurveyDataset;

        String uid = (String) getIntent()
                .getSerializableExtra(EXTRA_SURVEY_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_fragment_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Survey survey = surveyData.get(position);
                return SurveyFragment.newInstance(survey.getSurveyUID());
            }

            @Override
            public int getCount() {
                return surveyData.size();
            }
        });

        for (int i = 0; i <surveyData.size(); i++) {
            if (surveyData.get(i).getSurveyUID().equals(uid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
