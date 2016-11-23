package com.point2points.kdusurveysystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.admin.AdminHome;
import com.point2points.kdusurveysystem.datamodel.Admin;
import com.point2points.kdusurveysystem.datamodel.Lecturer;
import com.point2points.kdusurveysystem.datamodel.Student;
import com.point2points.kdusurveysystem.lecturer.LecturerHome;
import com.point2points.kdusurveysystem.student.StudentHome;

import static android.content.ContentValues.TAG;

public class Login extends Activity{

    private static final String USER_UNIQUE_ID = "com.point2points.kdusurveysystem.user_unique_id";

    private EditText inputEmailAddress, inputPassword;
    private Button mLoginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar progressBar;
    private ImageButton showpass;

    // Store logged in admin's credentials for reauthentication when necessary.
    public static String loginUID;
    public static String loginEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        // Fill text into login for faster testing
        EditText loginField = (EditText)findViewById(R.id.email_edit_Text);
        EditText passwordField = (EditText)findViewById(R.id.password_edit_text);
        loginField.setText("0106759@kdu-online.com");
        passwordField.setText("0106759");

        final Firebase myFirebaseRef = new Firebase("https://kdu-survey-system.firebaseio.com");

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

        if (mAuth.getCurrentUser() != null) {

        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmailAddress = (EditText) findViewById(R.id.email_edit_Text);
        inputPassword = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = (Button)findViewById(R.id.login_button);

        inputEmailAddress.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);
        inputPassword.getBackground().setColorFilter(getResources().getColor(R.color.sky_blue), PorterDuff.Mode.SRC_IN);

        showpass = (ImageButton) findViewById(R.id.login_show_password);
        showpass.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0173B1, android.graphics.PorterDuff.Mode.MULTIPLY);

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String email = inputEmailAddress.getText().toString();
                final String password = inputPassword.getText().toString();

                if("admin".equals(email) && "admin".equals(password)){
                    Intent intent = new Intent(Login.this, AdminHome.class);
                    startActivity(intent);
                    finish();
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar .setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(Login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    final String UID = user.getUid();

                                    loginUID = UID;
                                    loginEmail = user.getEmail();
                                    //Log.e("LOGGED IN: ", user.getEmail());

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    ref = ref.child("users").child("lecturer");
                                    Query query = ref;

                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            Log.e("Count " ,""+snapshot.getChildrenCount());
                                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                                if (UID.equals(postSnapshot.getValue(Lecturer.class).getUid())) {
                                                    Intent intent = new Intent(Login.this, LecturerHome.class);
                                                    progressBar.setVisibility(View.GONE);
                                                    startActivity(intent);
                                                    finish();
                                                }}
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError firebaseError) {
                                            Log.e("The read failed: " ,firebaseError.getMessage());
                                        }
                                    });

                                    ref = FirebaseDatabase.getInstance().getReference();
                                    ref = ref.child("users").child("admin");
                                    query = ref;

                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            Log.e("Count " ,""+snapshot.getChildrenCount());
                                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                                if (UID.equals(postSnapshot.getValue(Admin.class).getAdminUid())) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Intent intent = new Intent(Login.this, AdminHome.class);
                                                    startActivity(intent);
                                                    finish();
                                                }}
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError firebaseError) {
                                            Log.e("The read failed: " ,firebaseError.getMessage());
                                        }
                                    });

                                    ref = FirebaseDatabase.getInstance().getReference();
                                    ref = ref.child("users").child("student");
                                    query = ref;

                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            Log.e("Count " ,""+snapshot.getChildrenCount());
                                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                                if (UID.equals(postSnapshot.getValue(Student.class).getStudentUid())) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Intent intent = new Intent(Login.this,StudentHome.class);
                                                    startActivity(intent);
                                                    finish();
                                                }}
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError firebaseError) {
                                            progressBar.setVisibility(View.GONE);
                                            Log.e("The read failed: " ,firebaseError.getMessage());
                                        }
                                    });

                                }
                            }
                        });
            }});
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

