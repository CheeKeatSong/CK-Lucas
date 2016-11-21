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
import com.point2points.kdusurveysystem.datamodel.Subject;

import java.util.ArrayList;

import static com.point2points.kdusurveysystem.adapter.admin.RecyclerSubjectTabAdapter.SubjectDataset;

public class SubjectFragmentPagerActivity extends AppCompatActivity{

    private static final String EXTRA_SUBJECT_UID =
            "com.point2points.kdusurveysystem.subject_uid";

    private static ArrayList<Subject> subjectData = new ArrayList<>();

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, String uid) {
        Intent intent = new Intent(packageContext, SubjectFragmentPagerActivity.class);
        intent.putExtra(EXTRA_SUBJECT_UID, uid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        this.subjectData = SubjectDataset;

        String uid = (String) getIntent()
                .getSerializableExtra(EXTRA_SUBJECT_UID);

        mViewPager = (ViewPager) findViewById(R.id.activity_fragment_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Subject subject = subjectData.get(position);
                return SubjectFragment.newInstance(subject.getSubjectUid());
            }

            @Override
            public int getCount() {
                return subjectData.size();
            }
        });

        for (int i = 0; i <subjectData.size(); i++) {
            if (subjectData.get(i).getSubjectUid().equals(uid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
