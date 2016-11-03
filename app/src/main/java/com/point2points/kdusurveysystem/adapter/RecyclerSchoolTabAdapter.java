package com.point2points.kdusurveysystem.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
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
import com.point2points.kdusurveysystem.Fragment.SchoolFragmentPagerActivity;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewSchool;
import com.point2points.kdusurveysystem.adapter.util.RecyclerLetterIcon;
import com.point2points.kdusurveysystem.admin.AdminToolbarDrawer;
import com.point2points.kdusurveysystem.datamodel.School;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class RecyclerSchoolTabAdapter extends RecyclerSwipeAdapter<RecyclerSchoolTabAdapter.SimpleViewHolder> {

    private static final String EXTRA_SCHOOL_NAME = "com.point2points.kdusurveysystem.school_name";
    private static final String EXTRA_SCHOOL_NAME_SHORT = "com.point2points.kdusurveysystem.school_name_short";

    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    static Query query;

    private static Context mContext;
    private static ArrayList<School> schools = new ArrayList<>();
    public static ArrayList<School> SchoolDataset = new ArrayList<>();

    private static Activity mActivity;

    //private int selectedPos;

    public static boolean schoolRetrieval = false;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, RecyclerViewSchool.class);
        schoolRetrieval = true;
        return intent;
    }

    public static String schoolNameRetrieval(Intent result){
        return result.getStringExtra(EXTRA_SCHOOL_NAME);
    }

    public static String schoolNameShortRetrieval(Intent result){
        return result.getStringExtra(EXTRA_SCHOOL_NAME_SHORT);
    }

    private void setSchoolNameAndShort(String schoolName, String schoolNameShort){
        AdminToolbarDrawer.tabIdentifier = 2;
        Intent data = new Intent();
        data.putExtra(EXTRA_SCHOOL_NAME, schoolName);
        data.putExtra(EXTRA_SCHOOL_NAME_SHORT, schoolNameShort);
        mActivity.setResult(Activity.RESULT_OK, data);
        mActivity.finish();
        Log.e("set " ,"school name");
    }

    public class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewSchoolName;
        TextView textViewSchoolNameShort;
        TextView textViewSchoolUid;
        ImageButton buttonDelete;
        ImageButton  buttonEdit;
        ImageView letterimage;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewSchoolName = (TextView) itemView.findViewById(R.id.school_name_text_view);
            textViewSchoolNameShort = (TextView) itemView.findViewById(R.id.school_name_short_text_view);
            textViewSchoolUid = (TextView) itemView.findViewById(R.id.school_uid_text_view);
            buttonDelete = (ImageButton ) itemView.findViewById(R.id.delete);
            buttonEdit = (ImageButton ) itemView.findViewById(R.id.edit);
            letterimage = (ImageView) itemView.findViewById(R.id.letter_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //notifyItemChanged(selectedPos);
                    //selectedPos = getLayoutPosition();
                    //notifyItemChanged(selectedPos);
                   schoolItemOnClickListener(view);
                }
            });
        }
    }

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);
    public RecyclerSchoolTabAdapter(Context context) {
        this.mContext = context;
        mActivity = (Activity) mContext;
        FirebaseSchoolDataRetrieval();
    }

    public static void sortingData(int sortoption){

        FirebaseSchoolDataRetrieval();

        switch (sortoption) {
            case 1:
                Collections.sort(SchoolDataset, new Comparator<School>() {
                    @Override
                    public int compare(School school1, School school2){
                        return (school1.getSchoolName().substring(0, 1).toUpperCase() + school1.getSchoolName().substring(1)).compareTo(school2.getSchoolName().substring(0, 1).toUpperCase() + school2.getSchoolName().substring(1));
                    }
                });

                break;
            case 2:
                Collections.sort(SchoolDataset, new Comparator<School>() {
                    @Override
                    public int compare(School school1, School school2){
                        return (school1.getSchoolName().substring(0, 1).toUpperCase() + school1.getSchoolName().substring(1)).compareTo(school2.getSchoolName().substring(0, 1).toUpperCase() + school2.getSchoolName().substring(1));
                    }
                });
                Collections.reverse(SchoolDataset);
                break;

            case 3:
                Collections.sort(SchoolDataset, new Comparator<School>() {
                    @Override
                    public int compare(School school1, School school2){
                        return school1.getDate().compareTo(school2.getDate());
                    }
                });
                Collections.reverse(SchoolDataset);
                break;
            case 4:
                Collections.sort(SchoolDataset, new Comparator<School>() {
                    @Override
                    public int compare(School school1, School school2){
                        return school1.getDate().compareTo(school2.getDate());
                    }
                });
                break;
            default:
                break;
        }
        RecyclerViewSchool.notifyDataChanges();
    }

    public static void SchoolArrayListUpdate(ArrayList updatedSchools) {
        schools = updatedSchools;
        RecyclerViewSchool.notifyDataChanges();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_recycler_view_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        //viewHolder.itemView.setSelected(selectedPos == position);

        final School item = SchoolDataset.get(position);
        final String schoolName = (item.schoolName).substring(0, 1).toUpperCase() + (item.schoolName).substring(1);
        String schoolNameShort = item.schoolNameShort;
        String schoolUid = item.schoolUid;

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
                if(!schoolRetrieval) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.textViewSchoolName.getText().toString());
                    Intent intent = SchoolFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewSchoolUid.getText().toString());
                    viewHolder.swipeLayout.getContext().startActivity(intent);
                }
                else if(schoolRetrieval) {

                    final Toast toastOnDoubleClick = Toast.makeText(mContext, viewHolder.textViewSchoolName.getText().toString() + " Selected.", Toast.LENGTH_SHORT);
                    toastOnDoubleClick.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toastOnDoubleClick.cancel();
                        }
                    }, 1500);

                    String schoolName = viewHolder.textViewSchoolName.getText().toString();
                    String schoolNameShort = viewHolder.textViewSchoolNameShort.getText().toString();
                    schoolRetrieval = false;
                    setSchoolNameAndShort(schoolName, schoolNameShort);
                }
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Context context = view.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm deleting " + item.getSchoolName() + "?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                //Log.d("Deletion", viewHolder.textViewUid.getText().toString());
                                ref.child(viewHolder.textViewSchoolUid.getText().toString()).removeValue();
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                schools.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, SchoolDataset.size());
                                mItemManger.closeAllItems();
                                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewSchoolName.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                                SchoolDataset = schools;
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
                Intent intent = SchoolFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewSchoolUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
            }
        });

        String schoolNameReformatted = schoolName.substring(schoolName.lastIndexOf("of") +3);
        TextDrawable drawable = RecyclerLetterIcon.GenerateRecyclerLetterIcon(schoolNameReformatted);

        viewHolder.letterimage.setImageDrawable(drawable);
        viewHolder.textViewSchoolName.setText(schoolName);
        viewHolder.textViewSchoolNameShort.setText(schoolNameShort);
        viewHolder.textViewSchoolUid.setText(schoolUid);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return SchoolDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        SchoolDataset = new ArrayList<School>();

        if (charText.length() == 0) {
            SchoolDataset = schools;
        }
        else
        {
            for (School school : schools)
            {
                if (school.getSchoolName().toLowerCase(Locale.getDefault()).contains(charText)
                        || school.getSchoolName().toLowerCase(Locale.getDefault()).contains(charText)
                        || school.getSchoolNameShort().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    SchoolDataset.add(school);
                }
            }
        }
        RecyclerViewSchool.notifyDataChanges();
    }

    public static void FirebaseSchoolDataRetrieval(){
        //String key = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("school");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    boolean found = false;
                    for (School school : schools) {
                        if (school.getSchoolUid() == postSnapshot.getValue(School.class).getSchoolUid()) {
                            found = true;
                        }
                    }
                    if (!found){
                        schools.add(postSnapshot.getValue(School.class));
                        Log.e("Get Data", (postSnapshot.getValue(School.class).getSchoolName()));
                    }}
                if (schools.size() == snapshot.getChildrenCount()){
                    RecyclerViewSchool.offProgressBar();
                    RecyclerViewSchool.notifyDataChanges();
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
        SchoolDataset = schools;
    }

    public void schoolItemOnClickListener(View view){

        final Toast toastItemOnClick;

        if (!schoolRetrieval) {
            toastItemOnClick = Toast.makeText(mContext, "Double Tap to Edit the data", Toast.LENGTH_SHORT);
            toastItemOnClick.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toastItemOnClick.cancel();
                }
            }, 500);
        }
        else if (schoolRetrieval){
            toastItemOnClick = Toast.makeText(mContext, "Double Tap to select the data", Toast.LENGTH_SHORT);
            toastItemOnClick.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toastItemOnClick.cancel();
                }
            }, 500);
        }
    }

}

