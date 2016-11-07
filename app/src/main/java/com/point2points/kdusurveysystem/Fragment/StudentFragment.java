package com.point2points.kdusurveysystem.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.RecyclerSchoolTabAdapter;
import com.point2points.kdusurveysystem.adapter.RecyclerStudentTabAdapter;
import com.point2points.kdusurveysystem.admin.AdminToolbarDrawer;
import com.point2points.kdusurveysystem.datamodel.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.point2points.kdusurveysystem.adapter.RecyclerStudentTabAdapter.StudentDataset;

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
