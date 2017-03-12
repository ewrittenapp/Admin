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
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.internal.zzs.TAG;

public class FacultyDetailsFragment extends Fragment {

    EditText etFirstName;
    EditText etLastName;
    Spinner spnBranch;
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
                    String name = etFirstName.getText().toString().trim()+" "+etLastName.getText().toString().trim();
                    String phoneNum = etPhoneNum.getText().toString().trim();
                    String branch = spnBranch.getSelectedItem().toString().trim();
                    Log.d(TAG, "onClick: branch "+branch);
                    Faculty faculty = new Faculty(name, branch,
                            getArguments().getString("email"), phoneNum);
                    rootFB.child("facultyNode").child(getArguments().getString("key")).setValue(faculty);
                    Map<String, Object> facUser = new HashMap<String, Object>();
                    facUser.put("email", getArguments().getString("email"));
                    facUser.put("name", name);
                    String[] demo = branch.split(" ");
                    String brc = "";
                    for(String s: demo){
                        brc = brc+s;
                    }
                    Log.d(TAG, "onClick: brc short "+brc);
                    rootFB.child("facultyList").child(brc).child(getArguments().getString("key")).setValue(facUser);
//                    Map<String,String> userType = new HashMap<String, String>();
//                    userType.put(getArguments().getString("key"),"FACULTY");
                    rootFB.child("userType").child(getArguments().getString("key")).setValue("FACULTY");
                    Toast.makeText(getContext(), "User data inserted:"+getArguments().getString("email"), Toast.LENGTH_SHORT).show();
                    rootFB.child("newUser").child(getArguments().getString("key")).removeValue();
                    clearFields();
                }

            }
        });
        return view;
    }

    void clearFields(){
        etFirstName.setText("");
        etLastName.setText("");
        etFirstName.setText("");
    }

    private boolean inputValidation() {
        boolean valid = true;
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phoneNum = etPhoneNum.getText().toString().trim();

        if (firstName == null && !validatorHelper.isValidName(firstName)) {
            valid = false;
            etFirstName.setError("Enter valid first name.");
        }
        if (lastName == null && !validatorHelper.isValidName(lastName)) {
            valid = false;
            etLastName.setError("Enter valid last name.");
        }
        if (phoneNum == null && phoneNum.length() < 10) {
            valid = false;
            etPhoneNum.setError("Enter valid phone number");
        }
        return valid;
    }

    private void initialization(View view) {
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        spnBranch = (Spinner) view.findViewById(R.id.spnBranch);
        etPhoneNum = (EditText) view.findViewById(R.id.etPhoneNum);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        rootFB = new Firebase(FIREBASE_URL);
        validatorHelper = new InputValidatorHelper();
    }
}
