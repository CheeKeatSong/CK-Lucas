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
import com.point2points.kdusurveysystem.adapter.admin.RecyclerProgrammeTabAdapter;
import com.point2points.kdusurveysystem.datamodel.Programme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.point2points.kdusurveysystem.adapter.admin.RecyclerProgrammeTabAdapter.ProgrammeDataset;

public class ProgrammeFragment extends Fragment{

        private static final String ARG_SUBJECT_ID = "Programme_id";

        private Programme mProgramme;
        private TextView programmeDateTextView, programmeNameTextView, programmeCategoryTextView, programmeSchoolTextView;
        private EditText programmeNameEditText;
        private RadioGroup programmeCategoryRadioGroup;
        private RadioButton programmeCategoryRadioDiploma, programmeCategoryRadioDegree, programmeCategoryRadioOther;
        private Button programmeDataEditButton, programmeCancelButton;

        private static ArrayList<Programme> programmeData = new ArrayList<>();

        public static ProgrammeFragment newInstance(String uid) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_SUBJECT_ID, uid);

            ProgrammeFragment fragment = new ProgrammeFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.programmeData = ProgrammeDataset;

            String uid = (String) getArguments().getSerializable(ARG_SUBJECT_ID);

            mProgramme = getProgramme(uid);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.programme_fragment, container, false);

            programmeNameTextView = (TextView) v.findViewById(R.id.fragment_programme_name_text_view);
            programmeNameTextView.setText("Programme Name: ");

            programmeNameEditText = (EditText) v.findViewById(R.id.fragment_programme_name_edit_text);
            programmeNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
            programmeNameEditText.setText(mProgramme.getProgrammeName());
            programmeNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mProgramme.setProgrammeName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            programmeCategoryTextView = (TextView) v.findViewById(R.id.fragment_programme_category_text_view);
            programmeCategoryTextView.setText("Programme Category: ");

            programmeCategoryRadioGroup = (RadioGroup) v.findViewById(R.id.fragment_programme_category_radio_group);
            programmeCategoryRadioDiploma = (RadioButton) v.findViewById(R.id.fragment_programme_category_radio_diploma);
            programmeCategoryRadioDegree = (RadioButton) v.findViewById(R.id.fragment_programme_category_radio_degree);
            programmeCategoryRadioOther = (RadioButton) v.findViewById(R.id.fragment_programme_category_radio_other);
            programmeCategoryRadioDiploma.setText("Diploma");
            programmeCategoryRadioDegree.setText("Degree");
            programmeCategoryRadioOther.setText("Other");

            final String currentCategory = mProgramme.getProgrammeCategory();
            switch (currentCategory) {
                case "Diploma":
                    programmeCategoryRadioDiploma.setChecked(true);
                    programmeCategoryRadioDegree.setChecked(false);
                    programmeCategoryRadioOther.setChecked(false);
                    break;

                case "Degree":
                    programmeCategoryRadioDiploma.setChecked(false);
                    programmeCategoryRadioDegree.setChecked(true);
                    programmeCategoryRadioOther.setChecked(false);
                    break;

                case "Other":
                    programmeCategoryRadioDiploma.setChecked(false);
                    programmeCategoryRadioDegree.setChecked(false);
                    programmeCategoryRadioOther.setChecked(true);
                    break;
            }

            programmeCategoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // checkedId is the RadioButton selected
                    if (checkedId == programmeCategoryRadioDiploma.getId())
                        mProgramme.setProgrammeCategory("Diploma");

                    else if (checkedId == programmeCategoryRadioDegree.getId())
                        mProgramme.setProgrammeCategory("Degree");

                    else
                        mProgramme.setProgrammeCategory("Other");
                }
            });

            programmeSchoolTextView = (TextView) v.findViewById(R.id.fragment_programme_school_text_view);
            programmeSchoolTextView.setText(mProgramme.getProgrammeSchool() + " (" + mProgramme.getProgrammeSchoolShort() + ")");

            //programmeDepartmentTextView = (TextView) v.findViewById(R.id.fragment_programme_department_text_view);
            //programmeDepartmentTextView.setText(mProgramme);

            programmeDateTextView = (TextView) v.findViewById(R.id.fragment_programme_date_text_view);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            long programmeEpoch = Long.parseLong(mProgramme.getDate());
            String programmeDate = sdf.format(new Date(programmeEpoch));
            programmeDateTextView.setText("Creation Date: " + programmeDate);

            programmeDataEditButton = (Button) v.findViewById(R.id.fragment_programme_data_edit_button);
            programmeDataEditButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    final Context context = programmeDataEditButton.getContext();

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setMessage("Confirm making changes to " + mProgramme.getProgrammeName() + "?");

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    DatabaseReference mDatabase;
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("programme").child(mProgramme.getProgrammeUid());

                                    Map<String, Object> updateProgramme = new HashMap<String, Object>();

                                    updateProgramme.put("programmeName", mProgramme.getProgrammeName());
                                    updateProgramme.put("programmeCategory", mProgramme.getProgrammeCategory());

                                    mDatabase.updateChildren(updateProgramme);

                                    Toast.makeText(context, "Changes applied to " + mProgramme.getProgrammeName(), Toast.LENGTH_SHORT).show();
                                    refreshAdapter(mProgramme);
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

            programmeCancelButton = (Button) v.findViewById(R.id.fragment_programme_data_cancel_button);
            programmeCancelButton.setOnClickListener(new View.OnClickListener(){
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

        private Programme getProgramme(String uid){
            for (Programme programme : programmeData) {
                if (programme.getProgrammeUid().equals(uid)) {
                    return programme;
                }
            }
            return null;
        }

        public void refreshAdapter(Programme changedProgramme) {

            for (Programme programme : programmeData) {
                if (programme.getProgrammeUid().equals(changedProgramme.getProgrammeUid())) {
                    programme = changedProgramme;
                    break;
                }
            }

            RecyclerProgrammeTabAdapter.ProgrammeArrayListUpdate(programmeData);
        }
}
