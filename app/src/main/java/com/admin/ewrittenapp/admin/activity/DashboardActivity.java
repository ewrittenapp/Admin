package com.admin.ewrittenapp.admin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.ewrittenapp.admin.R;
import com.admin.ewrittenapp.admin.fragment.StudentDetailsFragment;
import com.admin.ewrittenapp.admin.fragment.FacultyDetailsFragment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    Spinner spnNewUser;
    Spinner spnUserType;
    Toolbar toolbar;
    LinearLayout llUserType;
    LinearLayout llNewUser;
    FrameLayout fragmentContainer;
    TextView tvNoNewUser;
    static final String FIREBASE_URL = "https://final-project-d2fd7.firebaseio.com/";
    static final String TAG = DashboardActivity.class.getSimpleName();
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    Firebase rootFB;
    FirebaseUser user;
    Bundle bundle;
    List<String> newUsers;
    List<String> userKey;


    private void initialization() {
        pDialog = new ProgressDialog(this);
        spnNewUser = (Spinner) findViewById(R.id.spnNewUser);
        spnUserType = (Spinner) findViewById(R.id.spnUserType);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        llNewUser = (LinearLayout) findViewById(R.id.llNewUser);
        llUserType = (LinearLayout) findViewById(R.id.llUserType);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        tvNoNewUser = (TextView) findViewById(R.id.tvNoNewUser);
        auth = FirebaseAuth.getInstance();
        rootFB = new Firebase(FIREBASE_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        bundle = new Bundle();
        newUsers = new ArrayList<String>();
        userKey = new ArrayList<String>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initialization();
        userAuthListener();
        showDialog();
        fragmentReplacement();
        changeBundleDataOfUser();
        insertingDataToSpinner();
    }

    void userAuthListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    rootFB.child("admin").child(user.getUid()).child("isData").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                boolean isData = (boolean) snapshot.getValue();
                                if (!isData) {
                                    startActivity(new Intent(DashboardActivity.this, GetDetailsActivity.class));
                                    finish();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                } else {
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    void changeBundleDataOfUser() {
        spnNewUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bundle.putString("key", userKey.get(i));
                bundle.putString("email", newUsers.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    void fragmentReplacement() {
        spnUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                switch (position) {
                    case 0:
                        StudentDetailsFragment stuFragment = StudentDetailsFragment.newInstance();
                        stuFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, stuFragment).commit();
                        break;
                    case 1:
                        FacultyDetailsFragment facFragment = FacultyDetailsFragment.newInstance();
                        facFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, facFragment).commit();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void insertingDataToSpinner() {
        rootFB.child("newUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    showView();
                    newUsers.clear();
                    userKey.clear();
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        userKey.add(areaSnapshot.getKey());
                        newUsers.add(areaSnapshot.child("email").getValue(String.class));
                    }
                    ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_spinner_item, newUsers);
                    userAdapter.notifyDataSetChanged();
                    userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnNewUser.setAdapter(userAdapter);
                    hideDialog();
                } else {
                    hideView();
                    newUsers.clear();
                    userKey.clear();
                    spnNewUser.setAdapter(null);
                    hideDialog();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                logout();
                break;

            case R.id.action_faculty_post:
                startActivity(new Intent(DashboardActivity.this, FacultyPostActivity.class));
                break;

            case R.id.action_user_password:
                startActivity(new Intent(DashboardActivity.this, UserPasswordActivity.class));
                break;
        }
        return true;
    }

    void showView(){
        llNewUser.setVisibility(View.VISIBLE);
        llUserType.setVisibility(View.VISIBLE);
        fragmentContainer.setVisibility(View.VISIBLE);
        tvNoNewUser.setVisibility(View.GONE);
    }

    void hideView(){
        llNewUser.setVisibility(View.GONE);
        llUserType.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.GONE);
        tvNoNewUser.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    private void logout() {
        auth.signOut();
        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        finish();
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            hideView();
            tvNoNewUser.setVisibility(View.GONE);
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
