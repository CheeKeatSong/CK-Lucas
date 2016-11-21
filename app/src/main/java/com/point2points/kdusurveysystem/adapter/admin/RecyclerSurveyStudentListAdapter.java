package com.point2points.kdusurveysystem.adapter.admin;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.datamodel.SurveyStudent;

import java.util.ArrayList;

import static com.point2points.kdusurveysystem.Fragment.SurveyFragment.surveyStudentDataSetDelete;

public class RecyclerSurveyStudentListAdapter extends RecyclerView.Adapter<RecyclerSurveyStudentListAdapter.SimpleViewHolder> {

    private static final String EXTRA_STUDENT_PICK_LIST = "com.point2points.kdusurveysystem.student_pick_list";

    public static ArrayList<SurveyStudent> surveyStudentDataSet = new ArrayList<>();

    private Context mContext;

    private static Activity mActivity;

    public class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView textViewSurveyStudentName;
        TextView textViewSurveyStudentStatus;
        TextView textViewSurveyUid;
        CheckBox checkBoxSurvey;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            textViewSurveyStudentName = (TextView) itemView.findViewById(R.id.survey_fragment_student_list_item_name);
            textViewSurveyStudentStatus = (TextView) itemView.findViewById(R.id.survey_fragment_student_list_item_status);
            textViewSurveyUid = (TextView) itemView.findViewById(R.id.survey_fragment_student_list_item_uid);
            checkBoxSurvey = (CheckBox)itemView.findViewById(R.id.survey_fragment_student_list_checkbox);
        }
    }

    public RecyclerSurveyStudentListAdapter(Context context, ArrayList<SurveyStudent> surveyStudentsDataset) {
        this.mContext = context;
        mActivity = (Activity) mContext;
        this.surveyStudentDataSet = surveyStudentsDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_fragment_student_list_recycler_view_items, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new SimpleViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int position) {
        final SurveyStudent item = this.surveyStudentDataSet.get(position);
        final String studentName = (item.getSurveyStudentName()).substring(0, 1).toUpperCase() + (item.getSurveyStudentName()).substring(1);

        viewHolder.checkBoxSurvey.setChecked(false);
        viewHolder.checkBoxSurvey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(item.getSurveyStudentName(),"add");
                    surveyStudentDataSetDelete.add(item);
                    //studentsID.add(item.getStudentID());
                    //studentsName.add(item.getStudentName());
                } else {
                    Log.e(item.getSurveyStudentName(),"delete");
                    surveyStudentDataSetDelete.remove(item);
                    //studentsID.remove(item.getStudentID());
                    //studentsName.remove(item.getStudentName());
                }
            }
        });

        viewHolder.textViewSurveyStudentName.setText(item.getSurveyStudentID() + "  " + studentName);
        if (!item.isSurveyStudentStatus()){
            viewHolder.textViewSurveyStudentStatus.setText("PENDING");
        }
        else if(item.isSurveyStudentStatus()) {
            viewHolder.textViewSurveyStudentStatus.setText("RESPONDED");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return surveyStudentDataSet.size();
    }
}
