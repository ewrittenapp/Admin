package com.admin.ewrittenapp.admin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.admin.ewrittenapp.admin.R;
import com.admin.ewrittenapp.admin.helper.InputValidatorHelper;
import com.admin.ewrittenapp.admin.pojo.Faculty;
import com.firebase.client.Firebase;

import static com.google.android.gms.internal.zzs.TAG;

public class FacultyDetailsFragment extends Fragment {

    EditText etFirstName;
    EditText etMiddleName;
    EditText etLastName;
    Spinner spnBranch;
    EditText etFacultyId;
    EditText etPhoneNum;
    Button btnSubmit;
    private static final String FIREBASE_URL = "https://final-project-d2fd7.firebaseio.com/";
    Firebase rootFB;
    InputValidatorHelper validatorHelper;

    public static FacultyDetailsFragment newInstance() {
        FacultyDetailsFragment fragment = new FacultyDetailsFragment();
        return fragment;
    }

    public FacultyDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculty_details, container, false);
        initialization(view);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation()) {
                    String firstName = etFirstName.getText().toString().trim();
                    String middleName = etMiddleName.getText().toString().trim();
                    String lastName = etLastName.getText().toString().trim();
                    String facultyId = etFacultyId.getText().toString().trim();
                    String phoneNum = etPhoneNum.getText().toString().trim();
                    String branch = (String) spnBranch.getSelectedItem();
                    Faculty faculty = new Faculty(firstName, middleName, lastName, branch, facultyId,
                            getArguments().getString("email"), phoneNum);
                    Log.d(TAG, "onDataChange: " + "add all the data to firebase");
                    rootFB.child("faculties").child(getArguments().getString("key")).setValue(faculty);
                    Toast.makeText(getContext(), "User data inserted:"+getArguments().getString("email"), Toast.LENGTH_SHORT).show();
                    rootFB.child("newUser").child(getArguments().getString("key")).removeValue();
                }

            }
        });
        return view;
    }


    private boolean inputValidation() {
        boolean valid = true;
        String firstName = etFirstName.getText().toString().trim();
        String middleName = etMiddleName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String facultyId = etFacultyId.getText().toString().trim();
        String phoneNum = etPhoneNum.getText().toString().trim();

        if (firstName == null && !validatorHelper.isValidName(firstName)) {
            valid = false;
            etFirstName.setError("Enter valid first name.");
        }
        if (middleName == null && !validatorHelper.isValidName(middleName)) {
            valid = false;
            etMiddleName.setError("Enter valid middle name.");
        }
        if (lastName == null && !validatorHelper.isValidName(lastName)) {
            valid = false;
            etLastName.setError("Enter valid last name.");
        }
        if (facultyId == null && facultyId.length() < 13) {
            valid = false;
            etFacultyId.setError("Enter faculty id number.");
        }
        if (phoneNum == null && phoneNum.length() < 10) {
            valid = false;
            etPhoneNum.setError("Enter valid phone number");
        }
        return valid;
    }

    private void initialization(View view) {
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etMiddleName = (EditText) view.findViewById(R.id.etMiddleName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        spnBranch = (Spinner) view.findViewById(R.id.spnBranch);
        etFacultyId = (EditText) view.findViewById(R.id.etFacultyId);
        etPhoneNum = (EditText) view.findViewById(R.id.etPhoneNum);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        rootFB = new Firebase(FIREBASE_URL);
        validatorHelper = new InputValidatorHelper();
    }
}