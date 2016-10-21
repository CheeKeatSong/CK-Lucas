package com.point2points.kdusurveysystem.RecylcerView;

        import android.app.ActionBar;
        import android.app.Activity;
        import android.content.DialogInterface;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v4.widget.Space;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.SearchView;
        import android.support.v7.widget.Toolbar;
        import android.text.Editable;
        import android.text.InputType;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.daimajia.swipe.util.Attributes;
        import com.point2points.kdusurveysystem.Fragment.AdminDrawerFragment;
        import com.point2points.kdusurveysystem.Lecturer;
        import com.point2points.kdusurveysystem.ListSorter;
        import com.point2points.kdusurveysystem.R;
        import com.point2points.kdusurveysystem.adapter.RecyclerViewAdapter;
        import com.point2points.kdusurveysystem.adapter.util.DividerItemDecoration;
        import com.point2points.kdusurveysystem.model.ExampleModel;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Comparator;
        import java.util.List;

        import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

        import static android.support.v7.recyclerview.R.attr.layoutManager;

public class RecyclerViewExample extends AppCompatActivity {

    /**
     * RecyclerView: The new recycler view replaces the list view. Its more modular and therefore we
     * must implement some of the functionality ourselves and attach it to our recyclerview.
     * <p/>
     * 1) Position items on the screen: This is done with LayoutManagers
     * 2) Animate & Decorate views: This is done with ItemAnimators & ItemDecorators
     * 3) Handle any touch events apart from scrolling: This is now done in our adapter's ViewHolder
     */
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<String> mDataSet;

    private ImageButton optionButton, addButton, searchButton, backButton;
    private Spinner sortButton;
    private SearchView searchEditText;
    private Space mSpace;
    private Toolbar mToolBar, mToolBar2;

    private int pointer = 0;

    private static final String[] MOVIES = new String[]{

};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("RecyclerView");
            }
        }

        mToolBar = (Toolbar) findViewById(R.id.tToolbar);
        setSupportActionBar(mToolBar);
        mToolBar2 = (Toolbar) findViewById(R.id.t2Toolbar);
        setSupportActionBar(mToolBar2);

        optionButton = (ImageButton) findViewById(R.id.menu_item_option);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        backButton = (ImageButton)findViewById(R.id.menu_item_back);
        mSpace = (Space) findViewById(R.id.toolbar_space);
        searchEditText = (SearchView) findViewById(R.id.search_edit_text);
        searchEditText.setIconified(false);
        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchButton = (ImageButton) findViewById(R.id.menu_item_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar.setVisibility(View.GONE);
                mToolBar2.setVisibility(View.VISIBLE);


            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar.setVisibility(View.VISIBLE);
                mToolBar2.setVisibility(View.GONE);
            }
        });

        sortButton = (Spinner) findViewById(R.id.menu_item_sort);
        sortButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String sortType = String.valueOf(sortButton.getSelectedItem());
                ((TextView)selectedItemView).setText(null);
                ListSorter listSorter = new ListSorter();

                if (sortType == "A-Z"){

                }
                else if(sortType == "Z-A"){

                }
                else if(sortType == "Latest"){

                }
                else if(sortType == "Earliest"){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        addButton = (ImageButton) findViewById(R.id.menu_item_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(RecyclerViewExample.this);
                View promptsView = li.inflate(R.layout.lecturer_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecyclerViewExample.this);

                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setTitle("CREATE A LECTURER INFO");

                final EditText email = (EditText) promptsView.findViewById(R.id.lecturer_dialog_email);
                final EditText password = (EditText) promptsView.findViewById(R.id.lecturer_dialog_password);
                final EditText fullname = (EditText) promptsView.findViewById(R.id.lecturer_dialog_fullname);
                final EditText username = (EditText) promptsView.findViewById(R.id.lecturer_dialog_username);

                final ImageButton showpass = (ImageButton) promptsView.findViewById(R.id.lecturer_dialog_show_password);
                showpass.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {

                        switch ( event.getAction() ) {
                            case MotionEvent.ACTION_DOWN:
                                password.setInputType(InputType.TYPE_CLASS_TEXT);
                                break;
                            case MotionEvent.ACTION_UP:
                                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                break;
                        }
                        return true;
                    }
                });

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        final String inputEmail = email.getText().toString();
                                        final String inputPassword = password.getText().toString();
                                        final String inputFullName = fullname.getText().toString();
                                        final String inputUsername = username.getText().toString();

                                        Lecturer lecturer = new Lecturer();
                                        lecturer.createLecturer(inputEmail,inputPassword,inputFullName,inputUsername);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Item Decorator:
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.border)));
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        // Adapter:
        String[] adapterData = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        mDataSet = new ArrayList<String>(Arrays.asList(adapterData));
        mAdapter = new RecyclerViewAdapter(this, mDataSet);
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
    }

    /**
     * Substitute for our onScrollListener for RecyclerView
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}

