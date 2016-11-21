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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.Login;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.admin.RecyclerSchoolTabAdapter;
import com.point2points.kdusurveysystem.adapter.admin.RecyclerStudentTabAdapter;
import com.point2points.kdusurveysystem.datamodel.Admin;
import com.point2points.kdusurveysystem.datamodel.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.point2points.kdusurveysystem.adapter.admin.RecyclerStudentTabAdapter.StudentDataset;

public class StudentFragment extends Fragment{

        private static final String ARG_SUBJECT_ID = "Student_id";
        private static final int REQUEST_SCHOOL_RETRIEVE = 0;

        String schoolName;
        String schoolNameShort;

        private Student mStudent;
        private TextView studentDateTextView, studentNameTextView, studentEmailTextView, studentUsernameTextView, studentPointTextView, studentPasswordTextView, studentIDTextView,
                studentCategoryTextView; // studentSchoolTextView
        private EditText studentNameEditText, studentIDEditText, studentPasswordEditText, studentPointEditText;
        private RadioGroup studentCategoryRadioGroup;
        private RadioButton studentCategoryRadioDiploma, studentCategoryRadioDegree, studentCategoryRadioOther;
        private Button studentDataEditButton, studentCancelButton, studentSchoolButton;

        private static final String INPUT_SCHOOL_NAME = "com.point2points.kdusurveysystem.school_name";
        private static final String INPUT_SCHOOL_NAME_SHORT = "com.point2points.kdusurveysystem.school_name_short";

        private static ArrayList<Student> studentData = new ArrayList<>();

        //relogin after delete data

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //Context mContext;
        //Activity mActivity;

        Admin admin = new Admin();

        String UID;
        String adminEmail;

        String currEmail, currPassword;

