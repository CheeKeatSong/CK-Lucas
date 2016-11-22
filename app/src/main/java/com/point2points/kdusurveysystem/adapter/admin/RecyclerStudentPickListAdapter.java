package com.point2points.kdusurveysystem.adapter.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewStudentPickList;
import com.point2points.kdusurveysystem.adapter.util.RecyclerLetterIcon;
import com.point2points.kdusurveysystem.admin.AdminToolbarDrawer;
import com.point2points.kdusurveysystem.datamodel.Student;
import com.point2points.kdusurveysystem.datamodel.SurveyStudent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class RecyclerStudentPickListAdapter extends RecyclerView.Adapter<RecyclerStudentPickListAdapter.SimpleViewHolder> {

    private static final String EXTRA_STUDENT_PICK_LIST = "com.point2points.kdusurveysystem.student_pick_list";

    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    static Query query;

    private Context mContext;
    private static ArrayList<Student> studentsList = new ArrayList<>();
    public static ArrayList<Student> studentListDataset = new ArrayList<>();
    public static ArrayList<Student> surveyStudentList = new ArrayList<>();
    public static ArrayList<SurveyStudent> surveyStudentCheckList = new ArrayList<>();
    //private static List<String> studentsID = new ArrayList<String>();
    //private static List<String> studentsName = new ArrayList<String>();

    public static boolean studentRetrieval = false;

    private static Activity mActivity;

    public static Intent newIntent(Context packageContext, ArrayList<SurveyStudent> Students) {
        surveyStudentCheckList = Students;
        surveyStudentList = new ArrayList<>();
        Intent intent = new Intent(packageContext, RecyclerViewStudentPickList.class);
        studentRetrieval = true;
        return intent;
    }

    private static void setSurveyStudentList(ArrayList<Student> surveyStudentList) {
        AdminToolbarDrawer.tabIdentifier = AdminToolbarDrawer.tabIdentifierMutex;
        Intent data = new Intent();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_STUDENT_PICK_LIST, (Serializable) surveyStudentList);
        data.putExtra("BUNDLE",args);
        mActivity.setResult(Activity.RESULT_OK, data);
        mActivity.finish();
    }

    public class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView textViewSurveyStudentName;
        TextView textViewSurveySchool;
        TextView textViewSurveyUid;
        TextView textViewSurveyCategory;
        CheckBox checkBoxSurvey;
        ImageView letterIcon;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            letterIcon = (ImageView) itemView.findViewById(R.id.letter_icon);
            textViewSurveyStudentName = (TextView) itemView.findViewById(R.id.survey_student_pick_list_item_name);
            textViewSurveySchool = (TextView) itemView.findViewById(R.id.survey_student_pick_list_item_school);
            textViewSurveyUid = (TextView) itemView.findViewById(R.id.survey_student_pick_list_item_uid);
            textViewSurveyCategory = (TextView) itemView.findViewById(R.id.survey_student_pick_list_item_category);
            checkBoxSurvey = (CheckBox) itemView.findViewById(R.id.survey_student_pick_list_checkbox);
        }
    }

    public RecyclerStudentPickListAdapter(Context context) {
        this.mContext = context;
        mActivity = (Activity) mContext;
        FirebaseStudentDataRetrieval();
    }

    public static void sortingData(int sortoption) {

        FirebaseStudentDataRetrieval();

        switch (sortoption) {
            case 1:
                Collections.sort(studentListDataset, new Comparator<Student>() {
                    @Override
                    public int compare(Student student1, Student student2) {
                        return (student1.getStudentName().substring(0, 1).toUpperCase() + student1.getStudentName().substring(1)).compareTo(student2.getStudentName().substring(0, 1).toUpperCase() + student2.getStudentName().substring(1));
                    }
                });

                break;
            case 2:
                Collections.sort(studentListDataset, new Comparator<Student>() {
                    @Override
                    public int compare(Student student1, Student student2) {
                        return (student1.getStudentName().substring(0, 1).toUpperCase() + student1.getStudentName().substring(1)).compareTo(student2.getStudentName().substring(0, 1).toUpperCase() + student2.getStudentName().substring(1));
                    }
                });
                Collections.reverse(studentListDataset);
                break;

            case 3:
                Collections.sort(studentListDataset, new Comparator<Student>() {
                    @Override
                    public int compare(Student student1, Student student2) {
                        return student1.getDate().compareTo(student2.getDate());
                    }
                });
                Collections.reverse(studentListDataset);
                break;
            case 4:
                Collections.sort(studentListDataset, new Comparator<Student>() {
                    @Override
                    public int compare(Student student1, Student student2) {
                        return student1.getDate().compareTo(student2.getDate());
                    }
                });
                break;
            default:
                break;
        }
        RecyclerViewStudentPickList.notifyDataChanges();
    }

    public static void StudentArrayListUpdate(ArrayList updatedStudents) {
        studentsList = updatedStudents;
        RecyclerViewStudentPickList.notifyDataChanges();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerStudentPickListAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_student_list_recycler_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new SimpleViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int position) {

        final Student item = studentListDataset.get(position);
        final String studentName = (item.studentName).substring(0, 1).toUpperCase() + (item.studentName).substring(1);
        String studentCategory = item.studentCategory;
        String studentUid = item.studentUid;

        //check the boolean status
        for (SurveyStudent surveyStudent : surveyStudentCheckList) {
            if (item.getStudentUid().equals(surveyStudent.getSurveyStudentUID())){
                //viewHolder.checkBoxSurvey.setChecked(true);
                viewHolder.checkBoxSurvey.setEnabled(false);
                //surveyStudentList.add(item);
                    // Method 1: For students already in the survey, mark disabled.
            }
        }

        viewHolder.checkBoxSurvey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    surveyStudentList.add(item);
                    //studentsID.add(item.getStudentID());
                    //studentsName.add(item.getStudentName());
                } else {
                    surveyStudentList.remove(item);
                    //studentsID.remove(item.getStudentID());
                    //studentsName.remove(item.getStudentName());
                }
            }
        });

        TextDrawable drawable = RecyclerLetterIcon.GenerateRecyclerLetterIcon(studentName, 54);

        viewHolder.letterIcon.setImageDrawable(drawable);
        viewHolder.textViewSurveyStudentName.setText(item.getStudentID() + " \t" + studentName);
        viewHolder.textViewSurveySchool.setText(item.getStudentSchoolShort());
        viewHolder.textViewSurveyCategory.setText("\t(" + studentCategory + ")");
        viewHolder.textViewSurveyUid.setText(studentUid);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return studentListDataset.size();
    }

    public static void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        studentListDataset = new ArrayList<Student>();

        if (charText.length() == 0) {
            studentListDataset = studentsList;
        } else {
            for (Student student : studentsList) {
                if (student.getStudentName().toLowerCase(Locale.getDefault()).contains(charText)
                        || student.getStudentID().toLowerCase(Locale.getDefault()).contains(charText)
                        || student.getStudentCategory().toLowerCase(Locale.getDefault()).contains(charText)
                        || student.getStudentSchool().toLowerCase(Locale.getDefault()).contains(charText)
                        || student.getStudentSchoolShort().toLowerCase(Locale.getDefault()).contains(charText)) {
                    studentListDataset.add(student);
                }
            }
        }
        RecyclerViewStudentPickList.notifyDataChanges();
    }

    public static void FirebaseStudentDataRetrieval() {
        //String key = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("student");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    boolean found = false;
                    for (Student student : studentsList) {
                        if (student.getStudentUid() == postSnapshot.getValue(Student.class).getStudentUid()) {
                            found = true;
                        }
                    }
                    if (!found) {
                        studentsList.add(postSnapshot.getValue(Student.class));
                        Log.e("Get Data", (postSnapshot.getValue(Student.class).getStudentName()));
                    }
                }
                if (studentsList.size() == snapshot.getChildrenCount()) {
                    RecyclerViewStudentPickList.offProgressBar();
                    RecyclerViewStudentPickList.notifyDataChanges();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
        studentListDataset = studentsList;
        Collections.sort(studentListDataset, new Comparator<Student>() {
            @Override
            public int compare(Student student1, Student student2) {
                return student1.getDate().compareTo(student2.getDate());
            }
        });
        Collections.reverse(studentListDataset);
    }

    public static void forwardPickedSurveyStudent() {
        studentRetrieval = false;
        setSurveyStudentList(surveyStudentList);

        // Method 2: Remove duplicates via hash set. Doesn't work, no duplicates at this point of the List.
        //setSurveyStudentList(removeDuplicates(surveyStudentList));
    }

    /*public static ArrayList removeDuplicates(ArrayList list) {
        Log.e("SIZE 1: ", String.valueOf(list.size()));
        HashSet<Student> hashSet = new HashSet<Student>();

        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        Log.e("SIZE 2: ", String.valueOf(list.size()));
        return list;
    }*/
}

// Ideas to avoid duplicates:
// 1: Don't allow selecting of exiting data. (Method 1)
// 2: Remove duplicated data before returning list to be added. (Method 2)
// 3: Clear/empty the data set in SurveyFragment before adding to it.

