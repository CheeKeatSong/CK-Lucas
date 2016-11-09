package com.point2points.kdusurveysystem.admin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.point2points.kdusurveysystem.Login;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewLecturer;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewProgramme;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewSchool;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewStudent;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewSubject;
import com.point2points.kdusurveysystem.adapter.RecyclerLecturerTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerProgrammeTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerSchoolTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerStudentTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerSubjectTabAdapter;
import com.point2points.kdusurveysystem.datamodel.Admin;
import com.point2points.kdusurveysystem.datamodel.Lecturer;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.datamodel.Programme;
import com.point2points.kdusurveysystem.datamodel.School;
import com.point2points.kdusurveysystem.datamodel.Student;
import com.point2points.kdusurveysystem.datamodel.Subject;

import java.util.Locale;

import static android.view.View.GONE;

public class AdminToolbarDrawer extends AppCompatActivity {

    private static final String TAG = "AdminToolbarDrawer";
    private static final int REQUEST_SCHOOL_RETRIEVE = 0;
    private static final int REQUEST_PROGRAMME_RETRIEVE = 1;

    //Lecturer Data Creation
    private static final String INPUT_LECTURER_EMAIL = "com.point2points.kdusurveysystem.lecturer_email";
    private static final String INPUT_LECTURER_PASSWORD = "com.point2points.kdusurveysystem.lecturer_password";
    private static final String INPUT_LECTURER_FULLNAME = "com.point2points.kdusurveysystem.lecturer_fullname";
    private static final String INPUT_LECTURER_USERNAME = "com.point2points.kdusurveysystem.lecturer_username";
    //private static final String INPUT_LECTURER_ID = "com.point2points.kdusurveysystem.lecturer_id";

    //Subject Data Creation
    private static final String INPUT_SUBJECT_NAME = "com.point2points.kdusurveysystem.subject_name";
    private static final String INPUT_SUBJECT_CATEGORY = "com.point2points.kdusurveysystem.subject_category";
    private static final String INPUT_SUBJECT_CODE = "com.point2points.kdusurveysystem.subject_code";

    //School Data Creation
    private static final String INPUT_SCHOOL_NAME = "com.point2points.kdusurveysystem.school_name";
    private static final String INPUT_SCHOOL_NAME_SHORT = "com.point2points.kdusurveysystem.school_name_short";

    //Programme Data Creation
    private static final String INPUT_PROGRAMME_NAME = "com.point2points.kdusurveysystem.programme_name";
    private static final String INPUT_PROGRAMME_CATEGORY = "com.point2points.kdusurveysystem.programme_category";

    //Student Data Creation
    private static final String INPUT_STUDENT_NAME = "com.point2points.kdusurveysystem.student_name";
    //private static final String INPUT_STUDENT_EMAIL = "com.point2points.kdusurveysystem.student_email";
    private static final String INPUT_STUDENT_PASSWORD = "com.point2points.kdusurveysystem.student_password";
    private static final String INPUT_STUDENT_ID = "com.point2points.kdusurveysystem.student_id";
    private static final String INPUT_STUDENT_CATEGORY = "com.point2points.kdusurveysystem.student_category";

    String schoolName;
    String schoolNameShort;

    String programmeName;

    String inputLecturerEmail;
    String inputLecturerEmailFormatted;
    String inputLecturerPassword;
    String inputLecturerFullName;
    String inputLecturerUsername;
    //String inputLecturerID;

    String inputStudentName;
    String inputStudentID;
    String inputStudentPassword;
    String inputStudentCategory;

    String inputSubjectName;
    String inputSubjectCategory;
    String inputSubjectCode;

    String inputProgrammeName;
    String inputProgrammeCategory;

    private ImageButton optionButton, addButton, searchButton, backButton;
    private Spinner sortButton;
    private EditText searchEditText;
    private Toolbar mToolBar, mToolBar2;

