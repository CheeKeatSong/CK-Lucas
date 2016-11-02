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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.RecyclerSchoolTabAdapter;
import com.point2points.kdusurveysystem.datamodel.School;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.point2points.kdusurveysystem.adapter.RecyclerSchoolTabAdapter.SchoolDataset;

public class SchoolFragment extends Fragment{

        private static final String ARG_SCHOOL_ID = "School_id";

        private School mSchool;
        private TextView schoolDateTextView, schoolNameTextView, schoolNameShortTextView;
        private EditText schoolNameShortEditText, schoolNameEditText;
        private Button schoolDataEditButton, schoolCancelButton;

        private static ArrayList<School> schoolData = new ArrayList<>();

        public static SchoolFragment newInstance(String uid) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_SCHOOL_ID, uid);

            SchoolFragment fragment = new SchoolFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.schoolData = SchoolDataset;

            String uid = (String) getArguments().getSerializable(ARG_SCHOOL_ID);

            mSchool = getSchool(uid);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.school_fragment, container, false);

            schoolNameTextView = (TextView) v.findViewById(R.id.fragment_school_name_text_view);
            schoolNameTextView.setText("School Name: ");

            schoolNameEditText = (EditText) v.findViewById(R.id.fragment_school_name_edit_text);
            schoolNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            schoolNameEditText.setText(mSchool.getSchoolName());
            schoolNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSchool.setSchoolName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            schoolNameShortTextView = (TextView) v.findViewById(R.id.fragment_school_name_short_text_view);
            schoolNameShortTextView.setText("School Name: ");

            schoolNameShortEditText = (EditText) v.findViewById(R.id.fragment_school_name_short_edit_text);
            schoolNameShortEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            schoolNameShortEditText.setText(mSchool.getSchoolNameShort());
            schoolNameShortEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mSchool.setSchoolNameShort(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            schoolDateTextView = (TextView) v.findViewById(R.id.fragment_school_date_text_view);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            long schoolEpoch = Long.parseLong(mSchool.getDate());
            String schoolDate = sdf.format(new Date(schoolEpoch));
            schoolDateTextView.setText("Creation Date: " + schoolDate);

            schoolDataEditButton = (Button) v.findViewById(R.id.fragment_school_data_edit_button);
            schoolDataEditButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    final Context context = schoolDataEditButton.getContext();

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setMessage("Confirm making changes to " + mSchool.getSchoolName() + "?");

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    DatabaseReference mDatabase;
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("school").child(mSchool.getSchoolUid());

                                    Map<String, Object> updateSchool = new HashMap<String, Object>();

                                    updateSchool.put("schoolName", mSchool.getSchoolName());
                                    updateSchool.put("schoolNameShort", mSchool.getSchoolNameShort());

                                    mDatabase.updateChildren(updateSchool);

                                    Toast.makeText(context, "Changes applied to " + mSchool.getSchoolName(), Toast.LENGTH_SHORT).show();
                                    refreshAdapter(mSchool);
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

            schoolCancelButton = (Button) v.findViewById(R.id.fragment_school_data_cancel_button);
            schoolCancelButton.setOnClickListener(new View.OnClickListener(){
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

        private School getSchool(String uid){
            for (School school : schoolData) {
                if (school.getSchoolUid().equals(uid)) {
                    return school;
                }
            }
            return null;
        }

        public void refreshAdapter(School changedSchool) {

            for (School school : schoolData) {
                if (school.getSchoolUid().equals(changedSchool.getSchoolUid())) {
                    school = changedSchool;
                    break;
                }
            }

            RecyclerSchoolTabAdapter.SchoolArrayListUpdate(schoolData);
        }
}
