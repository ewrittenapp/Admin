package com.admin.ewrittenapp.admin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.admin.ewrittenapp.admin.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacultyPostActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spnBranch, spnFaculty, spnPost, spnSem, spnDiv;
    Button btnSubmit;
    LinearLayout llClass;
    LinearLayout llSem;

    static final String FIREBASE_URL = "https://final-project-d2fd7.firebaseio.com/";
    static final String TAG = FacultyPostActivity.class.getSimpleName();

    FirebaseAuth auth;
    Firebase rootFB;
    FirebaseUser user;
    List<String> newUsers;
    List<String> userKey;

    void initialization() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spnBranch = (Spinner) findViewById(R.id.spnBranch);
        spnFaculty = (Spinner) findViewById(R.id.spnFaculty);
        spnPost = (Spinner) findViewById(R.id.spnPost);
        spnSem = (Spinner) findViewById(R.id.spnSem);
        spnDiv = (Spinner) findViewById(R.id.spnDiv);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        llClass = (LinearLayout) findViewById(R.id.llClass);
        llSem = (LinearLayout) findViewById(R.id.llSem);
        auth = FirebaseAuth.getInstance();
        rootFB = new Firebase(FIREBASE_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        newUsers = new ArrayList<String>();
        userKey = new ArrayList<String>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_post);
        initialization();
        hideSpinnersByPost();
        insertingDataToSpinner();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String branch = "";
                String[] demo = spnBranch.getSelectedItem().toString().split(" ");
                for (String s : demo) {
                    branch = branch + s;
                }

                switch (spnPost.getSelectedItemPosition()) {
                    case 0:
                        Map<String, Object> cc = new HashMap<String, Object>();
                        cc.put("ClassCoordinator", userKey.get(spnFaculty.getSelectedItemPosition()));
                        rootFB.child("facultyCoordinators").child(branch).
                                child("Sem" + spnSem.getSelectedItem().toString().trim()).
                                child("Class" + spnDiv.getSelectedItem().toString().trim()).updateChildren(cc);
                        break;
                    case 1:
                        Map<String, Object> sc = new HashMap<String, Object>();
                        sc.put("SemesterCoordinator", userKey.get(spnFaculty.getSelectedItemPosition()));
                        rootFB.child("facultyCoordinators").child(branch).
                                child("Sem" + spnSem.getSelectedItem().toString().trim()).updateChildren(sc);
                        break;
                    case 2:
                        Map<String, Object> hod = new HashMap<String, Object>();
                        hod.put("HOD", userKey.get(spnFaculty.getSelectedItemPosition()));
                        rootFB.child("facultyCoordinators").child(branch).updateChildren(hod);
                        break;
                }
                Toast.makeText(FacultyPostActivity.this, "" + spnPost.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void insertingDataToSpinner() {
        spnBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String branch = "";
                String[] demo = spnBranch.getItemAtPosition(i).toString().split(" ");
                for (String s : demo) {
                    branch = branch + s;
                }
                rootFB.child("facultyList").child(branch).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            newUsers.clear();
                            userKey.clear();
                            for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                userKey.add(areaSnapshot.getKey());
                                newUsers.add(areaSnapshot.child("name").getValue(String.class));
                            }
                            ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(FacultyPostActivity.this, android.R.layout.simple_spinner_item, newUsers);
                            userAdapter.notifyDataSetChanged();
                            userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnFaculty.setAdapter(userAdapter);

                        } else {
                            newUsers.clear();
                            userKey.clear();
                            spnFaculty.setAdapter(null);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void hideSpinnersByPost() {
        spnPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                llClass.setVisibility(View.VISIBLE);
                llSem.setVisibility(View.VISIBLE);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        llClass.setVisibility(View.GONE);
                        break;
                    case 2:
                        llSem.setVisibility(View.GONE);
                        llClass.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
