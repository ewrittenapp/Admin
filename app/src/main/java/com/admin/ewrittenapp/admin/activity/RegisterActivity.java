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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputEmail, inputPassword;
    private TextView tvLogin;
    private FirebaseAuth auth;
    private Button btnRegister;
    private ProgressDialog pDialog;
    private InputValidatorHelper validatorHelper;

    //initialization of components or variables
    void initialization() {
        validatorHelper = new InputValidatorHelper();
        inputEmail = (EditText) findViewById(R.id.etEmail);
        inputPassword = (EditText) findViewById(R.id.etPassword);
        tvLogin = (TextView) findViewById(R.id.tvRegistered);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        pDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialization();
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                Log.d(TAG, "onCreate: " + "going to login page");
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation()) {
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();
                    showDialog();
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(RegisterActivity.this, "Successfully registered.", Toast.LENGTH_SHORT).show();
                                    hideDialog();

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegisterActivity.this, "Enter valid data." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                                        Log.d(TAG, "onCreate: " + "data is not given and start get details activity");
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    boolean inputValidation() {
        boolean valid = true;
        if (!validatorHelper.isValidEmail(inputEmail.getText().toString())) {
            inputEmail.setError("Enter valid email");
            valid = false;
        }
        if (!validatorHelper.isValidPassword(inputPassword.getText().toString(), true)) {
            inputPassword.setError("Use at least one special, one upper, one lower charter and one number. Password length should be greater than 6.");
            valid = false;
        }
        return valid;
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