        public static StudentFragment newInstance(String uid) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_SUBJECT_ID, uid);

            StudentFragment fragment = new StudentFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.studentData = StudentDataset;

            String uid = (String) getArguments().getSerializable(ARG_SUBJECT_ID);

            mStudent = getStudent(uid);
            currEmail = mStudent.getStudentEmail();
            currPassword = mStudent.getStudentPassword();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.student_fragment, container, false);

            studentNameTextView = (TextView) v.findViewById(R.id.fragment_student_name_text_view);
            studentNameTextView.setText("Name: ");

            studentNameEditText = (EditText) v.findViewById(R.id.fragment_student_name_edit_text);
            studentNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            studentNameEditText.setText(mStudent.getStudentName());
            studentNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mStudent.setStudentName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            studentIDTextView = (TextView) v.findViewById(R.id.fragment_student_id_text_view);
            studentIDTextView.setText("Student ID: ");

            studentIDEditText = (EditText) v.findViewById(R.id.fragment_student_id_edit_text);
            studentIDEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            studentIDEditText.setText(mStudent.getStudentID());
            studentIDEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mStudent.setStudentID(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            studentUsernameTextView = (TextView) v.findViewById(R.id.fragment_student_username_text_view);
            studentUsernameTextView.setText("Username: " + mStudent.getStudentUsername());

            studentEmailTextView = (TextView) v.findViewById(R.id.fragment_student_email_text_view);
            studentEmailTextView.setText("Email: " + mStudent.getStudentEmail());

            studentCategoryTextView = (TextView) v.findViewById(R.id.fragment_student_category_text_view);
            studentCategoryTextView.setText("Student Category: ");

            studentCategoryRadioGroup = (RadioGroup) v.findViewById(R.id.fragment_student_category_radio_group);
            studentCategoryRadioDiploma = (RadioButton) v.findViewById(R.id.fragment_student_category_radio_diploma);
            studentCategoryRadioDegree = (RadioButton) v.findViewById(R.id.fragment_student_category_radio_degree);
            studentCategoryRadioOther = (RadioButton) v.findViewById(R.id.fragment_student_category_radio_other);
            studentCategoryRadioDiploma.setText("Diploma");
            studentCategoryRadioDegree.setText("Degree");
            studentCategoryRadioOther.setText("Other");

            final String currentCategory = mStudent.getStudentCategory();
            switch (currentCategory) {
                case "Diploma":
                    studentCategoryRadioDiploma.setChecked(true);
                    studentCategoryRadioDegree.setChecked(false);
                    studentCategoryRadioOther.setChecked(false);
                    break;

                case "Degree":
                    studentCategoryRadioDiploma.setChecked(false);
                    studentCategoryRadioDegree.setChecked(true);
                    studentCategoryRadioOther.setChecked(false);
                    break;

                case "Other":
                    studentCategoryRadioDiploma.setChecked(false);
                    studentCategoryRadioDegree.setChecked(false);
                    studentCategoryRadioOther.setChecked(true);
                    break;
            }

            studentCategoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // checkedId is the RadioButton selected
                    if (checkedId == studentCategoryRadioDiploma.getId())
                        mStudent.setStudentCategory("Diploma");

                    else if (checkedId == studentCategoryRadioDegree.getId())
                        mStudent.setStudentCategory("Degree");

                    else
                        mStudent.setStudentCategory("Other");
                }
            });

            /*studentDepartmentTextView = (TextView) v.findViewById(R.id.fragment_student_department_text_view);
            studentDepartmentTextView.setText("Student Department: ");

            studentDepartmentEditText = (EditText) v.findViewById(R.id.fragment_student_department_edit_text);
            studentDepartmentEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            studentDepartmentEditText.setText(mStudent.getStudentDepartment());
            studentDepartmentEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mStudent.setStudentDepartment(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });*/

            studentPasswordTextView = (TextView) v.findViewById(R.id.fragment_student_password_text_view);
            studentPasswordTextView.setText("Password: ");

            studentPasswordEditText = (EditText) v.findViewById(R.id.fragment_student_password_edit_text);
            studentPasswordEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            studentPasswordEditText.setText(mStudent.getStudentPassword());
            studentPasswordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mStudent.setStudentPassword(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            studentPointTextView = (TextView) v.findViewById(R.id.fragment_student_point_text_view);
            studentPointTextView.setText("Point: ");

            studentPointEditText = (EditText) v.findViewById(R.id.fragment_student_point_edit_text);
            studentPointEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            studentPointEditText.setText(mStudent.getStudentPoint());
            studentPointEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mStudent.setStudentPoint(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //studentSchoolTextView = (TextView) v.findViewById(R.id.fragment_student_school_text_view);
            //studentSchoolTextView.setText(mStudent.getStudentSchool() + " (" + mStudent.getStudentSchoolShort() + ")");

            studentSchoolButton = (Button) v.findViewById(R.id.fragment_student_school_button);
            studentSchoolButton.setText(mStudent.getStudentSchool() + " (" + mStudent.getStudentSchoolShort() + ")");

            studentSchoolButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Toast.makeText(getActivity(), "Double tap a school to edit the data", Toast.LENGTH_SHORT).show();
                    Intent intent = RecyclerSchoolTabAdapter.newIntent(getActivity());
                    startActivityForResult(intent, REQUEST_SCHOOL_RETRIEVE);


                }
            });

            studentDateTextView = (TextView) v.findViewById(R.id.fragment_student_date_text_view);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            long studentEpoch = Long.parseLong(mStudent.getDate());
            String studentDate = sdf.format(new Date(studentEpoch));
            studentDateTextView.setText("Creation Date: " + studentDate);

            studentDataEditButton = (Button) v.findViewById(R.id.fragment_student_data_edit_button);
            studentDataEditButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    final Context context = studentDataEditButton.getContext();

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setMessage("Confirm making changes to " + mStudent.getStudentName() + "?");

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    mStudent.setStudentUsername(mStudent.getStudentID());
                                    mStudent.setStudentEmail(mStudent.getStudentID() + "@kdu-online.com");

                                    DatabaseReference mDatabase;
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("student").child(mStudent.getStudentUid());

                                    Map<String, Object> updateStudent = new HashMap<String, Object>();

                                    updateStudent.put("studentName", mStudent.getStudentName());
                                    updateStudent.put("studentID", mStudent.getStudentID());
                                    updateStudent.put("studentUsername", mStudent.getStudentID()); // Username is same as Student ID
                                    updateStudent.put("studentEmail", mStudent.getStudentID() + "@kdu-online.com");
                                    updateStudent.put("studentPassword", mStudent.getStudentPassword());
                                    updateStudent.put("studentPoint", mStudent.getStudentPoint());
                                    updateStudent.put("studentCategory", mStudent.getStudentCategory());
                                    updateStudent.put("studentSchool", mStudent.getStudentSchool());
                                    updateStudent.put("studentSchoolShort", mStudent.getStudentSchoolShort());
                                    //updateStudent.put("studentDepartment", mStudent.getStudentDepartment());

                                    mDatabase.updateChildren(updateStudent);

                                    String newEmail = mStudent.getStudentEmail();
                                    String newPassword = mStudent.getStudentPassword();

                                    if (!(newEmail.equals(currEmail)) || !(newPassword.equals(currPassword)) ) {
                                        Log.d("UPDATE", "I RUN");
                                        Log.d("NEW EMAIL", newEmail);
                                        Log.d("OLD EMAIL", currEmail);
                                        Log.d("NEW PW", newPassword);
                                        Log.d("OLDPW ", currPassword);
                                        updateAuthDetails(mStudent.getStudentEmail(), mStudent.getStudentPassword());
                                    }

                                    else
                                        Log.d("UPDATE", "I DONT RUN");

                                    Toast.makeText(context, "Changes applied to " + mStudent.getStudentName(), Toast.LENGTH_SHORT).show();
                                    refreshAdapter(mStudent);
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

            studentCancelButton = (Button) v.findViewById(R.id.fragment_student_data_cancel_button);
            studentCancelButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    getActivity().onBackPressed();
                }
            });
            return v;
        }

        public void updateAuthDetails(final String email, final String password) {
            //Context context = studentDataEditButton.getContext();
            //Activity mActivity = (Activity) context;

            UID = Login.adminLoginUID;  // Get logged in admin's credential from Login class
            adminEmail = Login.adminLoginEmail;

            FirebaseAuth.getInstance().signOut();

            Log.e("SIGN IN", currEmail + " | " + currPassword);
            mAuth.signInWithEmailAndPassword(currEmail, currPassword)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // there was an error
                                Log.e("SIGN IN ERROR", "DID NOT MANAGE TO LOGIN"); // Troubleshooting
                            } else {
                                Log.e("SIGN IN SUCCESSFUL", "MANAGED TO LOGIN"); // Troubleshooting

                                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(currEmail, currPassword);

                                currentUser.reauthenticate(credential)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                currentUser.updateEmail(email)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(email, "User email address updated.");;
                                                                }
                                                            }
                                                        });

                                                currentUser.updatePassword(password)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(password, "User password updated.");
                                                                }
                                                            }
                                                        });

                                                reauthAdmin();
                                            }
                                        });
                            }
                        }
                    });

            /*FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
                                Log.d("ERROR", "ERROR BEFORE ANYTHING");
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

                                                                else
                                                                    Log.d("ERROR", "ERROR EMAIL");
                                                            }
                                                        });

                                                user.updatePassword(password)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d("Student", "User password updated.");
                                                                }

                                                                else
                                                                    Log.d("ERROR", "ERROR PASSWORD");
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    });*/

            /*ref = FirebaseDatabase.getInstance().getReference();
            ref = ref.child("users").child("lecturer");
            query = ref;

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e("Count " ,""+snapshot.getChildrenCount());
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        if (UID.equals(postSnapshot.getValue(Student.class).getStudentUid())) {
                            student = postSnapshot.getValue(Student.class);
                            Log.e("Get Data", (postSnapshot.getValue(Student.class).getFullName()));
                            mAuth.signInWithEmailAndPassword(student.getStudentEmail(), student.getStudentPassword())
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



    public void reauthAdmin() {
        //Context context = studentDataEditButton.getContext();
        //final Activity mActivity = (Activity) context;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query;

        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("admin");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    if (UID.equals(postSnapshot.getValue(Admin.class).getAdminUid())) {
                        admin = postSnapshot.getValue(Admin.class);
                        Log.e("Get Data", (postSnapshot.getValue(Admin.class).getAdminName()));

                        mAuth.signInWithEmailAndPassword(admin.getAdminEmail(), admin.getAdminPassword())
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.d("Student", "Error reauthenticating!");
                                        } else {
                                            Log.d("Student", "Reauthenticated to " + admin.getAdminEmail() + " admin account");
                                        }
                                    }
                                });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }



        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            if (requestCode == REQUEST_SCHOOL_RETRIEVE){
                if (data == null){
                    return;
                }
                schoolName = RecyclerSchoolTabAdapter.schoolNameRetrieval(data);
                schoolNameShort = RecyclerSchoolTabAdapter.schoolNameShortRetrieval(data);

                mStudent.setStudentSchool(schoolName);
                mStudent.setStudentSchoolShort(schoolNameShort);

                studentSchoolButton.setText(mStudent.getStudentSchool() +" (" + mStudent.getStudentSchoolShort() + ")");
            }
        }

        private Student getStudent(String uid){
            for (Student student : studentData) {
                if (student.getStudentUid().equals(uid)) {
                    return student;
                }
            }
            return null;
        }

        public void refreshAdapter(Student changedStudent) {

            for (Student student : studentData) {
                if (student.getStudentUid().equals(changedStudent.getStudentUid())) {
                    student = changedStudent;
                    break;
                }
            }

            RecyclerStudentTabAdapter.StudentArrayListUpdate(studentData);
        }
}
