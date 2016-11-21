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
import com.point2points.kdusurveysystem.datamodel.School;

import java.util.ArrayList;

import static com.point2points.kdusurveysystem.adapter.admin.RecyclerSchoolTabAdapter.SchoolDataset;

public class SchoolFragmentPagerActivity extends AppCompatActivity{

    private static final String EXTRA_SCHOOL_UID =
            "com.point2points.kdusurveysystem.school_uid";

    private static ArrayList<School> schoolData = new ArrayList<>();

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, String uid) {
        Intent intent = new Intent(packageContext, SchoolFragmentPagerActivity.class);
        intent.putExtra(EXTRA_SCHOOL_UID, uid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        this.schoolData = SchoolDataset;

        String uid = (String) getIntent()
                .getSerializableExtra(EXTRA_SCHOOL_UID);

        mViewPager = (ViewPager) findViewById(R.id.activity_fragment_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                School school = schoolData.get(position);
                return SchoolFragment.newInstance(school.getSchoolUid());
            }

            @Override
            public int getCount() {
                return schoolData.size();
            }
        });

        for (int i = 0; i <schoolData.size(); i++) {
            if (schoolData.get(i).getSchoolUid().equals(uid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
