package com.admin.ewrittenapp.admin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.admin.ewrittenapp.admin.R;
import com.admin.ewrittenapp.admin.helper.InputValidatorHelper;
import com.admin.ewrittenapp.admin.pojo.Student;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class StudentDetailsFragment extends Fragment {
    EditText etFirstName;
    EditText etMiddleName;
    EditText etLastName;
    Spinner spnBranch;
    Spinner spnSem;
    Spinner spnDiv;
    EditText etEnrollNum;
    EditText etPhoneNum;
    Button btnSubmit;
    private static final String FIREBASE_URL = "https://final-project-d2fd7.firebaseio.com/";
    private static final String TAG = StudentDetailsFragment.class.getSimpleName();
    Firebase rootFB;
    InputValidatorHelper validatorHelper;

    public static StudentDetailsFragment newInstance() {
        StudentDetailsFragment fragment = new StudentDetailsFragment();
        return fragment;
    }

    public StudentDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_details, container, false);
        initialization(view);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation()) {
                    String firstName = etFirstName.getText().toString().trim();
                    String middleName = etMiddleName.getText().toString().trim();
                    String lastName = etLastName.getText().toString().trim();
                    String enrollNum = etEnrollNum.getText().toString().trim();
                    String phoneNum = etPhoneNum.getText().toString().trim();
                    String branch = (String) spnBranch.getSelectedItem();
                    String sem = (String) spnSem.getSelectedItem();
                    String div = (String) spnDiv.getSelectedItem();

                    Student student = new Student(firstName, middleName, lastName, branch, sem, div,
                            enrollNum, getArguments().getString("email"), phoneNum);
                    rootFB.child("studentNode").child(getArguments().getString("key")).setValue(student);
//                    Map<String,String> userType = new HashMap<String, String>();
//                    userType.put(getArguments().getString("key"),"STUDENT");
                    rootFB.child("userType").child(getArguments().getString("key")).setValue("STUDENT");
                    Toast.makeText(getContext(), "User data inserted:"+getArguments().getString("email"), Toast.LENGTH_SHORT).show();
                    rootFB.child("newUser").child(getArguments().getString("key")).removeValue();
                    clearFields();
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
        String enrollNum = etEnrollNum.getText().toString().trim();
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

        if (enrollNum == null && enrollNum.length() < 13) {
            valid = false;
            etEnrollNum.setError("Enter enrollment number.");
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
        spnSem = (Spinner) view.findViewById(R.id.spnSem);
        spnDiv = (Spinner) view.findViewById(R.id.spnDiv);
        etEnrollNum = (EditText) view.findViewById(R.id.etEnrollNum);
        etPhoneNum = (EditText) view.findViewById(R.id.etPhoneNum);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        rootFB = new Firebase(FIREBASE_URL);

        validatorHelper = new InputValidatorHelper();
    }

    void clearFields(){
        etFirstName.setText("");
        etMiddleName.setText("");
        etLastName.setText("");
        etEnrollNum.setText("");
        etPhoneNum.setText("");
    }
}
