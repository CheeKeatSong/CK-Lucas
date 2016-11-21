package com.point2points.kdusurveysystem.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.adapter.admin.RecyclerStudentPickListAdapter;
import com.point2points.kdusurveysystem.adapter.admin.RecyclerSubjectiveRespondAdapter;
import com.point2points.kdusurveysystem.adapter.admin.RecyclerSurveyStudentListAdapter;
import com.point2points.kdusurveysystem.adapter.admin.RecyclerSurveyTabAdapter;
import com.point2points.kdusurveysystem.adapter.util.DividerItemDecoration;
import com.point2points.kdusurveysystem.datamodel.Student;
import com.point2points.kdusurveysystem.datamodel.Survey;
import com.point2points.kdusurveysystem.datamodel.SurveyStudent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static com.point2points.kdusurveysystem.adapter.admin.RecyclerSurveyTabAdapter.SurveyDataset;

public class SurveyFragment extends Fragment {

    private static Context context;

    static ProgressBar progressBar;

    static DatabaseReference ref;
    static Query query;

    //RecyclerView
    private android.support.v7.widget.RecyclerView studentListRecyclerView, subjectiveRespondRecyclerView;
    private static android.support.v7.widget.RecyclerView.Adapter studentListAdapter, subjectiveRespondAdapter;

    private static final String TAG = "RecyclerViewStudentPickList";

    private static final String ARG_SURVEY_ID = "Survey_id";

    private static final String EXTRA_STUDENT_PICK_LIST = "com.point2points.kdusurveysystem.student_pick_list";

    private static final int REQUEST_STUDENT_RETRIEVE = 4;

    boolean respondOverviewTab = false;
    boolean subjectiveRespondTab = false;

    private Survey mSurvey;
    private TextView surveySubjectTextView, surveyLecturerTextView, surveySchoolTextView, surveySemesterTextView, surveyDateTextView; // subjectDepartmentTextView
    private Button surveyDataOkayButton, surveyCancelButton;
    private Button surveyAddStudentButton, surveyDeleteStudentButton;
    private Button surveyRespondOverviewButton, surveySubjectiveRespondButton;
    private TextView surveyQuestionR1, surveyQuestionR2, surveyQuestionR3, surveyQuestionR4, surveyTotalRating1, surveyTotalRating2, surveyTotalRating3,  surveyTotalRating4,  surveyTotalRating5;
    private LinearLayout respondOverview, subjectiveRespond;

    private static ArrayList<Student> surveyStudentList = new ArrayList<>();
    private static ArrayList<Survey> surveyData = new ArrayList<>();
    //Retrieved Online
    private static ArrayList<SurveyStudent> surveyStudentOnline = new ArrayList<>();
    //The set which is used on display
    public static ArrayList<SurveyStudent> surveyStudentDataSet = new ArrayList<>();
    //The retrieved set from pick list
    private static ArrayList<SurveyStudent> surveyStudentDataSetSelected = new ArrayList<>();
    //The retrieved set from survey fragment list
    public static ArrayList<SurveyStudent> surveyStudentDataSetDelete = new ArrayList<>();

    public static SurveyFragment newInstance(String uid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SURVEY_ID, uid);

        SurveyFragment fragment = new SurveyFragment();
        fragment.setArguments(args);

        surveyStudentList = new ArrayList<>();
        surveyStudentOnline = new ArrayList<>();
        surveyStudentDataSetSelected = new ArrayList<>();
        surveyStudentDataSetDelete = new ArrayList<>();
        surveyStudentDataSet = new ArrayList<>();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.surveyData = SurveyDataset;

        String uid = (String) getArguments().getSerializable(ARG_SURVEY_ID);

        mSurvey = getSurvey(uid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_fragment, container, false);

        context = getActivity();

        FirebaseSurveyStudentDataRetrieval();

        //StudentListRecyclerView
        studentListRecyclerView = (android.support.v7.widget.RecyclerView) v.findViewById(R.id.fragment_survey_recycler_view);

