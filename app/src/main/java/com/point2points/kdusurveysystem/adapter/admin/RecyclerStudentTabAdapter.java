package com.point2points.kdusurveysystem.adapter.admin;

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
import com.point2points.kdusurveysystem.Fragment.StudentFragmentPagerActivity;
import com.point2points.kdusurveysystem.Login;
import com.point2points.kdusurveysystem.R;
import com.point2points.kdusurveysystem.RecyclerView.RecyclerViewStudent;
import com.point2points.kdusurveysystem.adapter.util.RecyclerLetterIcon;
import com.point2points.kdusurveysystem.datamodel.Admin;
import com.point2points.kdusurveysystem.datamodel.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class RecyclerStudentTabAdapter extends RecyclerSwipeAdapter<RecyclerStudentTabAdapter.SimpleViewHolder> {

    private static final String TAG = "RecyclerStudentAdapter";

    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    static Query query;

    //relogin after delete data
    Admin admin = new Admin();

    String UID;
    String adminEmail;

    private static Activity mActivity;

    public static boolean studentRetrieval = false;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public class SimpleViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewStudentName;
        TextView textViewStudentID;
        //TextView textViewStudentEmail;
        TextView textViewStudentCategory;
        //TextView textViewStudentDepartment;
        TextView textViewStudentSchool;
        TextView textViewStudentPoint;
        TextView textViewStudentUid;
        ImageButton buttonDelete;
        ImageButton buttonEdit;
        ImageView letterimage;

        public SimpleViewHolder(final View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewStudentName = (TextView) itemView.findViewById(R.id.student_name_text_view);
            textViewStudentID = (TextView) itemView.findViewById(R.id.student_id_text_view);
            //textViewStudentEmail = (TextView) itemView.findViewById(R.id.student_email_text_view);
            textViewStudentCategory = (TextView) itemView.findViewById(R.id.student_category_text_view);
            textViewStudentSchool = (TextView) itemView.findViewById(R.id.student_school_text_view);
            textViewStudentPoint = (TextView) itemView.findViewById(R.id.student_point_text_view);
            textViewStudentUid = (TextView) itemView.findViewById(R.id.student_uid_text_view);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.delete);
            buttonEdit = (ImageButton) itemView.findViewById(R.id.edit);
            letterimage = (ImageView) itemView.findViewById(R.id.letter_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //notifyItemChanged(selectedPos);
                    //selectedPos = getLayoutPosition();
                    //notifyItemChanged(selectedPos);
                    if (!studentRetrieval) {
                        Toast.makeText(view.getContext(), "Double Tap to Edit the data", Toast.LENGTH_SHORT).show();
                    } else if (studentRetrieval) {
                        Toast.makeText(view.getContext(), "Double Tap to select the data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private Context mContext;
    private static ArrayList<Student> students = new ArrayList<>();
    public static ArrayList<Student> StudentDataset = new ArrayList<>();

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);
    public RecyclerStudentTabAdapter(Context context) {
        this.mContext = context;
        mActivity = (Activity) mContext;
        FirebaseStudentDataRetrieval();
    }

    public static void sortingData(int sortoption) {

        //FirebaseStudentDataRetrieval();

        switch (sortoption) {
            case 1:
                Collections.sort(StudentDataset, new Comparator<Student>() {
                    @Override
                    public int compare(Student student1, Student student2) {
                        return (student1.getStudentName().substring(0, 1).toUpperCase() + student1.getStudentName().substring(1)).compareTo(student2.getStudentName().substring(0, 1).toUpperCase() + student2.getStudentName().substring(1));
                    }
                });

                break;
            case 2:
                Collections.sort(StudentDataset, new Comparator<Student>() {
                    @Override
                    public int compare(Student student1, Student student2) {
                        return (student1.getStudentName().substring(0, 1).toUpperCase() + student1.getStudentName().substring(1)).compareTo(student2.getStudentName().substring(0, 1).toUpperCase() + student2.getStudentName().substring(1));
                    }
                });
                Collections.reverse(StudentDataset);
                break;

            case 3:
                Collections.sort(StudentDataset, new Comparator<Student>() {
                    @Override
                    public int compare(Student student1, Student student2) {
                        return student1.getDate().compareTo(student2.getDate());
                    }
                });
                Collections.reverse(StudentDataset);
                break;
            case 4:
                Collections.sort(StudentDataset, new Comparator<Student>() {
                    @Override
                    public int compare(Student student1, Student student2) {
                        return student1.getDate().compareTo(student2.getDate());
                    }
                });
                break;
            default:
                break;
        }
        RecyclerViewStudent.notifyDataChanges();
    }

    public static void StudentArrayListUpdate(ArrayList updatedStudents) {
        students = updatedStudents;
        RecyclerViewStudent.notifyDataChanges();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_recycler_view_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        //viewHolder.itemView.setSelected(selectedPos == position);

        final Student item = StudentDataset.get(position);
        final String studentName = (item.studentName).substring(0, 1).toUpperCase() + (item.studentName).substring(1);
        String studentID = item.studentID;
        String studentCategory = item.studentCategory;
        String studentSchool = item.studentSchool;
        String studentPoint = item.studentPoint;
        String studentUid = item.studentUid;

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
                //if(!studentRetrieval) {
                Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.textViewStudentName.getText().toString());
                Intent intent = StudentFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewStudentUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
                //}
                /*else if(studentRetrieval) {
                    String studentName = viewHolder.textViewStudentName.getText().toString();
                    String studentNameShort = viewHolder.textViewStudentNameShort.getText().toString();
                    AdminToolbarDrawer.getStudent(studentName, studentNameShort);
                    studentRetrieval = false;
                    RecyclerViewStudent.closeRecyclerViewStudent();
                }*/
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Context context = view.getContext();

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setMessage("Confirm deleting " + item.getStudentName() + "?");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Log.d("Deletion", viewHolder.textViewUid.getText().toString());

                                removeUserFromAuth(item.getStudentEmail(), item.getStudentPassword());

                                ref = FirebaseDatabase.getInstance().getReference();
                                ref.child("users").child("student").child(viewHolder.textViewStudentUid.getText().toString()).removeValue();
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                students.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, StudentDataset.size());
                                mItemManger.closeAllItems();
                                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewStudentName.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                                StudentDataset = students;

                                RecyclerViewStudent.offProgressBar();
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
                Intent intent = StudentFragmentPagerActivity.newIntent(viewHolder.swipeLayout.getContext(), viewHolder.textViewStudentUid.getText().toString());
                viewHolder.swipeLayout.getContext().startActivity(intent);
            }
        });

        TextDrawable drawable = RecyclerLetterIcon.GenerateRecyclerLetterIcon(studentName, 64);

        viewHolder.letterimage.setImageDrawable(drawable);
        viewHolder.textViewStudentName.setText(studentName);
        viewHolder.textViewStudentID.setText(studentID);
        viewHolder.textViewStudentCategory.setText(studentCategory);
        viewHolder.textViewStudentSchool.setText(studentSchool);
        viewHolder.textViewStudentPoint.setText("Point: " + studentPoint);
        viewHolder.textViewStudentUid.setText(studentUid);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return StudentDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        StudentDataset = new ArrayList<Student>();

        if (charText.length() == 0) {
            StudentDataset = students;
        } else {
            for (Student student : students) {
                if (student.getStudentName().toLowerCase(Locale.getDefault()).contains(charText)
                        || student.getStudentID().toLowerCase(Locale.getDefault()).contains(charText)
                        || student.getStudentCategory().toLowerCase(Locale.getDefault()).contains(charText)
                        || student.getStudentSchool().toLowerCase(Locale.getDefault()).contains(charText)
                        || student.getStudentSchoolShort().toLowerCase(Locale.getDefault()).contains(charText)) {
                    StudentDataset.add(student);
                }
            }
        }
        RecyclerViewStudent.notifyDataChanges();
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
                    for (Student student : students) {
                        if (student.getStudentUid() == postSnapshot.getValue(Student.class).getStudentUid()) {
                            found = true;
                        }
                    }
                    if (!found) {
                        students.add(postSnapshot.getValue(Student.class));
                        Log.e("Get Data", (postSnapshot.getValue(Student.class).getStudentName()));
                    }
                }
                if (students.size() == snapshot.getChildrenCount()) {
                    StudentDataset = students;
                    Collections.sort(StudentDataset, new Comparator<Student>() {
                        @Override
                        public int compare(Student student1, Student student2) {
                            return student1.getDate().compareTo(student2.getDate());
                        }
                    });
                    Collections.reverse(StudentDataset);
                    RecyclerViewStudent.offProgressBar();
                    RecyclerViewStudent.notifyDataChanges();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    public void removeUserFromAuth(final String email, final String password) {

        RecyclerViewStudent.onProgressBar();

        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //UID = currentUser.getUid(); // Get logged in admin's user ID through checking current logged in user's ID. Doesn't work because other logins can take place through account creation.

        UID = Login.loginUID;  // Get logged in admin's user ID from Login class
        adminEmail = Login.loginEmail;

        //Log.d(TAG, "FIND ME: " + UID + " | " + adminEmail); // Troubleshooting

        FirebaseAuth.getInstance().signOut();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                        } else {
                            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(email, password);

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

                                            reauthAdmin();
                                        }
                                    });
                        }
                    }
                });

        /*ref = FirebaseDatabase.getInstance().getReference();
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
                                            Log.d(TAG, "Error reauthenticating!");
                                        } else {
                                            Log.d(TAG, "Reauthenticated to " + admin.getAdminEmail() + " account");
                                        }
                                    }
                                });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });*/
    }

    public void reauthAdmin() {
        ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("users").child("admin");
        query = ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Log.d(TAG, "EVENT LISTENER RUN"); // Troubleshooting.
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Log.d(TAG, "DATA SNAPSHOT RUN"); // Troubleshooting.
                    if (UID.equals(postSnapshot.getValue(Admin.class).getAdminUid())) {
                        admin = postSnapshot.getValue(Admin.class);
                        Log.e("Get Data", (postSnapshot.getValue(Admin.class).getAdminName()));

                        //Log.d(TAG, admin.getAdminEmail() + " | " + admin.getAdminPassword()); // Troubleshooting.
                        mAuth.signInWithEmailAndPassword(admin.getAdminEmail(), admin.getAdminPassword())
                                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.d(TAG, "Error reauthenticating!");
                                        } else {
                                            Log.d(TAG, "Reauthenticated to " + admin.getAdminEmail() + " admin account");
                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }
}

