package com.point2points.kdusurveysystem.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.adapter.RecyclerLecturerTabAdapter;
import com.point2points.kdusurveysystem.datamodel.Lecturer;
import com.point2points.kdusurveysystem.R;

import static com.point2points.kdusurveysystem.adapter.RecyclerLecturerTabAdapter.LecturerDataset;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LecturerFragment extends Fragment{

    private static final String ARG_LECTURER_ID = "lecturer_id";

    private Lecturer mLecturer;
    private TextView lecturerIdTextView, lecturerEmailTextView, lecturerDateTextView, lecturerFullNameTextView, lecturerUsernameTextView, lecturerPointTextView, lecturerPasswordTextView, lecturerSchoolTextView;
    private EditText lecturerPasswordEditText, schoolNameEditText, lecturerUsernameEditText, lecturerPointEditText;
    private Button lecturerDataEditButton, lecturerCancelButton;

    private static ArrayList<Lecturer> lecturerData = new ArrayList<>();

    String currEmail, currPassword;

    public static LecturerFragment newInstance(String uid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LECTURER_ID, uid);

        LecturerFragment fragment = new LecturerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.lecturerData = LecturerDataset;

        String uid = (String) getArguments().getSerializable(ARG_LECTURER_ID);

        mLecturer = getLecturer(uid);
        currEmail = mLecturer.getEmailAddress();
        currPassword = mLecturer.getPassword();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lecturer_fragment, container, false);

        lecturerIdTextView = (TextView) v.findViewById(R.id.fragment_lecturer_id_text_view);
        lecturerIdTextView.setText("Lecturer ID: " + mLecturer.getLecturer_ID());

        lecturerFullNameTextView = (TextView) v.findViewById(R.id.fragment_lecturer_full_name_text_view);
        lecturerFullNameTextView.setText("Name: ");

        schoolNameEditText = (EditText) v.findViewById(R.id.fragment_lecturer_full_name_edit_text);
        schoolNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
        schoolNameEditText.setText(mLecturer.getFullName());
        schoolNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLecturer.setFullName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lecturerUsernameTextView = (TextView) v.findViewById(R.id.fragment_lecturer_user_name_text_view);
        lecturerUsernameTextView.setText("Username: ");

        lecturerUsernameEditText = (EditText) v.findViewById(R.id.fragment_lecturer_user_name_edit_text);
        lecturerUsernameEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
        lecturerUsernameEditText.setText(mLecturer.getUsername());
        lecturerUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLecturer.setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lecturerPointTextView = (TextView) v.findViewById(R.id.fragment_lecturer_point_text_view);
        lecturerPointTextView.setText("Point: ");

        lecturerPointEditText = (EditText) v.findViewById(R.id.fragment_lecturer_point_edit_text);
        lecturerPointEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
        lecturerPointEditText.setText(mLecturer.getPoint());
        lecturerPointEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLecturer.setPoint(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lecturerEmailTextView = (TextView) v.findViewById(R.id.fragment_lecturer_email_text_view);
        lecturerEmailTextView.setText("Lecturer Email: " + mLecturer.getEmailAddress());

        lecturerPasswordTextView = (TextView) v.findViewById(R.id.fragment_lecturer_password_text_view);
        lecturerPasswordTextView.setText("Password: ");

        lecturerPasswordEditText = (EditText) v.findViewById(R.id.fragment_lecturer_password_edit_text);
        lecturerPasswordEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
        lecturerPasswordEditText.setText(mLecturer.getPassword());
        lecturerPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLecturer.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lecturerSchoolTextView = (TextView) v.findViewById(R.id.fragment_lecturer_school_text_view);
        lecturerSchoolTextView.setText(mLecturer.getSchoolName() + " (" +  mLecturer.getSchoolNameShort() + ")");

        lecturerDateTextView = (TextView) v.findViewById(R.id.fragment_lecturer_date_text_view);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        long lecturerEpoch = Long.parseLong(mLecturer.getDate());
        String lecturerDate = sdf.format(new Date(lecturerEpoch));
        lecturerDateTextView.setText("Creation Date: " + lecturerDate);

        lecturerDataEditButton = (Button) v.findViewById(R.id.fragment_lecturer_data_edit_button);
        lecturerDataEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //Log.d("Check 1", lecturers.toString());
                final Context context = lecturerDataEditButton.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm making changes to " + mLecturer.getLecturer_ID() + "?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        DatabaseReference mDatabase;
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("lecturer").child(mLecturer.getUid());

                                        Map<String, Object> updateLecturer = new HashMap<String, Object>();

                                        updateLecturer.put("fullName", mLecturer.getFullName());
                                        updateLecturer.put("username", mLecturer.getUsername());
                                        updateLecturer.put("point",mLecturer.getPoint());
                                        updateLecturer.put("password", mLecturer.getPassword());

                                        mDatabase.updateChildren(updateLecturer);

                                        String newEmail = mLecturer.getEmailAddress();
                                        String newPassword = mLecturer.getPassword();

                                        if (!(newEmail.equals(currEmail)) || !(newPassword.equals(currPassword)) )
                                            updateAuthDetails(mLecturer.getEmailAddress(), mLecturer.getPassword());

                                        Toast.makeText(context, "Changes applied to " + mLecturer.getLecturer_ID(), Toast.LENGTH_SHORT).show();
                                        refreshAdapter(mLecturer);
                                        getActivity().onBackPressed();
                                    }
                                })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        lecturerCancelButton = (Button) v.findViewById(R.id.fragment_lecturer_data_cancel_button);
        lecturerCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    public void updateAuthDetails(final String email, final String password) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Context mContext = this.getContext();
        Activity mActivity = (Activity) mContext;

        //String UID = user.getUid();

        mAuth.signInWithEmailAndPassword(currEmail, currPassword)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                        } else {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(currEmail, currPassword);

                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            user.updateEmail(email)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Student", "User email address updated.");
                                                            }
                                                        }
                                                    });

                                            user.updatePassword(password)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Student", "User password updated.");
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });

        /*ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("lecturer");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (UID.equals(postSnapshot.getValue(Lecturer.class).getUid())) {
                        lecturer = postSnapshot.getValue(Lecturer.class);
                        Log.e("Get Data", (postSnapshot.getValue(Lecturer.class).getFullName()));
                        mAuth.signInWithEmailAndPassword(lecturer.getEmailAddress(), lecturer.getPassword())
                                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            // there was an error
                                        } else {
                                        }
                                    }
                                });
                    }}
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

    }

   private Lecturer getLecturer(String uid){
        for (Lecturer lecturer : lecturerData) {
            if (lecturer.getUid().equals(uid)) {
                return lecturer;
            }
        }
        return null;
    }

    public void refreshAdapter(Lecturer changedLecturer) {

        for (Lecturer lecturer : lecturerData) {
            if (lecturer.getUid().equals(changedLecturer.getUid())) {
                lecturer = changedLecturer;

                break;
            }
        }

        RecyclerLecturerTabAdapter.LecturerArrayListUpdate(lecturerData);

        //Log.d("Check 2", lecturers.toString());
        //RecyclerViewLecturer.notifyDataChanges();
    }
}
