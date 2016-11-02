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
    private TextView lecturerIdTextView, lecturerEmailTextView, lecturerDateTextView, lecturerFullNameTextView, lecturerUsernameTextView, lecturerPointTextView, lecturerPasswordTextView;
    private EditText lecturerPasswordEditText, lecturerFullNameEditText, lecturerUsernameEditText, lecturerPointEditText;
    private Button lecturerDataEditButton, lecturerCancelButton;

    private static ArrayList<Lecturer> lecturerData = new ArrayList<>();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lecturer_fragment, container, false);

        lecturerIdTextView = (TextView) v.findViewById(R.id.fragment_lecturer_id_text_view);
        lecturerIdTextView.setText("Lecturer ID: " + mLecturer.getLecturer_ID());

        lecturerFullNameTextView = (TextView) v.findViewById(R.id.fragment_lecturer_full_name_text_view);
        lecturerFullNameTextView.setText("Name: ");

        lecturerFullNameEditText = (EditText) v.findViewById(R.id.fragment_lecturer_full_name_edit_text);
        lecturerFullNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
        lecturerFullNameEditText.setText(mLecturer.getFullName());
        lecturerFullNameEditText.addTextChangedListener(new TextWatcher() {
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
        //RecyclerView.notifyDataChanges();
    }
}
