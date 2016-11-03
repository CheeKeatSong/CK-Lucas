package com.point2points.kdusurveysystem.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.daimajia.swipe.util.Attributes;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.RecyclerLecturerTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerSchoolTabAdapter;
import com.point2points.kdusurveysystem.adapter.util.DividerItemDecoration;
import com.point2points.kdusurveysystem.admin.AdminToolbarDrawer;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class RecyclerViewSchool extends AdminToolbarDrawer {

    private static Context context;
    static Activity activity;

    private android.support.v7.widget.RecyclerView recyclerView;
    private static android.support.v7.widget.RecyclerView.Adapter mAdapter;

    static ProgressBar progressBar;

    private int option;

    private static final String TAG = "RecyclerViewSchool";

    public void sorting(int option){
        this.option = option;
        mAdapter = new RecyclerLecturerTabAdapter(getApplicationContext());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        context = getApplicationContext();
        setContentView(R.layout.recycler_view);

        //No drawer if execute retrieve mode
        if(!RecyclerSchoolTabAdapter.schoolRetrieval) {
            super.onCreateDrawer();
        }

        super.onCreateToolbar(savedInstanceState);

        RecyclerViewSchool.context = getApplicationContext();

        recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("RecyclerViewSchool");
            }
        }

        recyclerView.setHasFixedSize(true);
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Item Decorator:
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.border)));
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        progressBar = (ProgressBar) findViewById(R.id.progressBar_recycler_view);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0173B1, android.graphics.PorterDuff.Mode.MULTIPLY);
        onProgressBar();

        tabIdentifier = 5;
        mAdapter = new RecyclerSchoolTabAdapter(this);
        ((RecyclerSchoolTabAdapter) mAdapter).setMode(Attributes.Mode.Single);

        recyclerView.setAdapter(mAdapter);
        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
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

    public static void closeRecyclerViewSchool(){
        activity.finish();
    }

}


