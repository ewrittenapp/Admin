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
import android.widget.TextView;
import android.widget.Toast;

import com.admin.ewrittenapp.admin.R;
import com.admin.ewrittenapp.admin.helper.InputValidatorHelper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText inputEmail, inputPassword;
    private TextView tvRegister;
    private FirebaseAuth auth;
    private Button btnLogin;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();

        }
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                if(!email.equals("") && !password.equals("")){
                    showDialog();
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    } else {
                                        hideDialog();
                                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Input fields can't be empty.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void initialization() {
        inputEmail = (EditText) findViewById(R.id.etEmail);
        inputPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegisterHere);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(this);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

