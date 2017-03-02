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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.ewrittenapp.admin.R;
import com.admin.ewrittenapp.admin.helper.InputValidatorHelper;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvLogin;
    Button btnRegister;
    ProgressDialog pDialog;
    RadioGroup rgUserType;
    RadioButton rdoAdmin, rdoUser;
    static final String TAG = RegisterActivity.class.getSimpleName();
    FirebaseAuth auth;
    InputValidatorHelper validatorHelper;
    static final String FIREBASE_URL = "https://final-project-d2fd7.firebaseio.com/";
    Firebase rootFB;

    //initialization of components or variables
    void initialization() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvLogin = (TextView) findViewById(R.id.tvRegistered);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        pDialog = new ProgressDialog(this);
        rgUserType = (RadioGroup) findViewById(R.id.rgUserType);
        rdoAdmin = (RadioButton) findViewById(R.id.rdoAdmin);
        rdoUser = (RadioButton) findViewById(R.id.rdoUser);
        validatorHelper = new InputValidatorHelper();
        auth = FirebaseAuth.getInstance();
        rootFB = new Firebase(FIREBASE_URL);
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
                    final String email = etEmail.getText().toString().trim();
                    final String password = etPassword.getText().toString().trim();
                    showDialog();
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    hideDialog();

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegisterActivity.this, "Enter valid data." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        switch (rgUserType.getCheckedRadioButtonId()) {
                                            case R.id.rdoAdmin:
                                                Toast.makeText(RegisterActivity.this, "Admin successfully registered", Toast.LENGTH_SHORT).show();
                                                Map<String, Object> adminUpdates = new HashMap<String, Object>();
                                                adminUpdates.put("email", auth.getCurrentUser().getEmail());
                                                adminUpdates.put("isData", false);
                                                rootFB.child("admin").child(auth.getCurrentUser().getUid()).updateChildren(adminUpdates);
                                                auth.signOut();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                                break;
                                            case R.id.rdoUser:
                                                Toast.makeText(RegisterActivity.this, "User registered " + email, Toast.LENGTH_SHORT).show();
                                                Toast.makeText(RegisterActivity.this, "Register another user", Toast.LENGTH_LONG).show();

                                                Map<String, Object> userCred = new HashMap<String, Object>();
                                                userCred.put("email", auth.getCurrentUser().getEmail());
                                                userCred.put("password", password);
                                                rootFB.child("userCredential").child(auth.getCurrentUser().getUid()).updateChildren(userCred);

                                                Map<String, Object> newUser = new HashMap<String, Object>();
                                                newUser.put("email", auth.getCurrentUser().getEmail());
                                                rootFB.child("newUser").child(auth.getCurrentUser().getUid()).updateChildren(newUser);

                                                clearFields();
                                                auth.signOut();
                                                break;
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    void clearFields() {
        etEmail.setText("");
        etPassword.setText("");
    }

    boolean inputValidation() {
        boolean valid = true;
        if (!validatorHelper.isValidEmail(etEmail.getText().toString())) {
            etEmail.setError("Enter valid email");
            valid = false;
        }
        if (!validatorHelper.isValidPassword(etPassword.getText().toString(), true)) {
            etPassword.setError("Use at least one special, one upper, one lower charter and one number. Password length should be greater than 6.");
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
