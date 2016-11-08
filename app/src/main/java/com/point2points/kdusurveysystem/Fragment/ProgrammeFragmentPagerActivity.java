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
import com.point2points.kdusurveysystem.datamodel.Programme;

import java.util.ArrayList;

import static com.point2points.kdusurveysystem.adapter.RecyclerProgrammeTabAdapter.ProgrammeDataset;

public class ProgrammeFragmentPagerActivity extends AppCompatActivity{

    private static final String EXTRA_PROGRAMME_UID =
            "com.point2points.kdusurveysystem.programme_uid";

    private static ArrayList<Programme> programmeData = new ArrayList<>();

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, String uid) {
        Intent intent = new Intent(packageContext, ProgrammeFragmentPagerActivity.class);
        intent.putExtra(EXTRA_PROGRAMME_UID, uid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        this.programmeData = ProgrammeDataset;

        String uid = (String) getIntent()
                .getSerializableExtra(EXTRA_PROGRAMME_UID);

        mViewPager = (ViewPager) findViewById(R.id.activity_fragment_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Programme programme = programmeData.get(position);
                return ProgrammeFragment.newInstance(programme.getProgrammeUid());
            }

            @Override
            public int getCount() {
                return programmeData.size();
            }
        });

        for (int i = 0; i <programmeData.size(); i++) {
            if (programmeData.get(i).getProgrammeUid().equals(uid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
