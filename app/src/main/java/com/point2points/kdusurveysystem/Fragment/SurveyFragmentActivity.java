package com.point2points.kdusurveysystem.Fragment;

import android.content.Context;
import android.content.Intent;

public class SurveyFragmentActivity extends SingleFragmentActivity{

    private static final String EXTRA_SURVEY_ID = "com.point2points.kdusurveysystem.survey_uid";

    public static Intent newIntent(Context packageContext, String uid) {
        Intent intent = new Intent(packageContext, SurveyFragmentActivity.class);
        intent.putExtra(EXTRA_SURVEY_ID, uid);
        return intent;
    }

    @Override
    protected android.support.v4.app.Fragment createFragment() {
        String uid = (String) getIntent().getSerializableExtra(EXTRA_SURVEY_ID);
        return SurveyFragment.newInstance(uid);
    }
}
