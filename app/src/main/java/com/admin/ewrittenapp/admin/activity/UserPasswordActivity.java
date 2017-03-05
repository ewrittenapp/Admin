package com.admin.ewrittenapp.admin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.admin.ewrittenapp.admin.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPasswordActivity extends AppCompatActivity {

    AutoCompleteTextView actvEmail;
    TextView tvPassword;
    Toolbar toolbar;
    static final String FIREBASE_URL = "https://final-project-d2fd7.firebaseio.com/";
    static final String TAG = UserPasswordActivity.class.getSimpleName();
    FirebaseAuth auth;
    Firebase rootFB;
    Map<String, String> hello;
    List<String> userEmail;
    List<String> userPassword;
//
    void initialization() {
        actvEmail = (AutoCompleteTextView) findViewById(R.id.actvEmail);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        rootFB = new Firebase(FIREBASE_URL);
        userEmail = new ArrayList<String>();
        userPassword = new ArrayList<String>();
        hello = new HashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password);
        initialization();
        insertingDataIntoACTV();

        actvEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvPassword.setText(hello.get(adapterView.getItemAtPosition(i).toString()));
            }
        });
    }


    void insertingDataIntoACTV() {
        rootFB.child("userCredential").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    userEmail.clear();
                    userPassword.clear();
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        hello.put(areaSnapshot.child("email").getValue(String.class), areaSnapshot.child("password").getValue(String.class));
                        userEmail.add(areaSnapshot.child("email").getValue(String.class));
                        userPassword.add(areaSnapshot.child("password").getValue(String.class));
                    }
                    ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(UserPasswordActivity.this, android.R.layout.simple_list_item_1, userEmail);
                    userAdapter.notifyDataSetChanged();
                    actvEmail.setAdapter(userAdapter);
                } else {
                    userEmail.clear();
                    userPassword.clear();
                    actvEmail.setAdapter(null);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