        studentListRecyclerView.setHasFixedSize(true);
        // Layout Managers:
        studentListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Item Decorator:
        studentListRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.border_grey)));
        studentListRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        studentListAdapter = new RecyclerSurveyStudentListAdapter(getActivity(), surveyStudentDataSet);

        studentListRecyclerView.setAdapter(studentListAdapter);

        /* Listeners */
        studentListRecyclerView.setOnScrollListener(onScrollListener);
        //End of StudentListRecyclerView

        //subjectiveRespondRecyclerView
        subjectiveRespondRecyclerView = (android.support.v7.widget.RecyclerView) v.findViewById(R.id.recycler_view_subject_respond);

        subjectiveRespondRecyclerView.setHasFixedSize(true);
        // Layout Managers:
        subjectiveRespondRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Item Decorator:
        subjectiveRespondRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.border_grey)));
        subjectiveRespondRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        subjectiveRespondAdapter = new RecyclerSubjectiveRespondAdapter(getActivity(), surveyStudentDataSet);

        subjectiveRespondRecyclerView.setAdapter(subjectiveRespondAdapter);

        /* Listeners */
        subjectiveRespondRecyclerView.setOnScrollListener(onScrollListener);
        //End of subjectiveRespondRecyclerView

        //progress bar
        progressBar = (ProgressBar) v.findViewById(R.id.survey_fragment_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0173B1, android.graphics.PorterDuff.Mode.MULTIPLY);

        surveySubjectTextView = (TextView) v.findViewById(R.id.fragment_survey_subject_text_view);
        surveySubjectTextView.setText(mSurvey.getSurveySubject() + " (" + mSurvey.getSurveySubjectCode() + ")");

        surveyLecturerTextView = (TextView) v.findViewById(R.id.fragment_survey_lecturer);
        surveyLecturerTextView.setText(mSurvey.getSurveyLecturer() + " (" + mSurvey.getSurveyLecturerId() + ")");

        surveySchoolTextView = (TextView) v.findViewById(R.id.fragment_survey_school);
        surveySchoolTextView.setText(mSurvey.getSurveySchool() + " (" + mSurvey.getSurveySchoolShort() + ")");

        surveySemesterTextView = (TextView) v.findViewById(R.id.fragment_survey_sem);
        surveySemesterTextView.setText(mSurvey.getSurveySem());

        surveyDateTextView = (TextView) v.findViewById(R.id.fragment_survey_date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        long surveyEpoch = Long.parseLong(mSurvey.getSurveyDate());
        String surveyDate = sdf.format(new Date(surveyEpoch));
        surveyDateTextView.setText(surveyDate);

        surveyAddStudentButton = (Button) v.findViewById(R.id.survey_fragment_add_icon);
        surveyAddStudentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                surveyStudentDataSetSelected = new ArrayList<>();

                Toast.makeText(getActivity(), "Add student into this survey", Toast.LENGTH_SHORT).show();
                Intent intent = RecyclerStudentPickListAdapter.newIntent(getActivity(), surveyStudentDataSet);
                startActivityForResult(intent, REQUEST_STUDENT_RETRIEVE);
            }
        });

        surveyDeleteStudentButton = (Button) v.findViewById(R.id.survey_fragment_delete_icon);
        surveyDeleteStudentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //delete the arraylist and data in database
                final Context context = surveyDeleteStudentButton.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm delete selected students in this list?\n(Student's survey status will be reset)");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                //get children, compare children, if same, then remove value.
                                ArrayList<SurveyStudent> toRemove = new ArrayList<>();
                                for (SurveyStudent surveyStudent1 : surveyStudentDataSet) {
                                    for (SurveyStudent surveyStudent2 : surveyStudentDataSetDelete){
                                        if (!surveyStudent2.getSurveyStudentID().equals(surveyStudent1.getSurveyStudentID())){
                                            toRemove.add(surveyStudent2);
                                        }
                                        if (surveyStudentDataSetDelete.size() == 1){
                                            if (surveyStudent2.getSurveyStudentID().equals(surveyStudent1.getSurveyStudentID())){
                                                toRemove.add(surveyStudent2);
                                            }
                                        }
                                    }
                                }
                                surveyStudentDataSet.removeAll(toRemove);
                                toRemove = new ArrayList<>();
                                surveyStudentDataSetDelete = new ArrayList<>();
                                surveyStudentList = new ArrayList<>();
                                surveyStudentDataSetSelected = new ArrayList<>();
                                notifyDataChanges();
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

        surveyDataOkayButton = (Button) v.findViewById(R.id.fragment_survey_data_okay_button);
        surveyDataOkayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                final Context context = surveyDataOkayButton.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm making changes to " + mSurvey.getSurveySubject() + "?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                ref = FirebaseDatabase.getInstance().getReference().child("survey").child(mSurvey.getSurveyUID()).child("student");

                                onProgressBar();

                                for (SurveyStudent surveyStudent1 : surveyStudentOnline) {
                                    boolean surveyStudentChecker = false;
                                    for (SurveyStudent surveyStudent2 : surveyStudentDataSet){
                                        if (surveyStudent1.getSurveyStudentID().equals(surveyStudent2.getSurveyStudentID())){
                                            surveyStudentChecker = true;
                                        }
                                    }
                                    if(!surveyStudentChecker){
                                        ref.child(surveyStudent1.getSurveyStudentUID()).removeValue();
                                    }
                                }
                                for (SurveyStudent surveyStudent1 : surveyStudentDataSet) {
                                        ref.child(surveyStudent1.getSurveyStudentUID()).setValue(surveyStudent1);
                                    }


                                Toast.makeText(context, "Changes applied to " + mSurvey.getSurveySubject(), Toast.LENGTH_SHORT).show();
                                refreshAdapter(mSurvey);
                                offProgressBar();
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

        surveyCancelButton = (Button) v.findViewById(R.id.fragment_survey_data_cancel_button);
        surveyCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getActivity().onBackPressed();
            }
        });

        respondOverview = (LinearLayout) v.findViewById(R.id.survey_fragment_linear_layout_respond_overview);
        subjectiveRespond = (LinearLayout) v.findViewById(R.id.survey_fragment_linear_layout_subjective_response);
        
        surveyRespondOverviewButton = (Button) v.findViewById(R.id.survey_fragment_respond_overview_button);
        surveyRespondOverviewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!respondOverviewTab) {
                    respondOverview.setVisibility(View.VISIBLE);
                    respondOverviewTab = true;
                    return;
                }
                else{
                    respondOverview.setVisibility(View.GONE);
                    respondOverviewTab = false;
                }
            }
        });
        surveySubjectiveRespondButton = (Button) v.findViewById(R.id.survey_fragment_subjective_respond_button);
        surveySubjectiveRespondButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!subjectiveRespondTab) {
                    subjectiveRespond.setVisibility(View.VISIBLE);
                    subjectiveRespondTab = true;
                    return;
                }
                else{
                    subjectiveRespond.setVisibility(View.GONE);
                    subjectiveRespondTab = false;
                }
            }
        });

        surveyQuestionR1 = (TextView) v.findViewById(R.id.fragment_survey_queston_rating1);
        surveyQuestionR1.setText("A1 : " + mSurvey.getSurveyQ1() + "\nA5 : " + mSurvey.getSurveyQ5() + "\nA9 : " + mSurvey.getSurveyQ9() + "\n\nB1 : "+ mSurvey.getSurveyQ11() +"\nB5 : "+ mSurvey.getSurveyQ15());

        surveyQuestionR2 = (TextView) v.findViewById(R.id.fragment_survey_queston_rating2);
        surveyQuestionR2.setText("A2   : " + mSurvey.getSurveyQ2() + "\nA6   : " + mSurvey.getSurveyQ6() + "\nA10 : " + mSurvey.getSurveyQ10() + "\n\nB2 : "+ mSurvey.getSurveyQ12() +"\nB6 : "+ mSurvey.getSurveyQ16());

        surveyQuestionR3 = (TextView) v.findViewById(R.id.fragment_survey_queston_rating3);
        surveyQuestionR3.setText("A3 : " + mSurvey.getSurveyQ3() + "\nA7 : " + mSurvey.getSurveyQ7() +"\n" + "\n\nB3 : "+ mSurvey.getSurveyQ13() +"\nB7 : "+ mSurvey.getSurveyQ17());

        surveyQuestionR4 = (TextView) v.findViewById(R.id.fragment_survey_queston_rating4);
        surveyQuestionR4.setText("A4 : " + mSurvey.getSurveyQ4() + "\nA8 : " + mSurvey.getSurveyQ8() + "\n"+ "\n\nB4 : "+ mSurvey.getSurveyQ14() +"\nB8 : "+ mSurvey.getSurveyQ18());

        surveyTotalRating1 = (TextView) v.findViewById(R.id.fragment_survey_total_rating1);
        surveyTotalRating1.setText("1: " + mSurvey.getSurveyRatingScale1());

        surveyTotalRating2 = (TextView) v.findViewById(R.id.fragment_survey_total_rating2);
        surveyTotalRating2.setText("2: " + mSurvey.getSurveyRatingScale2());

        surveyTotalRating3 = (TextView) v.findViewById(R.id.fragment_survey_total_rating3);
        surveyTotalRating3.setText("3: " + mSurvey.getSurveyRatingScale3());

        surveyTotalRating4 = (TextView) v.findViewById(R.id.fragment_survey_total_rating4);
        surveyTotalRating4.setText("4: " + mSurvey.getSurveyRatingScale4());

        surveyTotalRating5 = (TextView) v.findViewById(R.id.fragment_survey_total_rating5);
        surveyTotalRating5.setText("5: " + mSurvey.getSurveyRatingScale5());

        //return view
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_STUDENT_RETRIEVE){
            if(data == null){
                return;
            }
            Bundle args = data.getBundleExtra("BUNDLE");
            surveyStudentList = (ArrayList<Student>)args.getSerializable(EXTRA_STUDENT_PICK_LIST);
            for (Student student : surveyStudentList) {
                SurveyStudent surveyStudent = new SurveyStudent(student.getStudentID(), student.getStudentName(), false, null, student.getStudentUid());
                surveyStudentDataSetSelected.add(surveyStudent);
            }
            /*for (SurveyStudent surveyStudent1 : surveyStudentDataSet) {
                boolean surveyStudentChecker = false;
                for (SurveyStudent surveyStudent2 : surveyStudentDataSetSelected){
                    if (surveyStudent1.getSurveyStudentID() == surveyStudent2.getSurveyStudentID()){
                        surveyStudentChecker = true;
                    }
                }
                if(!surveyStudentChecker){
                    surveyStudentDataSet.remove(surveyStudent1);
                }
            }*/
            for (SurveyStudent surveyStudent1 : surveyStudentDataSetSelected){
                boolean surveyStudentChecker = false;
                for (SurveyStudent surveyStudent2 : surveyStudentDataSet){
                    if (surveyStudent1.getSurveyStudentID() == (surveyStudent2.getSurveyStudentID())){
                        surveyStudentChecker = true;
                    }
                }
                if(!surveyStudentChecker){
                    surveyStudentDataSet.add(surveyStudent1);
                }
            }
        }

        studentListRecyclerView.setAdapter(studentListAdapter);
    }

    private Survey getSurvey(String uid) {
        for (Survey survey : surveyData) {
            if (survey.getSurveyUID().equals(uid)) {
                return survey;
            }
        }
        return null;
    }

    public void refreshAdapter(Survey changedSurvey) {

        for (Survey survey : surveyData) {
            if (survey.getSurveyUID().equals(changedSurvey.getSurveyUID())) {
                survey = changedSurvey;
                break;
            }
        }
        RecyclerSurveyTabAdapter.SurveyArrayListUpdate(surveyData);
    }

    //Retrieve survey students
    public void FirebaseSurveyStudentDataRetrieval(){
        //String key = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference().child("survey").child(mSurvey.getSurveyUID()).child("student");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    boolean found = false;
                    for (SurveyStudent Surveystudent : surveyStudentOnline) {
                        if (Surveystudent.getSurveyStudentUID() == postSnapshot.getValue(SurveyStudent.class).getSurveyStudentUID()) {
                            found = true;
                        }
                    }
                    if (!found){
                        surveyStudentOnline.add(postSnapshot.getValue(SurveyStudent.class));
                    }}
                if(surveyStudentOnline.size() == snapshot.getChildrenCount()) {
                    offProgressBar();
                    notifyDataChanges();
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
        surveyStudentDataSet = surveyStudentOnline;
        Collections.sort(surveyStudentDataSet, new Comparator<SurveyStudent>() {
            @Override
            public int compare(SurveyStudent surveyStudent1, SurveyStudent surveyStudent2){
                return surveyStudent1.getSurveyStudentID().compareTo(surveyStudent2.getSurveyStudentID());
            }
        });
        Collections.reverse(surveyStudentDataSet);
    }

    public static void onProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void offProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

    //Recycler View methods
    android.support.v7.widget.RecyclerView.OnScrollListener onScrollListener = new android.support.v7.widget.RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(android.support.v7.widget.RecyclerView RecyclerView, int newState) {
            super.onScrollStateChanged(RecyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(android.support.v7.widget.RecyclerView RecyclerView, int dx, int dy) {
            super.onScrolled(RecyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };

    public static void notifyDataChanges() {
        studentListAdapter.notifyDataSetChanged();
    }
    //End of Recycler View methods

    @Override
    public void onResume() {
        super.onResume();
       notifyDataChanges();
    }
}
