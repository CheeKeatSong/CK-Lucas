package com.point2points.kdusurveysystem.adapter.admin;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.datamodel.SurveyStudent;

import java.util.ArrayList;

public class RecyclerSubjectiveRespondAdapter extends RecyclerView.Adapter<RecyclerSubjectiveRespondAdapter.SimpleViewHolder> {

    public static ArrayList<SurveyStudent> surveyStudentDataSet = new ArrayList<>();
    public static ArrayList<String> subjectiveResponds = new ArrayList<>();

    private Context mContext;

    private static Activity mActivity;

    private int numbering = 1;

    public class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView textViewSurveySubjectiveRespondNumber;
        TextView textViewSurveySubjectiveRespond;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            textViewSurveySubjectiveRespondNumber = (TextView) itemView.findViewById(R.id.survey_fragment_recycler_view_subjective_respond_number);
            textViewSurveySubjectiveRespond = (TextView) itemView.findViewById(R.id.survey_fragment_recycler_view_subjective_respond);
        }
    }

    public RecyclerSubjectiveRespondAdapter(Context context, ArrayList<SurveyStudent> surveyStudentsDataset) {
        this.mContext = context;
        mActivity = (Activity) mContext;
        this.surveyStudentDataSet = surveyStudentsDataset;
        for (SurveyStudent surveyStudent : surveyStudentsDataset) {
            if (surveyStudent.getSubjectiveRespond() != null) {
                if (surveyStudent.getSubjectiveRespond().length() > 0) {
                    subjectiveResponds.add(surveyStudent.getSubjectiveRespond());
                }else{}
            }else{}
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_fragment_subjective_respond_recycler_view_items, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new SimpleViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int position) {
        final String respond = subjectiveResponds.get(position);

                viewHolder.textViewSurveySubjectiveRespondNumber.setText(numbering + ". ");
                numbering++;
                viewHolder.textViewSurveySubjectiveRespond.setText(respond);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return subjectiveResponds.size();
    }
}
