package com.admin.ewrittenapp.admin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admin.ewrittenapp.admin.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    EditText etEmail,etPassword;
    Button btnLogout,btnAdd,btnUser;
    ProgressDialog pDialog;
    private static final String FIREBASE_URL = "https://e-written-application-aa06e.firebaseio.com/";
    private static final String TAG = DashboardActivity.class.getSimpleName();
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    Firebase rootFB;
    FirebaseUser user;


    private void initialization(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnUser = (Button) findViewById(R.id.btnUser);
        pDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        rootFB = new Firebase(FIREBASE_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initialization();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    rootFB.child("admin").child(user.getUid()).child("isData").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                boolean isData = (boolean) snapshot.getValue();
                                if (!isData) {
                                    startActivity(new Intent(DashboardActivity.this,GetDetailsActivity.class));
                                    finish();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) { }
                    });
                } else {
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                    Log.d(TAG, "onCreate: " + "if user is null than jump to LoginActivity");
                    finish();
                }
            }
        };

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(DashboardActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                hideDialog();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(DashboardActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(DashboardActivity.this, "Enter valid data." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DashboardActivity.this, "Successfully registered.", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    /*//adding admin to the databse in admin node
                                    Map<String, Object> userUpdates = new HashMap<String, Object>();
                                    userUpdates.put("email", auth.getCurrentUser().getEmail());
                                    rootFB.child("admin").child(auth.getCurrentUser().getUid()).updateChildren(userUpdates);
                                    startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                                    Log.d(TAG, "onCreate: " + "data is not given and start get details activity");
                                    finish();*/
                                }
                            }
                        });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                Log.d(TAG, "onCreate: " + "signing out");
                finish();
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
            }
        });

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

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.setMessage("Adding user...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
