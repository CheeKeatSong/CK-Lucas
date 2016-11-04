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
import com.point2points.kdusurveysystem.datamodel.Student;

import java.util.ArrayList;

import static com.point2points.kdusurveysystem.adapter.RecyclerStudentTabAdapter.StudentDataset;

public class StudentFragmentPagerActivity extends AppCompatActivity{

    private static final String EXTRA_STUDENT_UID =
            "com.point2points.kdusurveysystem.student_uid";

    private static ArrayList<Student> studentData = new ArrayList<>();

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, String uid) {
        Intent intent = new Intent(packageContext, StudentFragmentPagerActivity.class);
        intent.putExtra(EXTRA_STUDENT_UID, uid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        this.studentData = StudentDataset;

        String uid = (String) getIntent()
                .getSerializableExtra(EXTRA_STUDENT_UID);

        mViewPager = (ViewPager) findViewById(R.id.activity_fragment_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Student student = studentData.get(position);
                return StudentFragment.newInstance(student.getStudentUid());
            }

            @Override
            public int getCount() {
                return studentData.size();
            }
        });

        for (int i = 0; i <studentData.size(); i++) {
            if (studentData.get(i).getStudentUid().equals(uid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
