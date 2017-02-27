package com.admin.ewrittenapp.admin.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.admin.ewrittenapp.admin.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://e-written-application-aa06e.firebaseio.com/";
    private static final String TAG = DashboardActivity.class.getSimpleName();
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    Firebase firebase;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        auth = FirebaseAuth.getInstance();
        firebase = new Firebase(FIREBASE_URL);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {

                } else {
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                    Log.d(TAG, "onCreate: " + "if user is null than jump to LoginActivity");
                    finish();
                }
            }
        };

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                Log.d(TAG, "onCreate: " + "signing out");
                finish();
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

}
