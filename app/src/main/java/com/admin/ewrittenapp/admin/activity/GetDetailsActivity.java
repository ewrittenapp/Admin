package com.admin.ewrittenapp.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.admin.ewrittenapp.admin.R;
import com.admin.ewrittenapp.admin.helper.InputValidatorHelper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class GetDetailsActivity extends AppCompatActivity {

    EditText etFname,etMname,etLname,etMobile;
    Button btnSubmit;
    private static final String FIREBASE_URL = "https://final-project-d2fd7.firebaseio.com/";
    FirebaseAuth auth;
    Firebase rootFB;
    FirebaseUser user;
    InputValidatorHelper validatorHelper;


    private void initialization(){
        etFname = (EditText) findViewById(R.id.etFname);
        etMname = (EditText) findViewById(R.id.etMname);
        etLname = (EditText) findViewById(R.id.etLname);
        etMobile = (EditText) findViewById(R.id.etMobile);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        auth = FirebaseAuth.getInstance();
        rootFB = new Firebase(FIREBASE_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        validatorHelper = new InputValidatorHelper();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);
        initialization();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null && inputValidation()) {
                    Map<String, Object> adminUpdates = new HashMap<String, Object>();
                    adminUpdates.put("isData", true);
                    adminUpdates.put("fname", etFname.getText().toString());
                    adminUpdates.put("mname", etMname.getText().toString());
                    adminUpdates.put("lname", etLname.getText().toString());
                    adminUpdates.put("mobile", etMobile.getText().toString());
                    rootFB.child("admin").child(auth.getCurrentUser().getUid()).updateChildren(adminUpdates);

                    rootFB.child("admin").child(user.getUid()).child("isData").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean isData = (boolean) snapshot.getValue();
                            if (isData) {
                                startActivity(new Intent(GetDetailsActivity.this,DashboardActivity.class));
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) { }
                    });
                }
            }
        });
    }

    private boolean inputValidation() {
        boolean valid = true;
        String fName = etFname.getText().toString().trim();
        String mName = etMname.getText().toString().trim();
        String lName = etLname.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();

        if (fName == null && !validatorHelper.isValidName(fName)) {
            valid = false;
            etFname.setError("Enter valid first name.");
        }
        if (mName == null && !validatorHelper.isValidName(mName)) {
            valid = false;
            etMname.setError("Enter valid middle name.");
        }
        if (lName == null && !validatorHelper.isValidName(lName)) {
            valid = false;
            etLname.setError("Enter valid last name.");
        }

        if (mobile == null && mobile.length() < 10) {
            valid = false;
            etMobile.setError("Enter valid phone number");
        }

        return valid;
    }
}
