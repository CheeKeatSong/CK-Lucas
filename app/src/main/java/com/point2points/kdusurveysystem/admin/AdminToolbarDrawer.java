package com.point2points.kdusurveysystem.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
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
import com.point2points.kdusurveysystem.RecyclerView.RecyclerView;
import com.point2points.kdusurveysystem.adapter.RecyclerLecturerTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerSchoolTabAdapter;
import com.point2points.kdusurveysystem.datamodel.Lecturer;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.datamodel.School;

import java.util.Locale;

public class AdminToolbarDrawer extends AppCompatActivity {

    private ImageButton optionButton, addButton, searchButton, backButton;
    private Spinner sortButton;
    private EditText searchEditText;
    private Toolbar mToolBar, mToolBar2;

    public int sortoption = 0;
    public static int tabIdentifier;
    public static int adapterIdentifier;

    private Drawer adminDrawer;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private static final String TAG = "AdminToolbarDrawer";

    protected void onCreateToolbar() {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        mToolBar = (Toolbar) findViewById(R.id.tToolbar);
        setSupportActionBar(mToolBar);
        mToolBar2 = (Toolbar) findViewById(R.id.t2Toolbar);
        setSupportActionBar(mToolBar2);

        optionButton = (ImageButton) findViewById(R.id.menu_item_option);
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

                String sortType = String.valueOf(sortButton.getSelectedItem());

                if (sortType.equals("A-Z")){
                    sortoption = 1;
                    switch (tabIdentifier){
                        case 1:
                    RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
                        case 2:
                    RecyclerSchoolTabAdapter.sortingData(sortoption);
                            break;
                    }
                }
                else if(sortType.equals("Z-A")){
                    sortoption = 2;
                    switch (tabIdentifier){
                        case 1:
                            RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
                        case 2:
                            RecyclerSchoolTabAdapter.sortingData(sortoption);
                            break;
                    }
                }
                else if(sortType.equals("Latest")){
                    sortoption = 3;
                    switch (tabIdentifier){
                        case 1:
                            RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
                        case 2:
                            RecyclerSchoolTabAdapter.sortingData(sortoption);
                            break;
                    }
                }
                else if(sortType.equals("Earliest")){
                    sortoption = 4;
                    switch (tabIdentifier){
                        case 1:
                            RecyclerLecturerTabAdapter.sortingData(sortoption);
                            break;
                        case 2:
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

                    LayoutInflater li = LayoutInflater.from(AdminToolbarDrawer.this);

                    switch(tabIdentifier){
                        case 1:

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
                                            final String inputEmail = email.getText().toString();
                                            final String inputPassword = password.getText().toString();
                                            final String inputFullName = fullname.getText().toString();
                                            final String inputUsername = username.getText().toString();

                                            if (!(inputEmail.contains("@")) || !(inputEmail.contains(".com"))) {
                                                Toast.makeText(getApplicationContext(), "Enter a proper format of email address!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (TextUtils.isEmpty(inputEmail)) {
                                                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (TextUtils.isEmpty(inputPassword)) {
                                                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (inputPassword.length() < 6) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.minimum_password), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            final String schoolName = "";
                                            final String schoolNameShort = "";

                                            mAuth = FirebaseAuth.getInstance();

                                            mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                                                    .addOnCompleteListener(AdminToolbarDrawer.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                                            FirebaseUser user = task.getResult().getUser();
                                                            String uid = user.getUid();

                                                            Lecturer lecturer = new Lecturer();
                                                            lecturer.createLecturer(inputEmail,inputPassword,inputFullName,inputUsername, uid, schoolName, schoolNameShort);

                                                            Toast.makeText(AdminToolbarDrawer.this, R.string.lecturer_data_creation_success, Toast.LENGTH_SHORT).show();

                                                            if (!task.isSuccessful()) {
                                                                Log.d(TAG, "onComplete: uid=" + user.getUid());
                                                                Toast.makeText(AdminToolbarDrawer.this, R.string.lecturer_data_creation_fail, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
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

                        case 2:

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

                                                    final String schoolName = "";
                                                    final String schoolNameShort = "";

                                                    School school = new School();
                                                    school.createSchool(inputSchoolName);

                                                    Toast.makeText(AdminToolbarDrawer.this, R.string.lecturer_data_creation_success, Toast.LENGTH_SHORT).show();

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
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(AdminToolbarDrawer.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                }
            }
        });

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
                                adapterIdentifier = 2;
                                Intent intentLecturer = new Intent(AdminToolbarDrawer.this, RecyclerView.class);
                                startActivity(intentLecturer);
                                finish();
                                break;
                            case 5:
                                adapterIdentifier = 5;
                                Intent intentSchool = new Intent(AdminToolbarDrawer.this, RecyclerView.class);
                                startActivity(intentSchool);
                                finish();
                                break;
                            default:
                                adapterIdentifier = 2;
                                Intent intent = new Intent(AdminToolbarDrawer.this, RecyclerView.class);
                                startActivity(intent);
                                finish();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}