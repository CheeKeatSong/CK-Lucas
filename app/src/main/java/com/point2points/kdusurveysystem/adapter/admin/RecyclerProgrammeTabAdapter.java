package com.point2points.kdusurveysystem.adapter.admin;

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
import com.point2points.kdusurveysystem.Fragment.ProgrammeFragmentPagerActivity;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewProgramme;
import com.point2points.kdusurveysystem.adapter.util.RecyclerLetterIcon;
import com.point2points.kdusurveysystem.admin.AdminToolbarDrawer;
import com.point2points.kdusurveysystem.datamodel.Programme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class RecyclerProgrammeTabAdapter extends RecyclerSwipeAdapter<RecyclerProgrammeTabAdapter.SimpleViewHolder> {

    private static final String EXTRA_PROGRAMME = "com.point2points.kdusurveysystem.programme";

    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    static Query query;

    private static Context mContext;
    private static Activity mActivity;

    private static ArrayList<Programme> programmes = new ArrayList<>();
    public static ArrayList<Programme> ProgrammeDataset = new ArrayList<>();

    public static boolean programmeRetrieval = false;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, RecyclerViewProgramme.class);
        programmeRetrieval = true;
        return intent;
    }

    public static String programmeRetrieval(Intent result){
        return result.getStringExtra(EXTRA_PROGRAMME);
    }

    private void setProgramme(String programme){
        AdminToolbarDrawer.tabIdentifier = AdminToolbarDrawer.tabIdentifierMutex;
        Intent data = new Intent();
        data.putExtra(EXTRA_PROGRAMME, programme);
        mActivity.setResult(Activity.RESULT_OK, data);
        mActivity.finish();
        Log.e("set " ,"programme");
    }

    public class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewProgrammeName;
        TextView textViewProgrammeCategory;
        TextView textViewProgrammeSchool;
        TextView textViewProgrammeUid;
        ImageButton buttonDelete;
        ImageButton  buttonEdit;
        ImageView letterimage;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewProgrammeName = (TextView) itemView.findViewById(R.id.programme_name_text_view);
            textViewProgrammeCategory = (TextView) itemView.findViewById(R.id.programme_category_text_view);
            //textViewProgrammeDepartment = (TextView) itemView.findViewById(R.id.programme_department_text_view);
            textViewProgrammeSchool = (TextView) itemView.findViewById(R.id.programme_school_text_view);
            textViewProgrammeUid = (TextView) itemView.findViewById(R.id.programme_uid_text_view);
            buttonDelete = (ImageButton ) itemView.findViewById(R.id.delete);
            buttonEdit = (ImageButton ) itemView.findViewById(R.id.edit);
            letterimage = (ImageView) itemView.findViewById(R.id.letter_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    programmeItemOnClickListener(view);
                }
            });
        }
    }

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);
    public RecyclerProgrammeTabAdapter(Context context) {
        this.mContext = context;
        mActivity = (Activity) mContext;
        FirebaseProgrammeDataRetrieval();
    }

    public static void sortingData(int sortoption){

        FirebaseProgrammeDataRetrieval();

        switch (sortoption) {
            case 1:
                Collections.sort(ProgrammeDataset, new Comparator<Programme>() {
                    @Override
                    public int compare(Programme programme1, Programme programme2){
                        return (programme1.getProgrammeName().substring(0, 1).toUpperCase() + programme1.getProgrammeName().substring(1)).compareTo(programme2.getProgrammeName().substring(0, 1).toUpperCase() + programme2.getProgrammeName().substring(1));
                    }
                });

                break;
            case 2:
                Collections.sort(ProgrammeDataset, new Comparator<Programme>() {
                    @Override
                    public int compare(Programme programme1, Programme programme2){
                        return (programme1.getProgrammeName().substring(0, 1).toUpperCase() + programme1.getProgrammeName().substring(1)).compareTo(programme2.getProgrammeName().substring(0, 1).toUpperCase() + programme2.getProgrammeName().substring(1));
                    }
                });
                Collections.reverse(ProgrammeDataset);
                break;

            case 3:
                Collections.sort(ProgrammeDataset, new Comparator<Programme>() {
                    @Override
                    public int compare(Programme programme1, Programme programme2){
                        return programme1.getDate().compareTo(programme2.getDate());
                    }
                });
                Collections.reverse(ProgrammeDataset);
                break;
            case 4:
                Collections.sort(ProgrammeDataset, new Comparator<Programme>() {
                    @Override
                    public int compare(Programme programme1, Programme programme2){
                        return programme1.getDate().compareTo(programme2.getDate());
                    }
                });
                break;
            default:
                break;
        }
        RecyclerViewProgramme.notifyDataChanges();
    }

    public static void ProgrammeArrayListUpdate(ArrayList updatedProgrammes) {
        programmes = updatedProgrammes;
        RecyclerViewProgramme.notifyDataChanges();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.programme_recycler_view_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        //viewHolder.itemView.setSelected(selectedPos == position);

        final Programme item = ProgrammeDataset.get(position);
        final String programmeName = (item.programmeName).substring(0, 1).toUpperCase() + (item.programmeName).substring(1);
        String programmeCategory = item.programmeCategory;
        //String programmeDepartment = item.programmeDepartment;
        String programmeSchool = item.programmeSchool;
        String programmeUid = item.programmeUid;

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
                if(!programmeRetrieval) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.textViewProgrammeName.getText().toString());
                    Intent intent = ProgrammeFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewProgrammeUid.getText().toString());
                    viewHolder.swipeLayout.getContext().startActivity(intent);
                }
                else if(programmeRetrieval) {
                    final Toast toastOnDoubleClick = Toast.makeText(mContext, viewHolder.textViewProgrammeName.getText().toString() + " Selected.", Toast.LENGTH_SHORT);
                    toastOnDoubleClick.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toastOnDoubleClick.cancel();
                        }
                    }, 1500);

                    String programme = viewHolder.textViewProgrammeName.getText().toString();
                    programmeRetrieval = false;
                    setProgramme(programme);
                }
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Context context = view.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm deleting " + item.getProgrammeName() + "?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                //Log.d("Deletion", viewHolder.textViewUid.getText().toString());
                                ref.child(viewHolder.textViewProgrammeUid.getText().toString()).removeValue();
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                programmes.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, ProgrammeDataset.size());
                                mItemManger.closeAllItems();
                                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewProgrammeName.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                                ProgrammeDataset = programmes;
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
                Intent intent = ProgrammeFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewProgrammeUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
            }
        });

        String programmeNameReformatted = "";
        if(programmeName.contains("of")){
        programmeNameReformatted = programmeName.substring(programmeName.lastIndexOf("of") -1);
        }
        else if (programmeName.contains("in")){
            programmeNameReformatted = programmeName.substring(programmeName.lastIndexOf("in") +3);
        }
        TextDrawable drawable = RecyclerLetterIcon.GenerateRecyclerLetterIcon(programmeNameReformatted, 64);    // Fix this

        viewHolder.letterimage.setImageDrawable(drawable);
        viewHolder.textViewProgrammeName.setText(programmeName);
        viewHolder.textViewProgrammeCategory.setText(programmeCategory);
        //viewHolder.textViewProgrammeDepartment.setText(programmeDepartment);
        viewHolder.textViewProgrammeSchool.setText(programmeSchool);
        viewHolder.textViewProgrammeUid.setText(programmeUid);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return ProgrammeDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        ProgrammeDataset = new ArrayList<Programme>();

        if (charText.length() == 0) {
            ProgrammeDataset = programmes;
        }
        else
        {
            for (Programme programme : programmes)
            {
                if (programme.getProgrammeName().toLowerCase(Locale.getDefault()).contains(charText)
                        || programme.getProgrammeSchoolShort().toLowerCase(Locale.getDefault()).contains(charText)
                        || programme.getProgrammeCategory().toLowerCase(Locale.getDefault()).contains(charText)
                        || programme.getProgrammeSchool().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    ProgrammeDataset.add(programme);
                }
            }
        }
        RecyclerViewProgramme.notifyDataChanges();
    }

    public static void FirebaseProgrammeDataRetrieval(){
        //String key = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("programme");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    boolean found = false;
                    for (Programme programme : programmes) {
                        if (programme.getProgrammeUid() == postSnapshot.getValue(Programme.class).getProgrammeUid()) {
                            found = true;
                        }
                    }
                    if (!found){
                        programmes.add(postSnapshot.getValue(Programme.class));
                        Log.e("Get Data", (postSnapshot.getValue(Programme.class).getProgrammeName()));
                    }}
                if (programmes.size() == snapshot.getChildrenCount()){
                    RecyclerViewProgramme.offProgressBar();
                    RecyclerViewProgramme.notifyDataChanges();
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
        ProgrammeDataset = programmes;
        Collections.sort(ProgrammeDataset, new Comparator<Programme>() {
            @Override
            public int compare(Programme programme1, Programme programme2){
                return programme1.getDate().compareTo(programme2.getDate());
            }
        });
        Collections.reverse(ProgrammeDataset);
    }

    public void programmeItemOnClickListener(View view){

        final Toast toastItemOnClick;

        if (!programmeRetrieval) {
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
        else if (programmeRetrieval){
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

