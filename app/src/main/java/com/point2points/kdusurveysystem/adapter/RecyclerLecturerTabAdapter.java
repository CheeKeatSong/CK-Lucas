package com.point2points.kdusurveysystem.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.point2points.kdusurveysystem.Fragment.LecturerFragmentPagerActivity;
import com.point2points.kdusurveysystem.Login;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewLecturer;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewSchool;
import com.point2points.kdusurveysystem.adapter.util.RecyclerLetterIcon;
import com.point2points.kdusurveysystem.datamodel.Admin;
import com.point2points.kdusurveysystem.datamodel.Lecturer;
import com.point2points.kdusurveysystem.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.StringTokenizer;

public class RecyclerLecturerTabAdapter extends RecyclerSwipeAdapter<RecyclerLecturerTabAdapter.SimpleViewHolder> {

    private static final String TAG = "RecyclerLecturerAdapter";

    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    static Query query;

    private Activity mActivity;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String UID;

    //relogin after delete data
    Admin admin = new Admin();

    public static class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewFullName;
        TextView textViewID;
        TextView textViewEmail;
        TextView textViewPoint;
        TextView textViewUid;
        ImageButton buttonDelete;
        ImageButton  buttonEdit;
        ImageView letterimage;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewFullName = (TextView) itemView.findViewById(R.id.lecturer_fullname_text_view);
            textViewID = (TextView) itemView.findViewById(R.id.lecturer_ID_text_view);
            textViewEmail = (TextView) itemView.findViewById(R.id.lecturer_email_text_view);
            textViewPoint = (TextView) itemView.findViewById(R.id.lecturer_point_text_view);
            buttonDelete = (ImageButton ) itemView.findViewById(R.id.delete);
            buttonEdit = (ImageButton ) itemView.findViewById(R.id.edit);
            letterimage = (ImageView) itemView.findViewById(R.id.letter_icon);
            textViewUid = (TextView) itemView.findViewById(R.id.lecturer_uid_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Double Tap to Edit the data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //SortedList
   /* private final SortedList<ExampleModel> mSortedList = new SortedList<>(ExampleModel.class, new SortedList.Callback<ExampleModel>() {
        @Override
        public int compare(ExampleModel a, ExampleModel b) {
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(ExampleModel oldItem, ExampleModel newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(ExampleModel item1, ExampleModel item2) {
            return item1.getId() == item2.getId();
        }
    });

    //we first need a way to add or remove items to the Adapter. For this purpose we can add methods to the Adapter which allow us to add and remove items to the SortedList
    public void add(ExampleModel model) {
        mSortedList.add(model);
    }

    public void remove(ExampleModel model) {
        mSortedList.remove(model);
    }

    public void add(List<ExampleModel> models) {
        mSortedList.addAll(models);
    }

    public void remove(List<ExampleModel> models) {
        mSortedList.beginBatchedUpdates();
        for (ExampleModel model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<ExampleModel> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final ExampleModel model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }
    //add or remove end

    private final LayoutInflater mInflater;
    private final Comparator<ExampleModel> mComparator;*/

    private Context mContext;
    private static ArrayList<Lecturer> lecturers = new ArrayList<>();
    public static ArrayList<Lecturer> LecturerDataset = new ArrayList<>();

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);
    public RecyclerLecturerTabAdapter(Context context) {
        this.mContext = context;
        mActivity = (Activity) mContext;

        //String key = ref.push().getKey();
        FirebaseLecturerDataRetrieval();
        }

    public static void sortingData(int sortoption){

        FirebaseLecturerDataRetrieval();

        switch (sortoption) {
            case 1:
                Collections.sort(LecturerDataset, new Comparator<Lecturer>() {
                    @Override
                    public int compare(Lecturer lecturer1, Lecturer lecturer2){
                        return (lecturer1.getFullName().substring(0, 1).toUpperCase() + lecturer1.getFullName().substring(1)).compareTo(lecturer2.getFullName().substring(0, 1).toUpperCase() + lecturer2.getFullName().substring(1));
                    }
                });

                break;
            case 2:
                Collections.sort(LecturerDataset, new Comparator<Lecturer>() {
                    @Override
                    public int compare(Lecturer lecturer1, Lecturer lecturer2){
                        return (lecturer1.getFullName().substring(0, 1).toUpperCase() + lecturer1.getFullName().substring(1)).compareTo(lecturer2.getFullName().substring(0, 1).toUpperCase() + lecturer2.getFullName().substring(1));
                    }
                });
                Collections.reverse(LecturerDataset);
                break;
            case 3:
                Collections.sort(LecturerDataset, new Comparator<Lecturer>() {
                    @Override
                    public int compare(Lecturer lecturer1, Lecturer lecturer2){
                        return lecturer1.getDate().compareTo(lecturer2.getDate());
                    }
                });
                Collections.reverse(LecturerDataset);
                break;
            case 4:
                Collections.sort(LecturerDataset, new Comparator<Lecturer>() {
                    @Override
                    public int compare(Lecturer lecturer1, Lecturer lecturer2){
                        return lecturer1.getDate().compareTo(lecturer2.getDate());
                    }
                });
                break;
            default:
                break;
        }
        RecyclerViewLecturer.notifyDataChanges();
    }

