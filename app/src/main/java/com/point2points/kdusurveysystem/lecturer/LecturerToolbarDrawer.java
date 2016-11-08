package com.point2points.kdusurveysystem.lecturer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.point2points.kdusurveysystem.Login;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.datamodel.Lecturer;

import java.util.Locale;

import static android.view.View.GONE;

public class LecturerToolbarDrawer extends AppCompatActivity {

    private static final String TAG = "StudentToolbarDrawer";

    private TextView userLecturerName, userLecturerSchool, userLecturerSPoints;
    private ImageButton optionButton, userProfileButton, searchButton, backButton;
    private Spinner sortButton;
    private EditText searchEditText;
    private Toolbar mToolBar, mToolBar2, mToolBar3;

    public int sortoption = 0;
    public static int tabIdentifier;
    static Context mContext;

    private Drawer adminDrawer;

    private Lecturer mLecturer;

    private Activity mActivity;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    public void loadUserProfileInfo(final Bundle savedInstanceState){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String UID = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("lecturer");
        Query query = ref;

        Log.e("lecturer: " ,"haha");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (UID.equals(postSnapshot.getValue(Lecturer.class).getLecturer_ID())) {
                        mLecturer = postSnapshot.getValue(Lecturer.class);
                        onCreateToolbar(savedInstanceState);
                        onCreateDrawer();
                    }}
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    protected void onCreateToolbar(Bundle savedInstanceState) {

        //get context
        mContext = this;
        mActivity = (Activity) mContext;

        //fix status bar
        Window window = mActivity.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // finally change the color
            window.setStatusBarColor(mActivity.getResources().getColor(R.color.dark_kdu_blue));
        }

        //prevent keypad auto pop-up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //check user auth status
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //toolbar setup
        mToolBar = (Toolbar) findViewById(R.id.studentProfileToolbar);
        setSupportActionBar(mToolBar);
        mToolBar2 = (Toolbar) findViewById(R.id.studentToolbar);
        setSupportActionBar(mToolBar2);
        mToolBar3 = (Toolbar) findViewById(R.id.t2Toolbar);
        setSupportActionBar(mToolBar3);

        //UserProfileToolBar
        userProfileButton = (ImageButton) findViewById(R.id.menu_item_profile_student);
        userProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            }
        });

        userLecturerName = (TextView) findViewById(R.id.menu_item_username_student);
        userLecturerName.setText(mLecturer.getUsername());

        userLecturerSchool = (TextView) findViewById(R.id.menu_item_programme_student);
        userLecturerSchool.setText(mLecturer.getSchoolName());

        userLecturerSPoints = (TextView) findViewById(R.id.menu_item_point_student);
        userLecturerSPoints.setText(mLecturer.getPoint());


        //UserToolBar
        optionButton = (ImageButton) findViewById(R.id.menu_item_option_student);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminDrawer.openDrawer();
            }
        });

        backButton = (ImageButton)findViewById(R.id.menu_item_back);
        searchEditText = (EditText) findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchEditText.getText().toString().toLowerCase(Locale.getDefault());
                switch (tabIdentifier) {
                    case 2:
                        //RecyclerLecturerTabAdapter.filter(text);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
        searchButton = (ImageButton) findViewById(R.id.menu_item_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar2.setVisibility(GONE);
                mToolBar3.setVisibility(View.VISIBLE);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar2.setVisibility(View.VISIBLE);
                mToolBar3.setVisibility(GONE);
            }
        });

        sortButton = (Spinner) findViewById(R.id.menu_item_sort);
        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(selectedItemView != null) {
                    ((TextView) selectedItemView).setVisibility(GONE);
                }

                String sortType = String.valueOf(sortButton.getSelectedItem());

                if (sortType.equals("A-Z")) {
                    sortoption = 1;
                }
                else if(sortType.equals("Z-A")) {
                    sortoption = 2;
                }
                else if(sortType.equals("Latest")) {
                    sortoption = 3;
                }
                else if(sortType.equals("Earliest")) {
                    sortoption = 4;
                }
                switch (tabIdentifier){

                    //RecyclerLecturerTabAdapter.sortingData(sortoption);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if (savedInstanceState != null) {
        }
    }

    protected void onCreateDrawer() {

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.store);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_settings);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.sign_out);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.blue_drawer_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(mLecturer.getSchoolName()).withEmail(mLecturer.getEmailAddress()).withIcon(getResources().getDrawable(R.drawable.kdu_logo))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        adminDrawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        int drawerIdentifier = (int)drawerItem.getIdentifier();

                        switch (drawerIdentifier){
                            case 4:
                                FirebaseAuth.getInstance().signOut();
                                Intent intentLogout = new Intent(LecturerToolbarDrawer.this, Login.class);
                                startActivity(intentLogout);
                                finish();
                                break;
                            default:
                                Intent intent = new Intent(LecturerToolbarDrawer.this, LecturerHome.class);
                                startActivity(intent);
                                finish();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }
}
