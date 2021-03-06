package com.point2points.kdusurveysystem.adapter.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.Fragment.SurveyFragmentActivity;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewSurvey;
import com.point2points.kdusurveysystem.adapter.util.RecyclerLetterIcon;
import com.point2points.kdusurveysystem.datamodel.Survey;
import com.point2points.kdusurveysystem.datamodel.SurveyStudent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecyclerSurveyTabAdapter extends RecyclerSwipeAdapter<RecyclerSurveyTabAdapter.SimpleViewHolder> {

    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    static Query query;

    private static int total, completion;

    public class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewSurveySubjectName;
        TextView textViewSurveyStatus;
        TextView textViewSurveySubjectCode;
        TextView textViewSurveySubjectSchool;
        TextView textViewSurveyLecturer;
        TextView textViewSurveyUid;
        ImageButton buttonDelete;
        ImageButton buttonEdit;
        ImageView letterimage;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewSurveySubjectName = (TextView) itemView.findViewById(R.id.survey_name_text_view);
            textViewSurveyStatus = (TextView) itemView.findViewById(R.id.survey_status_text_view);
            textViewSurveySubjectSchool = (TextView) itemView.findViewById(R.id.survey_subject_school_text_view);
            textViewSurveyLecturer = (TextView) itemView.findViewById(R.id.survey_lecturer_text_view);
            textViewSurveySubjectCode = (TextView) itemView.findViewById(R.id.survey_subject_code_text_view);
            textViewSurveyUid = (TextView) itemView.findViewById(R.id.survey_uid_text_view);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.delete);
            buttonEdit = (ImageButton) itemView.findViewById(R.id.edit);
            letterimage = (ImageView) itemView.findViewById(R.id.letter_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Double Tap to Edit the data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Context mContext;
    private static ArrayList<Survey> surveys = new ArrayList<>();
    public static ArrayList<Survey> SurveyDataset = new ArrayList<>();
    private static ArrayList<SurveyStudent> surveyStudent = new ArrayList<>();

    public RecyclerSurveyTabAdapter(Context context) {
        this.mContext = context;
        FirebaseSurveyDataRetrieval();
    }

    public static void sortingData(int sortoption) {

        //FirebaseSurveyDataRetrieval();

        switch (sortoption) {
            case 1:
                Collections.sort(SurveyDataset, new Comparator<Survey>() {
                    @Override
                    public int compare(Survey survey1, Survey survey2) {
                        return (survey1.getSurveySubject().substring(0, 1).toUpperCase() + survey1.getSurveySubject().substring(1)).compareTo(survey2.getSurveySubject().substring(0, 1).toUpperCase() + survey2.getSurveySubject().substring(1));
                    }
                });

                break;
            case 2:
                Collections.sort(SurveyDataset, new Comparator<Survey>() {
                    @Override
                    public int compare(Survey survey1, Survey survey2) {
                        return (survey1.getSurveySubject().substring(0, 1).toUpperCase() + survey1.getSurveySubject().substring(1)).compareTo(survey2.getSurveySubject().substring(0, 1).toUpperCase() + survey2.getSurveySubject().substring(1));
                    }
                });
                Collections.reverse(SurveyDataset);
                break;
            case 3:
                Collections.sort(SurveyDataset, new Comparator<Survey>() {
                    @Override
                    public int compare(Survey survey1, Survey survey2) {
                        return survey1.getSurveyDate().compareTo(survey2.getSurveyDate());
                    }
                });
                Collections.reverse(SurveyDataset);
                break;
            case 4:
                Collections.sort(SurveyDataset, new Comparator<Survey>() {
                    @Override
                    public int compare(Survey survey1, Survey survey2) {
                        return survey1.getSurveyDate().compareTo(survey2.getSurveyDate());
                    }
                });
                break;
            default:
                break;
        }
        RecyclerViewSurvey.notifyDataChanges();
    }

    public static void SurveyArrayListUpdate(ArrayList updatedSurveys) {
        surveys = updatedSurveys;
        RecyclerViewSurvey.notifyDataChanges();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_recycler_view_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        final Survey item = SurveyDataset.get(position);
        String surveySubjectName = item.getSurveySubject();
        String surveySubjectCode = item.getSurveySubjectCode();
        String surveySubjectCategory = item.getSurveySubjectCategory();
        String surveyLecturer = item.getSurveyLecturer();
        String surveySchoolShort = item.getSurveySchoolShort();

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Swing).duration(1000).delay(100).playOn(layout.findViewById(R.id.edit));
                YoYo.with(Techniques.Swing).duration(1000).delay(100).playOn(layout.findViewById(R.id.delete));
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.textViewSurveySubjectName.getText().toString());
                Intent intent = SurveyFragmentActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewSurveyUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Context context = view.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm deleting " + item.getSurveySubject() + "?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ref.child(viewHolder.textViewSurveyUid.getText().toString()).removeValue();
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                surveys.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, SurveyDataset.size());
                                mItemManger.closeAllItems();
                                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewSurveySubjectName.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                                SurveyDataset = surveys;
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
        viewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SurveyFragmentActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewSurveyUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
            }
        });

        TextDrawable drawable = RecyclerLetterIcon.GenerateRecyclerLetterIcon(surveySubjectName, 64);

        viewHolder.letterimage.setImageDrawable(drawable);
        viewHolder.textViewSurveySubjectName.setText(surveySubjectName);
        viewHolder.textViewSurveyStatus.setText("Attendance: " + item.getSurveyAttendance() + "/" + item.getSurveyTotalAttendance());
        //viewHolder.textViewSurveyStatus.setText("Attendance: " + getAttendance(studentsAttendance, position) + "/" + getTotalStudents(totalStudents, position));
        viewHolder.textViewSurveySubjectSchool.setText(surveySchoolShort);
        viewHolder.textViewSurveyLecturer.setText(surveyLecturer);
        viewHolder.textViewSurveySubjectCode.setText(surveySubjectCode + "\t(" + surveySubjectCategory + ")");
        viewHolder.textViewSurveyUid.setText(item.getSurveyUID());
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return SurveyDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        SurveyDataset = new ArrayList<Survey>();

        if (charText.length() == 0) {
            SurveyDataset = surveys;
        } else {
            for (Survey survey : surveys) {
                if (survey.getSurveySubject().toLowerCase(Locale.getDefault()).contains(charText)
                        || survey.getSurveySubjectCode().toLowerCase(Locale.getDefault()).contains(charText)
                        || survey.getSurveySubjectCategory().toLowerCase(Locale.getDefault()).contains(charText)
                        || survey.getSurveySchool().toLowerCase(Locale.getDefault()).contains(charText)
                        || survey.getSurveySchoolShort().toLowerCase(Locale.getDefault()).contains(charText)
                        || survey.getSurveySubject().toLowerCase(Locale.getDefault()).contains(charText)
                        || survey.getSurveyLecturer().toLowerCase(Locale.getDefault()).contains(charText)
                        || survey.getSurveyLecturerId().toLowerCase(Locale.getDefault()).contains(charText)) {
                    SurveyDataset.add(survey);
                }
            }
            RecyclerViewSurvey.notifyDataChanges();
        }
    }

    public static void FirebaseSurveyDataRetrieval() {
        RecyclerViewSurvey.onProgressBar();

        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("survey");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {

            // Don't need to get attendance count. Attendance count is recorded/updated in its appropriate node data value when changes occur.
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    boolean found = false;
                    for (Survey survey : surveys) {
                        if (survey.getSurveyUID() == postSnapshot.getValue(Survey.class).getSurveyUID()) {
                            found = true;
                        }
                    }
                    if (!found){
                        surveys.add(postSnapshot.getValue(Survey.class));
                        Log.e("Get Data", (postSnapshot.getValue(Survey.class).getSurveySubject()));
                    }}
                if (surveys.size() == snapshot.getChildrenCount()){

                    //copy and sort data
                    SurveyDataset = surveys;
                    Collections.sort(SurveyDataset, new Comparator<Survey>() {
                        @Override
                        public int compare(Survey survey1, Survey survey2) {
                            return survey1.getSurveyDate().compareTo(survey2.getSurveyDate());
                        }
                    });
                    Collections.reverse(SurveyDataset);

                    RecyclerViewSurvey.offProgressBar();
                    RecyclerViewSurvey.notifyDataChanges();
                }
            }

            // Old: Get correct attendance count by counting number of students participating and their completion status.
            /*@Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                    boolean found = false;
                    total = 0;
                    completion = 0;
                    for (Survey survey : surveys) {
                        if (survey.getSurveyUID() == postSnapshot.getValue(Survey.class).getSurveyUID()) {
                            found = true;
                        }
                    }

                    if (!found) {
                        Log.e("DO I RUN", "YES");
                        query = ref.child(postSnapshot.getValue(Survey.class).getSurveyUID()).child("student");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.e("DO I RUN 2", "YES 2");
                                total = (int) dataSnapshot.getChildrenCount();

                                for (final DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                                    if (postSnapshot2.getValue(SurveyStudent.class).isSurveyStudentStatus()) {
                                        completion++;
                                    }
                                }
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("survey").child(postSnapshot.getValue(Survey.class).getSurveyUID());
                                Map<String, Object> updateSurvey = new HashMap<String, Object>();

                                updateSurvey.put("surveyTotalAttendance", total);
                                updateSurvey.put("surveyAttendance", completion);

                                mDatabase.updateChildren(updateSurvey);

                                Survey surveyAdd;
                                surveyAdd = postSnapshot.getValue(Survey.class);
                                surveyAdd.setSurveyAttendance(completion);
                                surveyAdd.setSurveyTotalAttendance(total);
                                Log.e("Get Data", "Completion: " + completion + " | Total: " + total);
                                surveys.add(surveyAdd);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("The read failed: ", databaseError.getMessage());
                            }
                        });
                        Log.e("Get Data", (postSnapshot.getValue(Survey.class).getSurveySubject()));
                    }

                }
                if (surveys.size() >= snapshot.getChildrenCount()) {
                    RecyclerViewSurvey.offProgressBar();
                    RecyclerViewSurvey.notifyDataChanges();
                }
            }*/

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

}

