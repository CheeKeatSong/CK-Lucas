package com.point2points.kdusurveysystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
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
import com.point2points.kdusurveysystem.RecylcerView.RecyclerViewExample;

public class Login extends Activity{

    private EditText inputEmailAddress, inputPassword;
    private Button mLoginButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ImageButton showpass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.login);

        Firebase myFirebaseRef = new Firebase("https://kdu-survey-system.firebaseio.com/");

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmailAddress = (EditText) findViewById(R.id.email_edit_Text);
        inputPassword = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = (Button)findViewById(R.id.login_button);

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
                Intent i = new Intent(Login.this, RecyclerViewExample.class);
                startActivity(i);
                finish();

                String email = inputEmailAddress.getText().toString();
                final String password = inputPassword.getText().toString();

                if("admin".equals(email) && "admin".equals(password)){
                    Intent intent = new Intent(Login.this, AdminMainActivity.class);
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

                progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(Login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Intent i = new Intent(Login.this, RecyclerViewExample.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
            }});
    }
}

