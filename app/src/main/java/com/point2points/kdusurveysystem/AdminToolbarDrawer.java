package com.point2points.kdusurveysystem;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.quickstart.database.models.Post;
//import com.google.firebase.quickstart.database.models.User;

public class AdminToolbarDrawer extends AppCompatActivity {

    private ImageButton optionButton, addButton, searchButton, backButton;
    private Spinner sortButton;
    private SearchView searchEditText;
    private Toolbar mToolBar, mToolBar2;

    private Drawer adminDrawer;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private static final String TAG = "AdminToolbarDrawer";

    protected void onCreateToolbar() {

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
        searchEditText = (SearchView) findViewById(R.id.search_edit_text);
        searchEditText.setIconified(false);
        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
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
                ((TextView)selectedItemView).setText(null);
                ListSorter listSorter = new ListSorter();

                if (sortType == "A-Z"){

                }
                else if(sortType == "Z-A"){

                }
                else if(sortType == "Latest"){

                }
                else if(sortType == "Earliest"){

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

                Toast.makeText(AdminToolbarDrawer.this, "Your mother ass",
                               Toast.LENGTH_SHORT).show();

                mDatabase = FirebaseDatabase.getInstance().getReference();


                //final String testId = "TESTID123456";

                //Lecturer lecturer = new Lecturer("babayaga@email.com", "password123");
                //UserTest user = new UserTest("babayaga@email.com", "password123");

                //mDatabase.child("users").child(testId).setValue(user);
                //mDatabase.child("users").child("lecturer").child(testId).setValue(user);
                //mDatabase.child("users").child("lecturer").child(testId).setValue(lecturer);
                //mDatabase.child("users").child(testId).setValue(lecturer);

                Firebase ref = new Firebase("https://kdu-survey-system.firebaseio.com");

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                LayoutInflater li = LayoutInflater.from(AdminToolbarDrawer.this);
                View promptsView = li.inflate(R.layout.lecturer_prompt, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminToolbarDrawer.this);

                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setTitle("CREATE A LECTURER INFO");

                final EditText email = (EditText) promptsView.findViewById(R.id.lecturer_dialog_email);
                final EditText password = (EditText) promptsView.findViewById(R.id.lecturer_dialog_password);
                final EditText fullname = (EditText) promptsView.findViewById(R.id.lecturer_dialog_fullname);
                final EditText username = (EditText) promptsView.findViewById(R.id.lecturer_dialog_username);

                final ImageButton showpass = (ImageButton) promptsView.findViewById(R.id.lecturer_dialog_show_password);
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

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            final String inputEmail = email.getText().toString();
                                            final String inputPassword = password.getText().toString();
                                            final String inputFullName = fullname.getText().toString();
                                            final String inputUsername = username.getText().toString();

                                            mAuth = FirebaseAuth.getInstance();

                                            mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                                                    .addOnCompleteListener(AdminToolbarDrawer.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                                            FirebaseUser user = task.getResult().getUser();
                                                            //Lecturer lecturer = new Lecturer();
                                                            //lecturer.createLecturer(inputEmail,inputPassword,inputFullName,inputUsername,user.getUid());

                                                            Double zero = 0.00;
                                                            final String testId = "TESTID7890";

                                                            Lecturer lecturer = new Lecturer(inputEmail,inputPassword,inputFullName,inputUsername,zero);
                                                            mDatabase.child("users").child("lecturer").child(testId).setValue(lecturer);

                                                            if (!task.isSuccessful()) {
                                                                Log.d(TAG, "onComplete: uid=" + user.getUid());
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
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

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
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_settings);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName(R.string.sign_out);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.kdu_glenmarie_view)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.kdu_logo))
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
                        new DividerDrawerItem(),
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
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