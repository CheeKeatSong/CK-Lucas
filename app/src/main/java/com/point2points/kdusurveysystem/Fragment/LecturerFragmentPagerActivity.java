package com.point2points.kdusurveysystem.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.point2points.kdusurveysystem.datamodel.Lecturer;
import com.point2points.kdusurveysystem.R;

import java.util.ArrayList;

import static com.point2points.kdusurveysystem.adapter.admin.RecyclerLecturerTabAdapter.LecturerDataset;

public class LecturerFragmentPagerActivity extends AppCompatActivity {

    private static final String EXTRA_LECTURER_UID =
            "com.point2points.kdusurveysystem.lecturer_uid";

    private static ArrayList<Lecturer> lecturerData = new ArrayList<>();

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, String uid) {
        Intent intent = new Intent(packageContext, LecturerFragmentPagerActivity.class);
        intent.putExtra(EXTRA_LECTURER_UID, uid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        this.lecturerData = LecturerDataset;

        String uid = (String) getIntent()
                .getSerializableExtra(EXTRA_LECTURER_UID);

        mViewPager = (ViewPager) findViewById(R.id.activity_fragment_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Lecturer lecturer = lecturerData.get(position);
                return LecturerFragment.newInstance(lecturer.getUid());
            }

            @Override
            public int getCount() {
                return lecturerData.size();
            }
        });

        for (int i = 0; i <lecturerData.size(); i++) {
            if (lecturerData.get(i).getUid().equals(uid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}