    public static void LecturerArrayListUpdate(ArrayList updatedLecturers) {

        lecturers = updatedLecturers;

        RecyclerViewLecturer.notifyDataChanges();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_recycler_view_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        final Lecturer item = LecturerDataset.get(position);
        String fullname = (item.fullName).substring(0, 1).toUpperCase() + (item.fullName).substring(1);
        String email = item.emailAddress;
        String ID = item.lecturer_ID;
        String point = item.point;
        String uid = item.uid;
        StringTokenizer tokens = new StringTokenizer(point, ".");
        point = tokens.nextToken();

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
                Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.textViewFullName.getText().toString());
                Intent intent = LecturerFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Context context = view.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm deleting " + item.getLecturer_ID() + "?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                //Log.d("Deletion", viewHolder.textViewUid.getText().toString());
                                //removeUserFromAuth(item.getEmailAddress(), item.getPassword());

                                removeUserFromAuth(item.getEmailAddress(), item.getPassword());

                                ref.child("users").child("lecturer").child(viewHolder.textViewUid.getText().toString()).removeValue();
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                lecturers.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, LecturerDataset.size());
                                mItemManger.closeAllItems();
                                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewFullName.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                                LecturerDataset = lecturers;

                                RecyclerViewLecturer.offProgressBar();
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
                Intent intent = LecturerFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
            }
        });

        TextDrawable drawable = RecyclerLetterIcon.GenerateRecyclerLetterIcon(fullname);

        viewHolder.letterimage.setImageDrawable(drawable);
        viewHolder.textViewFullName.setText(fullname);
        viewHolder.textViewID.setText(ID);
        viewHolder.textViewEmail.setText(email);
        viewHolder.textViewPoint.setText("Point: " + point);
        viewHolder.textViewUid.setText(uid);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return LecturerDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        LecturerDataset = new ArrayList<Lecturer>();

        if (charText.length() == 0) {
            LecturerDataset = lecturers;
        }
        else
        {
            for (Lecturer lecturer : lecturers) {
                if (lecturer.getFullName().toLowerCase(Locale.getDefault()).contains(charText)
                        || lecturer.getEmailAddress().toLowerCase(Locale.getDefault()).contains(charText)
                        || lecturer.getLecturer_ID().toLowerCase(Locale.getDefault()).contains(charText)
                        || lecturer.getUsername().toLowerCase(Locale.getDefault()).contains(charText)
                        || lecturer.getSchoolName().toLowerCase(Locale.getDefault()).contains(charText)
                        || lecturer.getSchoolNameShort().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    LecturerDataset.add(lecturer);
                }
            }
        }
        RecyclerViewLecturer.notifyDataChanges();
    }

    public static void FirebaseLecturerDataRetrieval(){
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("lecturer");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                RecyclerViewLecturer.onProgressBar();
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    boolean found = false;
                    for (Lecturer lecturer : lecturers) {
                        if (lecturer.getUid() == postSnapshot.getValue(Lecturer.class).getUid()) {
                            found = true;
                        }
                    }
                    if (!found){
                        lecturers.add(postSnapshot.getValue(Lecturer.class));
                        Log.e("Get Data", (postSnapshot.getValue(Lecturer.class).getFullName()));
                    }}
                if (lecturers.size() == snapshot.getChildrenCount()){
                    RecyclerViewLecturer.offProgressBar();
                    RecyclerViewLecturer.notifyDataChanges();
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
        LecturerDataset = lecturers;
        Collections.sort(LecturerDataset, new Comparator<Lecturer>() {
            @Override
            public int compare(Lecturer lecturer1, Lecturer lecturer2){
                return lecturer1.getDate().compareTo(lecturer2.getDate());
            }
        });
        Collections.reverse(LecturerDataset);
    }

    public void removeUserFromAuth(final String email, final String password) {

        RecyclerViewLecturer.onProgressBar();

        FirebaseUser Adminuser = FirebaseAuth.getInstance().getCurrentUser();
        UID = Adminuser.getUid();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                        } else {
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(email, password);

                            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            currentUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            currentUser.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "User account deleted.");
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });

        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("admin");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (UID.equals(postSnapshot.getValue(Admin.class).getAdminUid())) {
                        admin = postSnapshot.getValue(Admin.class);
                        Log.e("Get Data", (postSnapshot.getValue(Admin.class).getAdminName()));
                        mAuth.signInWithEmailAndPassword(admin.getAdminEmail(), admin.getAdminPassword())
                                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            // there was an error
                                        } else {
                                        }
                                    }
                                });
                    }}
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }
}
