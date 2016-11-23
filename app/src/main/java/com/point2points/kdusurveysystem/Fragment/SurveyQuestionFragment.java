package com.point2points.kdusurveysystem.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.student.StudentHomeRecyclerViewAdapter;
import com.point2points.kdusurveysystem.datamodel.Survey;
import com.point2points.kdusurveysystem.datamodel.SurveyQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.point2points.kdusurveysystem.Fragment.SurveyQuestionPagerActivity.surveyQuestionSet;
import static com.point2points.kdusurveysystem.student.StudentToolbarDrawer.mStudent;

public class SurveyQuestionFragment extends Fragment {

    protected FragmentActivity mActivity;

    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    static Query query;

    private static final String ARG_SURVEY_ID = "com.point2points.kdusurveysystem.survey_uid";
    private static final String ARG_QUESTION_ID = "com.point2points.kdusurveysystem.question_id";

    private long pointer;

    //To check answer status
    boolean answerStatusChecker;

    //To check whether to save data or not status
    boolean saveData;

    private String uid, subjectiveRespond, subjectiveRespondRecorder;
    private long qid;

    private Survey mSurvey;
    private SurveyQuestion mSurveyQuestion;

    private EditText subjectiveRespondEditText;
    private TextView surveyQuestionTextView, surveyQuestionNumberingTextView, surveyQuestionTitleTextView;
    private RadioGroup surveyQuestionRadioGroup;
    private RadioButton surveyQuestionRating1Radio, surveyQuestionRating2Radio, surveyQuestionRating3Radio, surveyQuestionRating4Radio, surveyQuestionRating5Radio;
    private LinearLayout surveyQuestionAbort, surveyQuestionBack, surveyQuestionNext, surveyQuestionSubmit;

    private static long[] responses = new long[18];
    private static long[] totalRating = new long[5];
    private static ArrayList<SurveyQuestion> surveyQuestionSetFragment = new ArrayList<>();

    public static SurveyQuestionFragment newInstance(String uid, long qid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SURVEY_ID, uid);
        args.putSerializable(ARG_QUESTION_ID, qid);

        SurveyQuestionFragment fragment = new SurveyQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyQuestionSetFragment = surveyQuestionSet;

        uid = (String) getArguments().getSerializable(ARG_SURVEY_ID);
        qid = (Long) getArguments().getSerializable(ARG_QUESTION_ID);

        mSurveyQuestion = getCurrentSurveyQuestion(qid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_question_fragment, container, false);

        surveyQuestionTitleTextView = (TextView) v.findViewById(R.id.fragment_survey_question_title_text_view);
        if (qid == 1) {
            surveyQuestionTitleTextView.setText("\nThe teaching of this subject\n");
        } else if (qid == 11) {
            surveyQuestionTitleTextView.setText("\nSubject content\n");
        }

        surveyQuestionNumberingTextView = (TextView) v.findViewById(R.id.fragment_survey_question_numbering_text_view);
        if (qid < 11) {
            surveyQuestionNumberingTextView.setText(Long.toString(qid) + ". ");
        } else if (qid < 19) {
            surveyQuestionNumberingTextView.setText(Long.toString(qid - 10) + ". ");
        } else {
            surveyQuestionNumberingTextView.setText("");
        }

        surveyQuestionTextView = (TextView) v.findViewById(R.id.fragment_survey_question_text_view);
        surveyQuestionTextView.setText(mSurveyQuestion.getSurveyQuestion());

        //subject respond edit text
        subjectiveRespondEditText = (EditText) v.findViewById(R.id.fragment_survey_question_subjective_respond);
        subjectiveRespondEditText.getBackground().setColorFilter(getResources().getColor(R.color.dark_kdu_blue), PorterDuff.Mode.SRC_IN);
        if (qid == 19) {
            subjectiveRespondEditText.setVisibility(View.VISIBLE);
        } else {
            subjectiveRespondEditText.setVisibility(View.GONE);
        }
        subjectiveRespondEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subjectiveRespond = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Abort button
        surveyQuestionAbort = (LinearLayout) v.findViewById(R.id.fragment_survey_question_abort_layout);
        surveyQuestionAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Context context = surveyQuestionAbort.getContext();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Are you sure you want to abort this survey?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        if (qid == 1) {
            surveyQuestionAbort.setVisibility(View.VISIBLE);
        } else {
            surveyQuestionAbort.setVisibility(View.GONE);
        }
        //End of abort button