    public int sortoption = 0;
    public static int tabIdentifierMutex;
    public static int tabIdentifier;
    static Context mContext;

    Admin mAdmin;

    private Activity mActivity;

    private Drawer adminDrawer;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    protected void loadUserProfileInfo(final Bundle savedInstanceState){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String UID = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("admin");
        Query query = ref;

        Log.e("lecturer: " ,UID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    //Log.e("lecturer: " ,"hahawa");
                    Log.e("Admin: ",(postSnapshot.getValue(Admin.class).getAdminName()));
                    if (UID.equals(postSnapshot.getValue(Admin.class).getAdminUid())) {
                        mAdmin = postSnapshot.getValue(Admin.class);

                        //Toolbar Creation
                        onCreateToolbar(savedInstanceState);

                        //No drawer if execute retrieve mode
                        if(!RecyclerSchoolTabAdapter.schoolRetrieval) {
                            onCreateDrawer();
                        }
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
                // ...
            }
        };

        //toolbar setup
        mToolBar = (Toolbar) findViewById(R.id.tToolbar);
        setSupportActionBar(mToolBar);
        mToolBar2 = (Toolbar) findViewById(R.id.t2Toolbar);
        setSupportActionBar(mToolBar2);

        optionButton = (ImageButton) findViewById(R.id.menu_item_option);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RecyclerSchoolTabAdapter.schoolRetrieval){
                adminDrawer.openDrawer();
            }
            else{
                    final Toast toast = Toast.makeText(mContext, "Please select data from the list", Toast.LENGTH_SHORT);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);
                }
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
                    RecyclerLecturerTabAdapter.filter(text);
                        break;
                    case 3:
                    RecyclerStudentTabAdapter.filter(text);
                        break;
                    case 4:
                    RecyclerSubjectTabAdapter.filter(text);
                        break;
                    case 5:
                    RecyclerSchoolTabAdapter.filter(text);
                        break;
                    case 6:
                        RecyclerProgrammeTabAdapter.filter(text);
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
                mToolBar.setVisibility(GONE);
                mToolBar2.setVisibility(View.VISIBLE);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar.setVisibility(View.VISIBLE);
                mToolBar2.setVisibility(GONE);
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
                        case 2:
                            RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
                        case 3:
                            RecyclerStudentTabAdapter.sortingData(sortoption);
                            break;
                        case 4:
                            RecyclerSubjectTabAdapter.sortingData(sortoption);
                            break;
                        case 5:
                            RecyclerSchoolTabAdapter.sortingData(sortoption);
                            break;
                        case 6:
                            RecyclerProgrammeTabAdapter.sortingData(sortoption);
                            break;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        addButton = (ImageButton) findViewById(R.id.menu_item_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase = FirebaseDatabase.getInstance().getReference();

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    dataCreation(tabIdentifier);
                } else {
                    Toast.makeText(AdminToolbarDrawer.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                }
            }
        });

        if (savedInstanceState != null) {
            inputLecturerEmail = savedInstanceState.getString(INPUT_LECTURER_EMAIL);
            inputLecturerPassword = savedInstanceState.getString(INPUT_LECTURER_PASSWORD);
            inputLecturerFullName = savedInstanceState.getString(INPUT_LECTURER_FULLNAME);
            inputLecturerUsername = savedInstanceState.getString(INPUT_LECTURER_USERNAME);
            inputSubjectName = savedInstanceState.getString(INPUT_SUBJECT_NAME);
            inputSubjectCategory = savedInstanceState.getString(INPUT_SUBJECT_CATEGORY);
            inputSubjectCode = savedInstanceState.getString(INPUT_SUBJECT_CODE);
            inputStudentName = savedInstanceState.getString(INPUT_STUDENT_NAME);
            inputStudentID = savedInstanceState.getString(INPUT_STUDENT_ID);
            inputStudentPassword = savedInstanceState.getString(INPUT_STUDENT_PASSWORD);
            inputStudentCategory = savedInstanceState.getString(INPUT_STUDENT_CATEGORY);
            inputProgrammeCategory = savedInstanceState.getString(INPUT_PROGRAMME_CATEGORY);
            schoolName = savedInstanceState.getString(INPUT_SCHOOL_NAME);
            schoolNameShort = savedInstanceState.getString(INPUT_SCHOOL_NAME_SHORT);
            programmeName = savedInstanceState.getString(INPUT_PROGRAMME_NAME);

        }

    }

    protected void onCreateDrawer() {

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.lecturer);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.student);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.subject);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName(R.string.school);
        SecondaryDrawerItem item6 = new SecondaryDrawerItem().withIdentifier(6).withName(R.string.programme);
        SecondaryDrawerItem item7 = new SecondaryDrawerItem().withIdentifier(7).withName(R.string.survey_list);
        SecondaryDrawerItem item8 = new SecondaryDrawerItem().withIdentifier(8).withName(R.string.drawer_item_settings);
        SecondaryDrawerItem item9 = new SecondaryDrawerItem().withIdentifier(9).withName(R.string.sign_out);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.blue_drawer_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(mAdmin.getAdminName()).withEmail(mAdmin.getAdminEmail()).withIcon(getResources().getDrawable(R.drawable.kdu_logo))
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
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        item4,
                        item5,
                        item6,
                        new DividerDrawerItem(),
                        item7,
                        new DividerDrawerItem(),
                        item8,
                        item9
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        int drawerIdentifier = (int)drawerItem.getIdentifier();

                        switch (drawerIdentifier){
                            case 2:
                                Intent intentLecturer = new Intent(AdminToolbarDrawer.this, RecyclerViewLecturer.class);
                                startActivity(intentLecturer);
                                finish();
                                break;
                            case 3:
                                Intent intentStudent = new Intent(AdminToolbarDrawer.this, RecyclerViewStudent.class);
                                startActivity(intentStudent);
                                finish();
                                break;
                            case 4:
                                Intent intentSubject = new Intent(AdminToolbarDrawer.this, RecyclerViewSubject.class);
                                startActivity(intentSubject);
                                finish();
                                break;
                            case 5:
                                Intent intentSchool = new Intent(AdminToolbarDrawer.this, RecyclerViewSchool.class);
                                startActivity(intentSchool);
                                finish();
                                break;
                            case 6:
                                Intent intentProgramme = new Intent(AdminToolbarDrawer.this, RecyclerViewProgramme.class);
                                startActivity(intentProgramme);
                                finish();
                                break;
                            case 9:
                                FirebaseAuth.getInstance().signOut();
                                Intent intentLogout = new Intent(AdminToolbarDrawer.this, Login.class);
                                startActivity(intentLogout);
                                finish();
                                break;
                            default:
                                Intent intent = new Intent(AdminToolbarDrawer.this, RecyclerViewLecturer.class);
                                startActivity(intent);
                                finish();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    public void dataCreation(final int tabIdentifier){

        LayoutInflater li = LayoutInflater.from(AdminToolbarDrawer.this);

        final int CAT1_ID = 101; //first radio button id
        final int CAT2_ID = 102; //second radio button id
        final int CAT3_ID = 103; //third radio button id

        switch(tabIdentifier){
            case 2:
                View lecturerPromptsView = li.inflate(R.layout.lecturer_creation_dialog, null);

                final AlertDialog.Builder lecturerDialogBuilder = new AlertDialog.Builder(AdminToolbarDrawer.this, R.style.MyDialogTheme);

                lecturerDialogBuilder.setView(lecturerPromptsView);
                lecturerDialogBuilder.setTitle("CREATE A LECTURER INFO");

                final EditText email = (EditText) lecturerPromptsView.findViewById(R.id.lecturer_dialog_email);
                final EditText password = (EditText) lecturerPromptsView.findViewById(R.id.lecturer_dialog_password);
                final EditText fullname = (EditText) lecturerPromptsView.findViewById(R.id.lecturer_dialog_fullname);
                final EditText username = (EditText) lecturerPromptsView.findViewById(R.id.lecturer_dialog_username);

                email.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                password.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                fullname.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                username.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);

                final ImageButton showpass = (ImageButton) lecturerPromptsView.findViewById(R.id.lecturer_dialog_show_password);
                showpass.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {

                        switch ( event.getAction() ) {
                            case MotionEvent.ACTION_DOWN:
                                password.setInputType(InputType.TYPE_CLASS_TEXT);
                                break;
                            case MotionEvent.ACTION_UP:
                                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                break;
                        }
                        return true;
                    }
                });

                lecturerDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        inputLecturerEmail = email.getText().toString();
                                        inputLecturerEmailFormatted = inputLecturerEmail + "@kdu.edu.my";
                                        inputLecturerPassword = password.getText().toString();
                                        inputLecturerFullName = fullname.getText().toString();
                                        inputLecturerUsername = username.getText().toString();

                                        if ((inputLecturerEmail.contains("@")) || (inputLecturerEmail.contains(".com"))) {
                                            Toast.makeText(getApplicationContext(), "Enter a proper format of email address!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (TextUtils.isEmpty(inputLecturerEmail)) {
                                            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(inputLecturerPassword)) {
                                            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (inputLecturerPassword.length() < 6) {
                                            Toast.makeText(getApplicationContext(), getString(R.string.minimum_password), Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        tabIdentifierMutex = tabIdentifier;
                                        Toast.makeText(AdminToolbarDrawer.this, "Select a school to complete data creation", Toast.LENGTH_SHORT).show();
                                        Intent intent = RecyclerSchoolTabAdapter.newIntent(mContext);
                                        startActivityForResult(intent, REQUEST_SCHOOL_RETRIEVE);

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog lecturerAlertDialog = lecturerDialogBuilder.create();
                lecturerAlertDialog.show();
                break;

            case 3:
                View studentPromptsView = li.inflate(R.layout.student_creation_dialog, null);

                final AlertDialog.Builder studentDialogBuilder = new AlertDialog.Builder(AdminToolbarDrawer.this, R.style.MyDialogTheme);

                studentDialogBuilder.setView(studentPromptsView);
                studentDialogBuilder.setTitle("CREATE A STUDENT INFO");

                final EditText studentName = (EditText) studentPromptsView.findViewById(R.id.student_dialog_name);
                final EditText studentID = (EditText) studentPromptsView.findViewById(R.id.student_dialog_ID);
                final EditText studentPassword = (EditText) studentPromptsView.findViewById(R.id.student_dialog_password);
                final RadioGroup studentCategory = (RadioGroup) studentPromptsView.findViewById(R.id.student_dialog_category);

                final RadioButton studentCategory1 = (RadioButton) studentPromptsView.findViewById(R.id.student_dialog_category_diploma);
                final RadioButton studentCategory2 = (RadioButton) studentPromptsView.findViewById(R.id.student_dialog_category_degree);
                final RadioButton studentCategory3 = (RadioButton) studentPromptsView.findViewById(R.id.student_dialog_category_other);

                studentCategory1.setId(View.generateViewId());
                studentCategory2.setId(View.generateViewId());
                studentCategory3.setId(View.generateViewId());

                studentName.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                studentID.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                studentPassword.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);

                final ImageButton showpassStudent = (ImageButton) studentPromptsView.findViewById(R.id.student_dialog_show_password);
                showpassStudent.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {

                        switch ( event.getAction() ) {
                            case MotionEvent.ACTION_DOWN:
                                studentPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                                break;
                            case MotionEvent.ACTION_UP:
                                studentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                break;
                        }
                        return true;
                    }
                });

                studentDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        inputStudentName = studentName.getText().toString();
                                        final int inputCategorySelection = studentCategory.getCheckedRadioButtonId();
                                        inputStudentID = studentID.getText().toString();
                                        inputStudentPassword = studentPassword.getText().toString();

                                        if (TextUtils.isEmpty(inputStudentName)) {
                                            Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(inputStudentID)) {
                                            Toast.makeText(getApplicationContext(), "Enter ID!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(inputStudentPassword)) {
                                            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (inputStudentPassword.length() < 6) {
                                            Toast.makeText(getApplicationContext(), getString(R.string.minimum_password), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (inputCategorySelection == -1) {
                                            Toast.makeText(getApplicationContext(), "Select category!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        /*switch(inputCategorySelection) {
                                            case CAT1_ID:
                                                inputStudentCategory = "Diploma";
                                                break;

                                            case CAT2_ID:
                                                inputStudentCategory = "Degree";
                                                break;

                                            case CAT3_ID:
                                                inputStudentCategory = "Other";
                                                break;

                                            default:
                                                inputStudentCategory = "None";
                                        }*/

                                        if (studentCategory1.isSelected())
                                            inputStudentCategory = "Diploma";

                                        else if (studentCategory2.isSelected())
                                            inputStudentCategory = "Degree";

                                        else if (studentCategory3.isSelected())
                                            inputStudentCategory = "Degree";

                                        else
                                            inputStudentCategory = "None";


                                        tabIdentifierMutex = tabIdentifier;
                                        Toast.makeText(AdminToolbarDrawer.this, "Select a school to complete data creation", Toast.LENGTH_SHORT).show();
                                        Intent intent = RecyclerSchoolTabAdapter.newIntent(mContext);
                                        startActivityForResult(intent, REQUEST_SCHOOL_RETRIEVE);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog studentAlertDialog = studentDialogBuilder.create();
                studentAlertDialog.show();
                break;

            case 4:
                View subjectPromptsView = li.inflate(R.layout.subject_creation_dialog, null);

                final AlertDialog.Builder subjectDialogBuilder = new AlertDialog.Builder(AdminToolbarDrawer.this, R.style.MyDialogTheme);

                subjectDialogBuilder.setView(subjectPromptsView);
                subjectDialogBuilder.setTitle("CREATE A SUBJECT INFO");

                final EditText subjectName = (EditText) subjectPromptsView.findViewById(R.id.subject_dialog_name);
                //final EditText subjectCategory = (EditText) subjectPromptsView.findViewById(R.id.subject_dialog_category);
                final RadioGroup subjectCategory = (RadioGroup) subjectPromptsView.findViewById(R.id.subject_dialog_category);
                //final EditText subjectDepartment = (EditText) subjectPromptsView.findViewById(R.id.subject_dialog_department);
                final EditText subjectCode = (EditText) subjectPromptsView.findViewById(R.id.subject_dialog_code);

                final RadioButton subjectCategory1 = (RadioButton) subjectPromptsView.findViewById(R.id.subject_dialog_category_diploma);
                final RadioButton subjectCategory2 = (RadioButton) subjectPromptsView.findViewById(R.id.subject_dialog_category_degree);
                final RadioButton subjectCategory3 = (RadioButton) subjectPromptsView.findViewById(R.id.subject_dialog_category_other);

                subjectCategory1.setTag(CAT1_ID);
                subjectCategory2.setTag(CAT2_ID);
                subjectCategory3.setTag(CAT3_ID);

                subjectName.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                //subjectCategory.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                //subjectDepartment.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                subjectCode.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);

                subjectDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        inputSubjectName = subjectName.getText().toString();
                                        final int inputCategorySelection = subjectCategory.getCheckedRadioButtonId();
                                        inputSubjectCode = subjectCode.getText().toString();

                                        /*if (!(inputEmail.contains("@")) || !(inputEmail.contains(".com"))) {
                                            Toast.makeText(getApplicationContext(), "Enter a proper format of email address!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }*/

                                        //Log.d("Selection 1", String.valueOf(inputCategorySelection));

                                        if (TextUtils.isEmpty(inputSubjectName)) {
                                            Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        /*if (TextUtils.isEmpty(inputCategory)) {
                                            Toast.makeText(getApplicationContext(), "Enter category!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }*/
                                        if (inputCategorySelection == -1) {
                                            Toast.makeText(getApplicationContext(), "Select category!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        /*if (TextUtils.isEmpty(inputDepartment)) {
                                            Toast.makeText(getApplicationContext(), "Enter department!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }*/
                                        if (TextUtils.isEmpty(inputSubjectCode)) {
                                            Toast.makeText(getApplicationContext(), "Enter subject code!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        //Log.d("Selection 2", String.valueOf(inputCategorySelection));

                                        switch(inputCategorySelection) {
                                            case CAT1_ID:
                                                inputSubjectCategory = "Diploma";
                                                break;

                                            case CAT2_ID:
                                                inputSubjectCategory = "Degree";
                                                break;

                                            case CAT3_ID:
                                                inputSubjectCategory = "Other";
                                                break;

                                            default:
                                                inputSubjectCategory = "None";
                                        }

                                        /*Toast.makeText(AdminToolbarDrawer.this, "Select a school to complete data creation", Toast.LENGTH_SHORT).show();
                                        Intent intent = RecyclerSchoolTabAdapter.newIntent(mContext);
                                        startActivity(intent);*/

                                        tabIdentifierMutex = tabIdentifier;
                                        Toast.makeText(AdminToolbarDrawer.this, "Select a school to complete data creation", Toast.LENGTH_SHORT).show();
                                        Intent intent = RecyclerSchoolTabAdapter.newIntent(mContext);
                                        startActivityForResult(intent, REQUEST_SCHOOL_RETRIEVE);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog subjectAlertDialog = subjectDialogBuilder.create();
                subjectAlertDialog.show();
                break;

            case 5:
                View promptsView = li.inflate(R.layout.school_creation_dialog, null);

                final AlertDialog.Builder schoolDialogBuilder = new AlertDialog.Builder(AdminToolbarDrawer.this, R.style.MyDialogTheme);

                schoolDialogBuilder.setView(promptsView);
                schoolDialogBuilder.setTitle("CREATE A SCHOOL INFO");

                final EditText schoolName = (EditText) promptsView.findViewById(R.id.school_dialog_name);

                schoolName.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);

                schoolDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        final String inputSchoolName = schoolName.getText().toString();

                                        if (!(inputSchoolName.contains("school")) && !(inputSchoolName.contains("School")) && !(inputSchoolName.contains("SCHOOL")) && !(inputSchoolName.contains("of")) && !(inputSchoolName.contains("OF")) && !(inputSchoolName.contains("Of"))){
                                            Toast.makeText(getApplicationContext(), "Enter a proper format of school name!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (TextUtils.isEmpty(inputSchoolName)) {
                                            Toast.makeText(getApplicationContext(), "Enter school name!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        School school = new School();
                                        school.createSchool(inputSchoolName);

                                        Toast.makeText(AdminToolbarDrawer.this, R.string.school_data_creation_success, Toast.LENGTH_SHORT).show();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog schoolAlertDialog = schoolDialogBuilder.create();
                schoolAlertDialog.show();
                break;
            case 6:
                View programmePromptsView = li.inflate(R.layout.programme_creation_dialog, null);

                final AlertDialog.Builder programmeDialogBuilder = new AlertDialog.Builder(AdminToolbarDrawer.this, R.style.MyDialogTheme);

                programmeDialogBuilder.setView(programmePromptsView);
                programmeDialogBuilder.setTitle("CREATE A PROGRAMME INFO");

                final EditText programmeName = (EditText) programmePromptsView.findViewById(R.id.programme_dialog_name);
                final RadioGroup programmeCategory = (RadioGroup) programmePromptsView.findViewById(R.id.programme_dialog_category);

                final RadioButton programmeCategory1 = (RadioButton) programmePromptsView.findViewById(R.id.programme_dialog_category_diploma);
                final RadioButton programmeCategory2 = (RadioButton) programmePromptsView.findViewById(R.id.programme_dialog_category_degree);
                final RadioButton programmeCategory3 = (RadioButton) programmePromptsView.findViewById(R.id.programme_dialog_category_other);

                programmeCategory1.setTag(CAT1_ID);
                programmeCategory2.setTag(CAT2_ID);
                programmeCategory3.setTag(CAT3_ID);

                programmeName.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);

                programmeDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        inputProgrammeName = programmeName.getText().toString();
                                        final int inputCategorySelection = programmeCategory.getCheckedRadioButtonId();

                                        if (TextUtils.isEmpty(inputProgrammeName)) {
                                            Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (inputCategorySelection == -1) {
                                            Toast.makeText(getApplicationContext(), "Select category!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        switch(inputCategorySelection) {
                                            case CAT1_ID:
                                                inputProgrammeCategory = "Diploma";
                                                break;

                                            case CAT2_ID:
                                                inputProgrammeCategory = "Degree";
                                                break;

                                            case CAT3_ID:
                                                inputProgrammeCategory = "Other";
                                                break;

                                            default:
                                                inputProgrammeCategory = "None";
                                        }

                                        tabIdentifierMutex = tabIdentifier;
                                        Toast.makeText(AdminToolbarDrawer.this, "Select a school to complete data creation", Toast.LENGTH_SHORT).show();
                                        Intent intent = RecyclerSchoolTabAdapter.newIntent(mContext);
                                        startActivityForResult(intent, REQUEST_SCHOOL_RETRIEVE);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog programmeAlertDialog = programmeDialogBuilder.create();
                programmeAlertDialog.show();
                break;
            default:
                break;
        }
    }

    public void lecturerDataCreation(){

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(inputLecturerEmailFormatted, inputLecturerPassword)
                .addOnCompleteListener(AdminToolbarDrawer.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        FirebaseUser user = task.getResult().getUser();
                        String uid = user.getUid();

                        Lecturer lecturer = new Lecturer();
                        lecturer.createLecturer(inputLecturerEmailFormatted,inputLecturerPassword,inputLecturerFullName,inputLecturerUsername, uid, schoolName, schoolNameShort);

                        Toast.makeText(AdminToolbarDrawer.this, R.string.lecturer_data_creation_success, Toast.LENGTH_SHORT).show();

                        inputLecturerEmail = null;
                        inputLecturerPassword = null;
                        inputLecturerFullName = null;
                        inputLecturerUsername = null;
                        schoolNameShort = null;
                        schoolName = null;

                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: uid=" + user.getUid());
                            Toast.makeText(AdminToolbarDrawer.this, R.string.lecturer_data_creation_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void studentDataCreation(){

        mAuth = FirebaseAuth.getInstance();

        String studentEmail = inputStudentID + "@kdu-online.com";

        mAuth.createUserWithEmailAndPassword(studentEmail, inputStudentPassword)
                .addOnCompleteListener(AdminToolbarDrawer.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        FirebaseUser user = task.getResult().getUser();
                        String UID = user.getUid();

                        Student student = new Student();
                        student.createStudent(inputStudentName, inputStudentID, inputStudentPassword, inputStudentCategory, schoolName,
                        schoolNameShort, UID, programmeName);

                        Toast.makeText(AdminToolbarDrawer.this, R.string.student_data_creation_success, Toast.LENGTH_SHORT).show();

                        inputStudentName = null;
                        inputStudentID = null;
                        inputStudentPassword = null;
                        inputStudentCategory = null;
                        programmeName = null;
                        schoolNameShort = null;
                        schoolName = null;

                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: uid=" + user.getUid());
                            Toast.makeText(AdminToolbarDrawer.this, R.string.student_data_creation_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void subjectDataCreation(){
        Subject subject = new Subject();
        //subject.createSubject(inputName, inputCategory, inputDepartment, inputSchool);
        subject.createSubject(inputSubjectName, inputSubjectCategory, schoolName, schoolNameShort, inputSubjectCode);
        Toast.makeText(AdminToolbarDrawer.this, R.string.subject_data_creation_success, Toast.LENGTH_SHORT).show();

        inputSubjectName = null;
        inputSubjectCategory = null;
        inputSubjectCode = null;
        schoolNameShort = null;
        schoolName = null;
    }

    public void programmeDataCreation(){
        Programme programme = new Programme();
        programme.createProgramme(inputProgrammeName, inputProgrammeCategory, schoolName, schoolNameShort);
        Toast.makeText(AdminToolbarDrawer.this, R.string.programme_data_creation_success, Toast.LENGTH_SHORT).show();

        inputProgrammeName = null;
        inputProgrammeCategory = null;
        schoolNameShort = null;
        schoolName = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putString(INPUT_LECTURER_EMAIL, inputLecturerEmail);
        savedInstanceState.putString(INPUT_LECTURER_PASSWORD, inputLecturerPassword);
        savedInstanceState.putString(INPUT_LECTURER_FULLNAME, inputLecturerFullName);
        savedInstanceState.putString(INPUT_LECTURER_USERNAME, inputLecturerUsername);
        savedInstanceState.putString(INPUT_SUBJECT_NAME, inputSubjectName);
        savedInstanceState.putString(INPUT_SUBJECT_CATEGORY, inputSubjectCategory);
        savedInstanceState.putString(INPUT_SUBJECT_CODE, inputSubjectCode);
        savedInstanceState.putString(INPUT_STUDENT_NAME, inputStudentName);
        savedInstanceState.putString(INPUT_STUDENT_PASSWORD, inputStudentPassword);
        savedInstanceState.putString(INPUT_STUDENT_ID, inputStudentID);
        savedInstanceState.putString(INPUT_STUDENT_CATEGORY, inputStudentCategory);
        savedInstanceState.putString(INPUT_PROGRAMME_CATEGORY, inputProgrammeCategory);
        savedInstanceState.putString(INPUT_SCHOOL_NAME, schoolName);
        savedInstanceState.putString(INPUT_SCHOOL_NAME_SHORT, schoolNameShort);
        savedInstanceState.putString(INPUT_PROGRAMME_NAME, programmeName);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_SCHOOL_RETRIEVE){
            if (data == null){
                return;
            }
            schoolName = RecyclerSchoolTabAdapter.schoolNameRetrieval(data);
            schoolNameShort = RecyclerSchoolTabAdapter.schoolNameShortRetrieval(data);
            switch (tabIdentifier){
                case 2:
                    lecturerDataCreation();
                    break;
                case 3:
                    retrieveProgrammeInfo();    // Wrong place?
                    break;
                case 4:
                    subjectDataCreation();
                    break;
                case 5:
                    break;
                case 6:
                    programmeDataCreation();
                default:
                    break;
            }
        }
        if (requestCode == REQUEST_PROGRAMME_RETRIEVE){
            if (data == null){
                return;
            }
            switch (tabIdentifier){
                case 2:
                    break;
                case 3:
                    studentDataCreation();  // Wrong place?
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    break;
            }
        }
    }

    public void retrieveProgrammeInfo(){
        tabIdentifierMutex = tabIdentifier;
        Toast.makeText(AdminToolbarDrawer.this, "Select a programme to complete data creation", Toast.LENGTH_SHORT).show();
        Intent intent = RecyclerProgrammeTabAdapter.newIntent(mContext);
        startActivityForResult(intent, REQUEST_PROGRAMME_RETRIEVE);
    }

}