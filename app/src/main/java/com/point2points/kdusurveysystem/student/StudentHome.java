package com.point2points.kdusurveysystem.student;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.daimajia.swipe.util.Attributes;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.admin.RecyclerSurveyTabAdapter;
import com.point2points.kdusurveysystem.adapter.student.StudentHomeRecyclerViewAdapter;
import com.point2points.kdusurveysystem.adapter.util.DividerItemDecoration;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class StudentHome extends StudentToolbarDrawer {

    private static android.support.v7.widget.RecyclerView recyclerView;
    private static android.support.v7.widget.RecyclerView.Adapter mAdapter;

    private static Context context;

    static ProgressBar progressBar;

    private int option;

    private static final String TAG = "StudentHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.student_home);

        super.onCreate(savedInstanceState);
        super.loadUserProfileInfo(savedInstanceState);

        context = getApplicationContext();

        progressBar = (ProgressBar) findViewById(R.id.progressBar_recycler_view);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0173B1, android.graphics.PorterDuff.Mode.MULTIPLY);

        recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.student_home_recycler_view);
        recyclerView.setHasFixedSize(true);
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Item Decorator:
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.border_grey)));
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        tabIdentifier = 1;
        mAdapter = new StudentHomeRecyclerViewAdapter(this);
        ((StudentHomeRecyclerViewAdapter)mAdapter).setMode(Attributes.Mode.Single);

        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);

        //notify data changes
        notifyDataChanges();
    }

    android.support.v7.widget.RecyclerView.OnScrollListener onScrollListener = new android.support.v7.widget.RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };

    public static void notifyDataChanges(){
        mAdapter.notifyDataSetChanged();
    }

    public static void onProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void offProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

    public static void setAdapterRefresh(){
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