        //Back button
        surveyQuestionBack = (LinearLayout) v.findViewById(R.id.fragment_survey_question_back_layout);
        surveyQuestionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyQuestionPagerActivity.getItem((int) (qid - 2));
            }
        });
        if (qid > 1) {
            surveyQuestionBack.setVisibility(View.VISIBLE);
        } else {
            surveyQuestionBack.setVisibility(View.GONE);
        }
        //End of previous button

        //Next button
        surveyQuestionNext = (LinearLayout) v.findViewById(R.id.fragment_survey_question_next_layout);
        surveyQuestionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyQuestionPagerActivity.getItem((int) (qid));
            }
        });
        if (qid < 19) {
            surveyQuestionNext.setVisibility(View.VISIBLE);
        } else {
            surveyQuestionNext.setVisibility(View.GONE);
        }
        //End of next button

        //Submit button
        surveyQuestionSubmit = (LinearLayout) v.findViewById(R.id.fragment_survey_question_submit_layout);
        surveyQuestionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerStatusChecker = false;

                final Context context = surveyQuestionSubmit.getContext();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Are you sure you want to submit these survey responses?\n(You cannot edit the response anymore after this.)");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                for (int i = 0; i <= 17; i++) {
                                    if (responses[i] == 0) {
                                        answerStatusChecker = true;

                                        final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);

                                        alertDialogBuilder2.setMessage("Please answer all the questions.(Subjective respond is optional)");

                                        alertDialogBuilder2
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder2.create();
                                        alertDialog.show();
                                        return;
                                    }
                                }

                                if (!answerStatusChecker) {
                                    for (int i = 0; i <= 17; i++) {
                                        long j = responses[i];
                                        switch ((int) j) {
                                            case 1:
                                                totalRating[0]++;
                                                break;
                                            case 2:
                                                totalRating[1]++;
                                                break;
                                            case 3:
                                                totalRating[2]++;
                                                break;
                                            case 4:
                                                totalRating[3]++;
                                                break;
                                            case 5:
                                                totalRating[4]++;
                                                break;
                                        }
                                    }

                                    saveData = true;

                                    ref = FirebaseDatabase.getInstance().getReference().child("survey").child(uid);
                                    ref.addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            mSurvey = snapshot.getValue(Survey.class);
                                            if (saveData) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                final String UID = user.getUid();

                                                DatabaseReference mDatabase;
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("survey").child(uid);
                                                Map<String, Object> updateSurvey = new HashMap<String, Object>();

                                                updateSurvey.put("surveyQ1", mSurvey.getSurveyQ1() + responses[0]);
                                                updateSurvey.put("surveyQ2", mSurvey.getSurveyQ2() + responses[1]);
                                                updateSurvey.put("surveyQ3", mSurvey.getSurveyQ3() + responses[2]);
                                                updateSurvey.put("surveyQ4", mSurvey.getSurveyQ4() + responses[3]);
                                                updateSurvey.put("surveyQ5", mSurvey.getSurveyQ5() + responses[4]);
                                                updateSurvey.put("surveyQ6", mSurvey.getSurveyQ6() + responses[5]);
                                                updateSurvey.put("surveyQ7", mSurvey.getSurveyQ7() + responses[6]);
                                                updateSurvey.put("surveyQ8", mSurvey.getSurveyQ8() + responses[7]);
                                                updateSurvey.put("surveyQ9", mSurvey.getSurveyQ9() + responses[8]);
                                                updateSurvey.put("surveyQ10", mSurvey.getSurveyQ10() + responses[9]);
                                                updateSurvey.put("surveyQ11", mSurvey.getSurveyQ11() + responses[10]);
                                                updateSurvey.put("surveyQ12", mSurvey.getSurveyQ12() + responses[11]);
                                                updateSurvey.put("surveyQ13", mSurvey.getSurveyQ13() + responses[12]);
                                                updateSurvey.put("surveyQ14", mSurvey.getSurveyQ14() + responses[13]);
                                                updateSurvey.put("surveyQ15", mSurvey.getSurveyQ15() + responses[14]);
                                                updateSurvey.put("surveyQ16", mSurvey.getSurveyQ16() + responses[15]);
                                                updateSurvey.put("surveyQ17", mSurvey.getSurveyQ17() + responses[16]);
                                                updateSurvey.put("surveyQ18", mSurvey.getSurveyQ18() + responses[17]);
                                                updateSurvey.put("surveyRatingScale1", mSurvey.getSurveyRatingScale1() + totalRating[0]);
                                                updateSurvey.put("surveyRatingScale2", mSurvey.getSurveyRatingScale2() + totalRating[1]);
                                                updateSurvey.put("surveyRatingScale3", mSurvey.getSurveyRatingScale3() + totalRating[2]);
                                                updateSurvey.put("surveyRatingScale4", mSurvey.getSurveyRatingScale4() + totalRating[3]);
                                                updateSurvey.put("surveyRatingScale5", mSurvey.getSurveyRatingScale5() + totalRating[4]);
                                                updateSurvey.put("surveyAttendance", mSurvey.getSurveyAttendance() + 1);

                                                mDatabase.updateChildren(updateSurvey);


                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("survey").child(uid).child("student").child(UID);
                                                Map<String, Object> updateSurveyStudent = new HashMap<String, Object>();

                                                updateSurveyStudent.put("surveyStudentStatus", true);

                                                mDatabase.updateChildren(updateSurveyStudent);

                                                //Update point (might not work for repetition, unless mStudent refreshed)
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("student").child(UID);
                                                Map<String, Object> updateStudent = new HashMap<String, Object>();

                                                updateStudent.put("studentPoint", Long.toString(Long.parseLong(mStudent.getStudentPoint()) + 50));

                                                mDatabase.updateChildren(updateStudent);

                                                saveData = false;
                                                //StudentHome.notifyDataChanges();
                                                //StudentHomeRecyclerViewAdapter.SurveyArrayListUpdate();
                                                StudentHomeRecyclerViewAdapter.FirebaseSurveyDataRetrieval();

                                                final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);

                                                alertDialogBuilder2.setMessage("You got 50 points!");

                                                alertDialogBuilder2
                                                        .setCancelable(false)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                            }
                                                        });
                                                AlertDialog alertDialog = alertDialogBuilder2.create();
                                                alertDialog.show();

                                                mActivity.finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError firebaseError) {
                                            Log.e("The read failed: ", firebaseError.getMessage());
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        if (qid == 19) {
            surveyQuestionSubmit.setVisibility(View.VISIBLE);
        } else {
            surveyQuestionSubmit.setVisibility(View.GONE);
        }
        //End of submit button

        surveyQuestionRadioGroup = (RadioGroup) v.findViewById(R.id.fragment_survey_question_rating_radio_group);
        surveyQuestionRating1Radio = (RadioButton) v.findViewById(R.id.fragment_survey_question_rating_1);
        surveyQuestionRating2Radio = (RadioButton) v.findViewById(R.id.fragment_survey_question_rating_2);
        surveyQuestionRating3Radio = (RadioButton) v.findViewById(R.id.fragment_survey_question_rating_3);
        surveyQuestionRating4Radio = (RadioButton) v.findViewById(R.id.fragment_survey_question_rating_4);
        surveyQuestionRating5Radio = (RadioButton) v.findViewById(R.id.fragment_survey_question_rating_5);
        surveyQuestionRating1Radio.setText("Strongly disagree");
        surveyQuestionRating2Radio.setText("Moderately disagree");
        surveyQuestionRating3Radio.setText("Neutral");
        surveyQuestionRating4Radio.setText("Moderately agree");
        surveyQuestionRating5Radio.setText("Strongly agree");

        if (qid == 19) {
            surveyQuestionRadioGroup.setVisibility(View.GONE);
        } else {
            surveyQuestionRadioGroup.setVisibility(View.VISIBLE);
        }

        //set current question pointer and exclude subjective respond
        if (qid < 19) {
            int i = (int) (qid - 1);
            pointer = responses[i];
        }

        //check the question option if the option is existing
        switch (Long.toString(pointer)) {
            case "1":
                surveyQuestionRating1Radio.setChecked(true);
                surveyQuestionRating2Radio.setChecked(false);
                surveyQuestionRating3Radio.setChecked(false);
                surveyQuestionRating4Radio.setChecked(false);
                surveyQuestionRating5Radio.setChecked(false);
                break;
            case "2":
                surveyQuestionRating1Radio.setChecked(false);
                surveyQuestionRating2Radio.setChecked(true);
                surveyQuestionRating3Radio.setChecked(false);
                surveyQuestionRating4Radio.setChecked(false);
                surveyQuestionRating5Radio.setChecked(false);
                break;
            case "3":
                surveyQuestionRating1Radio.setChecked(false);
                surveyQuestionRating2Radio.setChecked(false);
                surveyQuestionRating3Radio.setChecked(true);
                surveyQuestionRating4Radio.setChecked(false);
                surveyQuestionRating5Radio.setChecked(false);
                break;
            case "4":
                surveyQuestionRating1Radio.setChecked(false);
                surveyQuestionRating2Radio.setChecked(false);
                surveyQuestionRating3Radio.setChecked(false);
                surveyQuestionRating4Radio.setChecked(true);
                surveyQuestionRating5Radio.setChecked(false);
                break;
            case "5":
                surveyQuestionRating1Radio.setChecked(false);
                surveyQuestionRating2Radio.setChecked(false);
                surveyQuestionRating3Radio.setChecked(false);
                surveyQuestionRating4Radio.setChecked(false);
                surveyQuestionRating5Radio.setChecked(true);
                break;
        }

        //set current question rating
        surveyQuestionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId == surveyQuestionRating1Radio.getId()) {
                    pointer = 1;
                } else if (checkedId == surveyQuestionRating2Radio.getId()) {
                    pointer = 2;
                } else if (checkedId == surveyQuestionRating3Radio.getId()) {
                    pointer = 3;
                } else if (checkedId == surveyQuestionRating4Radio.getId()) {
                    pointer = 4;
                } else if (checkedId == surveyQuestionRating5Radio.getId()) {
                    pointer = 5;
                }
                responses[(int) (qid - 1)] = pointer;
            }
        });
        return v;
    }

    private SurveyQuestion getCurrentSurveyQuestion(long qid) {
        for (SurveyQuestion surveyQuestion : surveyQuestionSetFragment) {
            if (surveyQuestion.getSurveyQuestionID() == qid) {
                return surveyQuestion;
            }
        }
        return null;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

}
