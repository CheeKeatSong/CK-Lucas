package com.point2points.kdusurveysystem.admin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
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
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewLecturer;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewSchool;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewSubject;
import com.point2points.kdusurveysystem.adapter.RecyclerLecturerTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerSchoolTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerSubjectTabAdapter;
import com.point2points.kdusurveysystem.datamodel.Lecturer;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.datamodel.School;
import com.point2points.kdusurveysystem.datamodel.Subject;

import java.util.Locale;

public class AdminToolbarDrawer extends AppCompatActivity {

    private static final String TAG = "AdminToolbarDrawer";
    private static final int REQUEST_SCHOOL_RETRIEVE = 0;
    private static final String INPUT_LECTURER_EMAIL = "com.point2points.kdusurveysystem.lecturer_email";
    private static final String INPUT_LECTURER_PASSWORD = "com.point2points.kdusurveysystem.lecturer_password";
    private static final String INPUT_LECTURER_FULLNAME = "com.point2points.kdusurveysystem.lecturer_fullname";
    private static final String INPUT_LECTURER_USERNAME = "com.point2points.kdusurveysystem.lecturer_username";
    private static final String INPUT_SCHOOL_NAME = "com.point2points.kdusurveysystem.school_name";
    private static final String INPUT_SCHOOL_NAME_SHORT = "com.point2points.kdusurveysystem.school_name_short";

    String schoolName;
    String schoolNameShort;

    String inputLecturerEmail;
    String inputLecturerPassword;
    String inputLecturerFullName;
    String inputLecturerUsername;

    private ImageButton optionButton, addButton, searchButton, backButton;
    private Spinner sortButton;
    private EditText searchEditText;
    private Toolbar mToolBar, mToolBar2;

    public boolean lecturerDataCreationBoolean;
    public int sortoption = 0;
    public static int tabIdentifier;
    static Context mContext;

