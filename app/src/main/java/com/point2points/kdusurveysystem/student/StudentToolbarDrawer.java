package com.point2points.kdusurveysystem.student;

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
import com.point2points.kdusurveysystem.datamodel.Student;

import java.util.Locale;

import static android.view.View.GONE;

public class StudentToolbarDrawer extends AppCompatActivity{

    private static final String TAG = "StudentToolbarDrawer";

    private TextView userStudentName, userStudentProgramme, userStudentPoints;
    private ImageButton optionButton, userProfileButton, searchButton, backButton;
    private Spinner sortButton;
    private EditText searchEditText;
    private Toolbar mToolBar, mToolBar2, mToolBar3;

    public int sortoption = 0;
    public static int tabIdentifier;
    static Context mContext;
    private Activity mActivity;

    private Drawer adminDrawer;

    private Student mStudent;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    public void loadUserProfileInfo(final Bundle savedInstanceState){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String UID = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("student");
        Query query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.e("Student: " ,postSnapshot.getValue(Student.class).getStudentName());
                    if (UID.equals(postSnapshot.getValue(Student.class).getStudentUid())) {
                        mStudent = postSnapshot.getValue(Student.class);
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
        mActivity = (Activity)mContext;

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

        userStudentName = (TextView) findViewById(R.id.menu_item_username_student);
        userStudentName.setText(mStudent.getStudentUsername());

        userStudentProgramme = (TextView) findViewById(R.id.menu_item_programme_student);
        userStudentProgramme.setText(mStudent.getStudentProgramme());

        userStudentPoints = (TextView) findViewById(R.id.menu_item_point_student);
        userStudentPoints.setText(mStudent.getStudentPoint());


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
        searchButton = (ImageButton) findViewById(R.id.menu_item_search_student);
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

        sortButton = (Spinner) findViewById(R.id.menu_item_sort_student);
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
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.history);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.store);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_settings);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName(R.string.sign_out);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.blue_drawer_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(mStudent.getStudentName()).withEmail(mStudent.getStudentEmail()).withIcon(getResources().getDrawable(R.drawable.kdu_logo))
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
                        item3,
                        new DividerDrawerItem(),
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        int drawerIdentifier = (int)drawerItem.getIdentifier();

                        switch (drawerIdentifier){
                            case 5:
                                FirebaseAuth.getInstance().signOut();
                                Intent intentLogout = new Intent(StudentToolbarDrawer.this, Login.class);
                                startActivity(intentLogout);
                                finish();
                                break;
                            default:
                                Intent intent = new Intent(StudentToolbarDrawer.this, StudentHome.class);
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
