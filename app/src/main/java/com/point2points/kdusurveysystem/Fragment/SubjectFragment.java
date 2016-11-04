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
import com.point2points.kdusurveysystem.adapter.RecyclerSubjectTabAdapter;
import com.point2points.kdusurveysystem.datamodel.Subject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.point2points.kdusurveysystem.adapter.RecyclerSubjectTabAdapter.SubjectDataset;

public class SubjectFragment extends Fragment{

        private static final String ARG_SUBJECT_ID = "Subject_id";

        private Subject mSubject;
        private TextView subjectDateTextView, subjectNameTextView, subjectCategoryTextView, subjectSchoolTextView, subjectCodeTextView; // subjectDepartmentTextView
        private EditText subjectNameEditText, subjectCodeEditText; // subjectDepartmentEditText
        private RadioGroup subjectCategoryRadioGroup;
        private RadioButton subjectCategoryRadioDiploma, subjectCategoryRadioDegree, subjectCategoryRadioOther;
        private Button subjectDataEditButton, subjectCancelButton;

        private static ArrayList<Subject> subjectData = new ArrayList<>();

        public static SubjectFragment newInstance(String uid) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_SUBJECT_ID, uid);

            SubjectFragment fragment = new SubjectFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.subjectData = SubjectDataset;

            String uid = (String) getArguments().getSerializable(ARG_SUBJECT_ID);

            mSubject = getSubject(uid);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.subject_fragment, container, false);

            subjectNameTextView = (TextView) v.findViewById(R.id.fragment_subject_name_text_view);
            subjectNameTextView.setText("Subject Name: ");

            subjectNameEditText = (EditText) v.findViewById(R.id.fragment_subject_name_edit_text);
            subjectNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            subjectNameEditText.setText(mSubject.getSubjectName());
            subjectNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSubject.setSubjectName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            /*subjectNameShortTextView = (TextView) v.findViewById(R.id.fragment_subject_name_short_text_view);
            subjectNameShortTextView.setText("Subject Name: ");

            subjectNameShortEditText = (EditText) v.findViewById(R.id.fragment_subject_name_short_edit_text);
            subjectNameShortEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            subjectNameShortEditText.setText(mSubject.getSubjectNameShort());
            subjectNameShortEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSubject.setSubjectNameShort(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });*/

            subjectCategoryTextView = (TextView) v.findViewById(R.id.fragment_subject_category_text_view);
            subjectCategoryTextView.setText("Subject Category: ");

            subjectCategoryRadioGroup = (RadioGroup) v.findViewById(R.id.fragment_subject_category_radio_group);
            subjectCategoryRadioDiploma = (RadioButton) v.findViewById(R.id.fragment_subject_category_radio_diploma);
            subjectCategoryRadioDegree = (RadioButton) v.findViewById(R.id.fragment_subject_category_radio_degree);
            subjectCategoryRadioOther = (RadioButton) v.findViewById(R.id.fragment_subject_category_radio_other);
            subjectCategoryRadioDiploma.setText("Diploma");
            subjectCategoryRadioDegree.setText("Degree");
            subjectCategoryRadioOther.setText("Other");

            //int CAT1_ID = 101; //first radio button id
            //int CAT2_ID = 102; //second radio button id
            //subjectCategoryRadioDiploma.setId(CAT1_ID);
            //subjectCategoryRadioDegree.setId(CAT2_ID);

            final String currentCategory = mSubject.getSubjectCategory();
            switch (currentCategory) {
                case "Diploma":
                    subjectCategoryRadioDiploma.setChecked(true);
                    subjectCategoryRadioDegree.setChecked(false);
                    subjectCategoryRadioOther.setChecked(false);
                    break;

                case "Degree":
                    subjectCategoryRadioDiploma.setChecked(false);
                    subjectCategoryRadioDegree.setChecked(true);
                    subjectCategoryRadioOther.setChecked(false);
                    break;

                case "Other":
                    subjectCategoryRadioDiploma.setChecked(false);
                    subjectCategoryRadioDegree.setChecked(false);
                    subjectCategoryRadioOther.setChecked(true);
                    break;
            }

            subjectCategoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // checkedId is the RadioButton selected
                    if (checkedId == subjectCategoryRadioDiploma.getId())
                        mSubject.setSubjectCategory("Diploma");

                    else if (checkedId == subjectCategoryRadioDegree.getId())
                        mSubject.setSubjectCategory("Degree");

                    else
                        mSubject.setSubjectCategory("Other");
                }
            });

            /*subjectDepartmentTextView = (TextView) v.findViewById(R.id.fragment_subject_department_text_view);
            subjectDepartmentTextView.setText("Subject Department: ");

            subjectDepartmentEditText = (EditText) v.findViewById(R.id.fragment_subject_department_edit_text);
            subjectDepartmentEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            subjectDepartmentEditText.setText(mSubject.getSubjectDepartment());
            subjectDepartmentEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSubject.setSubjectDepartment(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });*/

            subjectSchoolTextView = (TextView) v.findViewById(R.id.fragment_subject_school_text_view);
            subjectSchoolTextView.setText(mSubject.getSubjectSchool() + " (" + mSubject.getSubjectSchoolShort() + ")");

            subjectCodeTextView = (TextView) v.findViewById(R.id.fragment_subject_code_text_view);
            subjectCodeTextView.setText("Subject Code: ");

            subjectCodeEditText = (EditText) v.findViewById(R.id.fragment_subject_code_edit_text) ;
            subjectCodeEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            subjectCodeEditText.setText(mSubject.getSubjectCode());
            subjectCodeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSubject.setSubjectSchool(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            subjectDateTextView = (TextView) v.findViewById(R.id.fragment_subject_date_text_view);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            long subjectEpoch = Long.parseLong(mSubject.getDate());
            String subjectDate = sdf.format(new Date(subjectEpoch));
            subjectDateTextView.setText("Creation Date: " + subjectDate);

            subjectDataEditButton = (Button) v.findViewById(R.id.fragment_subject_data_edit_button);
            subjectDataEditButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    final Context context = subjectDataEditButton.getContext();

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setMessage("Confirm making changes to " + mSubject.getSubjectName() + "?");

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    DatabaseReference mDatabase;
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("subject").child(mSubject.getSubjectUid());

                                    Map<String, Object> updateSubject = new HashMap<String, Object>();

                                    updateSubject.put("subjectName", mSubject.getSubjectName());
                                    updateSubject.put("subjectCategory", mSubject.getSubjectCategory());
                                    //updateSubject.put("subjectDepartment", mSubject.getSubjectDepartment());
                                    updateSubject.put("subjectCode", mSubject.getSubjectCode());

                                    mDatabase.updateChildren(updateSubject);

                                    Toast.makeText(context, "Changes applied to " + mSubject.getSubjectName(), Toast.LENGTH_SHORT).show();
                                    refreshAdapter(mSubject);
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

            subjectCancelButton = (Button) v.findViewById(R.id.fragment_subject_data_cancel_button);
            subjectCancelButton.setOnClickListener(new View.OnClickListener(){
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
        }

        private Subject getSubject(String uid){
            for (Subject subject : subjectData) {
                if (subject.getSubjectUid().equals(uid)) {
                    return subject;
                }
            }
            return null;
        }

        public void refreshAdapter(Subject changedSubject) {

            for (Subject subject : subjectData) {
                if (subject.getSubjectUid().equals(changedSubject.getSubjectUid())) {
                    subject = changedSubject;
                    break;
                }
            }

            RecyclerSubjectTabAdapter.SubjectArrayListUpdate(subjectData);
        }
}
