package com.point2points.kdusurveysystem.RecylcerView;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.Lecturer;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.RecyclerViewAdapter;
import com.point2points.kdusurveysystem.adapter.util.DividerItemDecoration;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import com.point2points.kdusurveysystem.AdminToolbarDrawer;

public class RecyclerViewExample extends AdminToolbarDrawer {

    /**
     * RecyclerView: The new recycler view replaces the list view. Its more modular and therefore we
     * must implement some of the functionality ourselves and attach it to our recyclerview.
     * <p/>
     * 1) Position items on the screen: This is done with LayoutManagers
     * 2) Animate & Decorate views: This is done with ItemAnimators & ItemDecorators
     * 3) Handle any touch events apart from scrolling: This is now done in our adapter's ViewHolder
     */
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<String> mDataSet;

    private static final String TAG = "RecyclerViewExample";

    private static final String[] MOVIES = new String[]{

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        super.onCreateDrawer();
        super.onCreateToolbar();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("RecyclerView");
            }
        }

        recyclerView.setHasFixedSize(true);
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Item Decorator:
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.border)));
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        final ArrayList<Lecturer> lecturers = new ArrayList<>();

        String key = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("lecturer");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    lecturers.add(postSnapshot.getValue(Lecturer.class));
                    Log.e("Get Data", (postSnapshot.getValue(Lecturer.class).getFullName()));
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });

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
        //mDataSet = new ArrayList<String>(Arrays.asList(adapterData));
        mAdapter = new RecyclerViewAdapter(this, lecturers);
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
    }

    /**
     * Substitute for our onScrollListener for RecyclerView
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };


}