    private Drawer adminDrawer;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    protected void onCreateToolbar(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get context
        mContext = this;

        //prevent keypad auto pop-up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //check user auth status
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
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
                    final Toast toastOnDoubleClick = Toast.makeText(mContext, "Please select data from the list", Toast.LENGTH_SHORT);
                    toastOnDoubleClick.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toastOnDoubleClick.cancel();
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
                RecyclerLecturerTabAdapter.filter(text);
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
                mToolBar.setVisibility(View.GONE);
                mToolBar2.setVisibility(View.VISIBLE);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar.setVisibility(View.VISIBLE);
                mToolBar2.setVisibility(View.GONE);
            }
        });

        sortButton = (Spinner) findViewById(R.id.menu_item_sort);
        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ((TextView)selectedItemView).setText(null);

                String sortType = String.valueOf(sortButton.getSelectedItem());

                if (sortType.equals("A-Z")){
                    sortoption = 1;
                    switch (tabIdentifier){
<<<<<<< HEAD
                        case 2:
                    RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
                        case 5:
                    RecyclerSchoolTabAdapter.sortingData(sortoption);
=======
                        case 1:
                            RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
                        case 3:
                            RecyclerSubjectTabAdapter.sortingData(sortoption);
                            break;
                        case 2:
                            RecyclerSchoolTabAdapter.sortingData(sortoption);
>>>>>>> origin/master
                            break;
                    }
                }
                else if(sortType.equals("Z-A")){
                    sortoption = 2;
                    switch (tabIdentifier){
                        case 2:
                            RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
<<<<<<< HEAD
                        case 5:
=======
                        case 3:
                            RecyclerSubjectTabAdapter.sortingData(sortoption);
                            break;
                        case 2:
>>>>>>> origin/master
                            RecyclerSchoolTabAdapter.sortingData(sortoption);
                            break;
                    }
                }
                else if(sortType.equals("Latest")){
                    sortoption = 3;
                    switch (tabIdentifier){
                        case 2:
                            RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
<<<<<<< HEAD
                        case 5:
=======
                        case 3:
                            RecyclerSubjectTabAdapter.sortingData(sortoption);
                            break;
                        case 2:
>>>>>>> origin/master
                            RecyclerSchoolTabAdapter.sortingData(sortoption);
                            break;
                    }
                }
                else if(sortType.equals("Earliest")){
                    sortoption = 4;
                    switch (tabIdentifier){
                        case 2:
                            RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
<<<<<<< HEAD
                        case 5:
=======
                        case 3:
                            RecyclerSubjectTabAdapter.sortingData(sortoption);
                            break;
                        case 2:
>>>>>>> origin/master
                            RecyclerSchoolTabAdapter.sortingData(sortoption);
                            break;
                    }
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
            schoolName = savedInstanceState.getString(INPUT_SCHOOL_NAME);
            schoolNameShort = savedInstanceState.getString(INPUT_SCHOOL_NAME_SHORT);
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
                .withHeaderBackground(R.drawable.kdu_glenmarie_view)
                .addProfiles(
                        new ProfileDrawerItem().withName("CK Song").withEmail("0116708@kdu-online.com").withIcon(getResources().getDrawable(R.drawable.kdu_logo))
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
<<<<<<< HEAD
    
=======

    public static void getSchool(String schoolNameGet, String schoolNameShortGet){
        AdminToolbarDrawer.schoolName = schoolNameGet;
        AdminToolbarDrawer.schoolNameShort = schoolNameShortGet;
    }

    // Get department? Get lecturer?

>>>>>>> origin/master
    public void dataCreation(int tabIdentifier){

        LayoutInflater li = LayoutInflater.from(AdminToolbarDrawer.this);

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
                                        inputLecturerPassword = password.getText().toString();
                                        inputLecturerFullName = fullname.getText().toString();
                                        inputLecturerUsername = username.getText().toString();

                                        if (!(inputLecturerEmail.contains("@")) || !(inputLecturerEmail.contains(".com"))) {
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

<<<<<<< HEAD
            case 5:
=======
            case 3:

                View subjectPromptsView = li.inflate(R.layout.subject_creation_dialog, null);

                final AlertDialog.Builder subjectDialogBuilder = new AlertDialog.Builder(AdminToolbarDrawer.this, R.style.MyDialogTheme);

                subjectDialogBuilder.setView(subjectPromptsView);
                subjectDialogBuilder.setTitle("CREATE A SUBJECT INFO");

                final EditText subjectName = (EditText) subjectPromptsView.findViewById(R.id.subject_dialog_name);
                //final EditText subjectCategory = (EditText) subjectPromptsView.findViewById(R.id.subject_dialog_category);
                final RadioGroup subjectCategory = (RadioGroup) subjectPromptsView.findViewById(R.id.subject_dialog_category);
                //final EditText subjectDepartment = (EditText) subjectPromptsView.findViewById(R.id.subject_dialog_department);
                final EditText subjectSchool = (EditText) subjectPromptsView.findViewById(R.id.subject_dialog_school);

                final RadioButton subjectCategory1 = (RadioButton) subjectPromptsView.findViewById(R.id.subject_dialog_category_diploma);
                final RadioButton subjectCategory2 = (RadioButton) subjectPromptsView.findViewById(R.id.subject_dialog_category_degree);

                final int CAT1_ID = 101; //first radio button id
                final int CAT2_ID = 102; //second radio button id

                subjectCategory1.setId(CAT1_ID);
                subjectCategory2.setId(CAT2_ID);

                subjectName.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                //subjectCategory.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                //subjectDepartment.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
                subjectSchool.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);

                subjectDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        final String inputName = subjectName.getText().toString();
                                        //final String inputCategory = subjectCategory.getText().toString();
                                        final int inputCategorySelection = subjectCategory.getCheckedRadioButtonId();
                                        final String inputCategory;
                                        //final String inputDepartment = subjectDepartment.getText().toString();
                                        final String inputSchool = subjectSchool.getText().toString();

                                        /*if (!(inputEmail.contains("@")) || !(inputEmail.contains(".com"))) {
                                            Toast.makeText(getApplicationContext(), "Enter a proper format of email address!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }*/

                                        //Log.d("Selection 1", String.valueOf(inputCategorySelection));

                                        if (TextUtils.isEmpty(inputName)) {
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
                                        if (TextUtils.isEmpty(inputSchool)) {
                                            Toast.makeText(getApplicationContext(), "Enter school!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        //Log.d("Selection 2", String.valueOf(inputCategorySelection));

                                        switch(inputCategorySelection) {
                                            case CAT1_ID:
                                                inputCategory = "Diploma";
                                                break;

                                            case CAT2_ID:
                                                inputCategory = "Degree";
                                                break;

                                            default:
                                                inputCategory = "Other";
                                        }

                                        /*Toast.makeText(AdminToolbarDrawer.this, "Select a school to complete data creation", Toast.LENGTH_SHORT).show();
                                        Intent intent = RecyclerSchoolTabAdapter.newIntent(mContext);
                                        startActivity(intent);*/

                                        Subject subject = new Subject();
                                        //subject.createSubject(inputName, inputCategory, inputDepartment, inputSchool);
                                        subject.createSubject(inputName, inputCategory, inputSchool);

                                        Toast.makeText(AdminToolbarDrawer.this, R.string.subject_data_creation_success, Toast.LENGTH_SHORT).show();
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

            case 2:
>>>>>>> origin/master

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
<<<<<<< HEAD
=======

>>>>>>> origin/master
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
        }
    }
    
    public void lecturerDataCreation(){

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(inputLecturerEmail, inputLecturerPassword)
                .addOnCompleteListener(AdminToolbarDrawer.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        FirebaseUser user = task.getResult().getUser();
                        String uid = user.getUid();

                        Lecturer lecturer = new Lecturer();
                        lecturer.createLecturer(inputLecturerEmail,inputLecturerPassword,inputLecturerFullName,inputLecturerUsername, uid, schoolName, schoolNameShort);

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

    public void dataCreationInitiator(){
        if (lecturerDataCreationBoolean){
            Log.e("set " ,"school name");
            lecturerDataCreation();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putString(INPUT_LECTURER_EMAIL, inputLecturerEmail);
        savedInstanceState.putString(INPUT_LECTURER_PASSWORD, inputLecturerPassword);
        savedInstanceState.putString(INPUT_LECTURER_FULLNAME, inputLecturerFullName);
        savedInstanceState.putString(INPUT_LECTURER_USERNAME, inputLecturerUsername);
        savedInstanceState.putString(INPUT_SCHOOL_NAME, schoolName);
        savedInstanceState.putString(INPUT_SCHOOL_NAME_SHORT, schoolNameShort);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
            Log.e(TAG, "data1:" + schoolName);
            Log.e(TAG, "data2:" + schoolNameShort);
            lecturerDataCreation();
        }
    }

}