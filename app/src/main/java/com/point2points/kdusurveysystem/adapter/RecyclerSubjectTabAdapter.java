package com.point2points.kdusurveysystem.adapter;

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
import com.point2points.kdusurveysystem.Fragment.SubjectFragmentPagerActivity;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewSubject;
import com.point2points.kdusurveysystem.adapter.util.RecyclerLetterIcon;
import com.point2points.kdusurveysystem.admin.AdminToolbarDrawer;
import com.point2points.kdusurveysystem.datamodel.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class RecyclerSubjectTabAdapter extends RecyclerSwipeAdapter<RecyclerSubjectTabAdapter.SimpleViewHolder> {

    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    static Query query;

    public static boolean subjectRetrieval = false;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, RecyclerViewSubject.class);
        subjectRetrieval = true;
        return intent;
    }

    public class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewSubjectName;
        TextView textViewSubjectCategory;
        //TextView textViewSubjectDepartment;
        TextView textViewSubjectCode;
        TextView textViewSubjectSchool;
        TextView textViewSubjectUid;
        ImageButton buttonDelete;
        ImageButton  buttonEdit;
        ImageView letterimage;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewSubjectName = (TextView) itemView.findViewById(R.id.subject_name_text_view);
            textViewSubjectCategory = (TextView) itemView.findViewById(R.id.subject_category_text_view);
            //textViewSubjectDepartment = (TextView) itemView.findViewById(R.id.subject_department_text_view);
            textViewSubjectSchool = (TextView) itemView.findViewById(R.id.subject_school_text_view);
            textViewSubjectUid = (TextView) itemView.findViewById(R.id.subject_uid_text_view);
            textViewSubjectCode = (TextView)itemView.findViewById(R.id.subject_code_text_view);
            buttonDelete = (ImageButton ) itemView.findViewById(R.id.delete);
            buttonEdit = (ImageButton ) itemView.findViewById(R.id.edit);
            letterimage = (ImageView) itemView.findViewById(R.id.letter_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //notifyItemChanged(selectedPos);
                    //selectedPos = getLayoutPosition();
                    //notifyItemChanged(selectedPos);
                    if (!subjectRetrieval) {    // What do these do?
                        Toast.makeText(view.getContext(), "Double Tap to Edit the data", Toast.LENGTH_SHORT).show();
                    }
                    else if (subjectRetrieval) {
                        Toast.makeText(view.getContext(), "Double Tap to select the data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private Context mContext;
    private static ArrayList<Subject> subjects = new ArrayList<>();
    public static ArrayList<Subject> SubjectDataset = new ArrayList<>();

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);
    public RecyclerSubjectTabAdapter(Context context) {
        this.mContext = context;
        FirebaseSubjectDataRetrieval();
    }

    public static void sortingData(int sortoption){

        FirebaseSubjectDataRetrieval();

        switch (sortoption) {
            case 1:
                Collections.sort(SubjectDataset, new Comparator<Subject>() {
                    @Override
                    public int compare(Subject subject1, Subject subject2){
                        return (subject1.getSubjectName().substring(0, 1).toUpperCase() + subject1.getSubjectName().substring(1)).compareTo(subject2.getSubjectName().substring(0, 1).toUpperCase() + subject2.getSubjectName().substring(1));
                    }
                });

                break;
            case 2:
                Collections.sort(SubjectDataset, new Comparator<Subject>() {
                    @Override
                    public int compare(Subject subject1, Subject subject2){
                        return (subject1.getSubjectName().substring(0, 1).toUpperCase() + subject1.getSubjectName().substring(1)).compareTo(subject2.getSubjectName().substring(0, 1).toUpperCase() + subject2.getSubjectName().substring(1));
                    }
                });
                Collections.reverse(SubjectDataset);
                break;

            case 3:
                Collections.sort(SubjectDataset, new Comparator<Subject>() {
                    @Override
                    public int compare(Subject subject1, Subject subject2){
                        return subject1.getDate().compareTo(subject2.getDate());
                    }
                });
                Collections.reverse(SubjectDataset);
                break;
            case 4:
                Collections.sort(SubjectDataset, new Comparator<Subject>() {
                    @Override
                    public int compare(Subject subject1, Subject subject2){
                        return subject1.getDate().compareTo(subject2.getDate());
                    }
                });
                break;
            default:
                break;
        }
        RecyclerViewSubject.notifyDataChanges();
    }

    public static void SubjectArrayListUpdate(ArrayList updatedSubjects) {
        subjects = updatedSubjects;
        RecyclerViewSubject.notifyDataChanges();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_recycler_view_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        //viewHolder.itemView.setSelected(selectedPos == position);

        final Subject item = SubjectDataset.get(position);
        final String subjectName = (item.subjectName).substring(0, 1).toUpperCase() + (item.subjectName).substring(1);
        String subjectCategory = item.subjectCategory;
        //String subjectDepartment = item.subjectDepartment;
        String subjectSchool = item.subjectSchool;
        String subjectUid = item.subjectUid;
        String subjectCode = item.subjectCode;

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
                //if(!subjectRetrieval) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.textViewSubjectName.getText().toString());
                    Intent intent = SubjectFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewSubjectUid.getText().toString());
                    viewHolder.swipeLayout.getContext().startActivity(intent);
                //}
                /*else if(subjectRetrieval) {
                    String subjectName = viewHolder.textViewSubjectName.getText().toString();
                    String subjectNameShort = viewHolder.textViewSubjectNameShort.getText().toString();
                    AdminToolbarDrawer.getSubject(subjectName, subjectNameShort);
                    subjectRetrieval = false;
                    RecyclerViewSubject.closeRecyclerViewSubject();
                }*/
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Context context = view.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm deleting " + item.getSubjectName() + "?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                //Log.d("Deletion", viewHolder.textViewUid.getText().toString());
                                ref.child(viewHolder.textViewSubjectUid.getText().toString()).removeValue();
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                subjects.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, SubjectDataset.size());
                                mItemManger.closeAllItems();
                                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewSubjectName.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                                SubjectDataset = subjects;
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
        viewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SubjectFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewSubjectUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
            }
        });

        //String subjectNameReformatted = subjectName.substring(subjectName.lastIndexOf("of") +3);
        //TextDrawable drawable = RecyclerLetterIcon.GenerateRecyclerLetterIcon(subjectNameReformatted);
        TextDrawable drawable = RecyclerLetterIcon.GenerateRecyclerLetterIcon(subjectName);

        viewHolder.letterimage.setImageDrawable(drawable);
        viewHolder.textViewSubjectName.setText(subjectName);
        viewHolder.textViewSubjectCategory.setText(subjectCategory);
        //viewHolder.textViewSubjectDepartment.setText(subjectDepartment);
        viewHolder.textViewSubjectSchool.setText(subjectSchool);
        viewHolder.textViewSubjectUid.setText(subjectUid);
        viewHolder.textViewSubjectCode.setText(subjectCode);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return SubjectDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        SubjectDataset = new ArrayList<Subject>();

        if (charText.length() == 0) {
            SubjectDataset = subjects;
        }
        else
        {
            for (Subject subject : subjects)
            {
                if (subject.getSubjectName().toLowerCase(Locale.getDefault()).contains(charText)
                        || subject.getSubjectSchoolShort().toLowerCase(Locale.getDefault()).contains(charText)
                        || subject.getSubjectCategory().toLowerCase(Locale.getDefault()).contains(charText)
                        || subject.getSubjectCode().toLowerCase(Locale.getDefault()).contains(charText)
                        || subject.getSubjectSchool().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    SubjectDataset.add(subject);
                }
            }
        }
        RecyclerViewSubject.notifyDataChanges();
    }

    public static void FirebaseSubjectDataRetrieval(){
        //String key = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("subject");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    boolean found = false;
                    for (Subject subject : subjects) {
                        if (subject.getSubjectUid() == postSnapshot.getValue(Subject.class).getSubjectUid()) {
                            found = true;
                        }
                    }
                    if (!found){
                        subjects.add(postSnapshot.getValue(Subject.class));
                        Log.e("Get Data", (postSnapshot.getValue(Subject.class).getSubjectName()));
                    }}
                if (subjects.size() == snapshot.getChildrenCount()){
                    RecyclerViewSubject.offProgressBar();
                    RecyclerViewSubject.notifyDataChanges();
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
        SubjectDataset = subjects;
        Collections.sort(SubjectDataset, new Comparator<Subject>() {
            @Override
            public int compare(Subject subject1, Subject subject2){
                return subject1.getDate().compareTo(subject2.getDate());
            }
        });
        Collections.reverse(SubjectDataset);
    }

}

