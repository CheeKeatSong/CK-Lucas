package com.point2points.kdusurveysystem.RecyclerView;

import android.app.ActionBar;
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

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import com.point2points.kdusurveysystem.admin.AdminToolbarDrawer;

public class RecyclerViewLecturer extends AdminToolbarDrawer {

    private static Context context;

    private android.support.v7.widget.RecyclerView recyclerView;
    private static android.support.v7.widget.RecyclerView.Adapter mAdapter;

    static ProgressBar progressBar;

    private int option;

    private static final String TAG = "RecyclerViewLecturer";

    public void sorting(int option){
        this.option = option;
        mAdapter = new RecyclerLecturerTabAdapter(getApplicationContext());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.recycler_view);
        super.onCreateDrawer();
        super.onCreateToolbar(savedInstanceState);

        RecyclerViewLecturer.context = getApplicationContext();

        recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("RecyclerViewLecturer");
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

        /*ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " lecturer data");
                for (DataSnapshot msgSnapshot : snapshot.getChildren()) {
                    lecturers.add(msgSnapshot.getValue(Lecturer.class));
                    Log.i("Lecturer", msgSnapshot.getValue(Lecturer.class).getFullName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ref.addValueEventListener(postListener);
*/
        // Adapter:
        //[] adapterData = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        //LecturerDataset = new ArrayList<String>(Arrays.asList(adapterData));

        tabIdentifier = 2;
        mAdapter = new RecyclerLecturerTabAdapter(this);
        ((RecyclerLecturerTabAdapter) mAdapter).setMode(Attributes.Mode.Single);

        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
    }

    /**
     * Substitute for our onScrollListener for RecyclerViewLecturer
     */
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

}

