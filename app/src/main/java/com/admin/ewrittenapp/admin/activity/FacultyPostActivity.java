package com.admin.ewrittenapp.admin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.admin.ewrittenapp.admin.R;

public class FacultyPostActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spnBranch, spnFaculty, spnPost, spnSem, spnDiv;
    Button btnSubmit;
    LinearLayout llClass;
    LinearLayout llSem;

    void initialization() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spnBranch = (Spinner) findViewById(R.id.spnBranch);
        spnFaculty = (Spinner) findViewById(R.id.spnFaculty);
        spnPost = (Spinner) findViewById(R.id.spnPost);
        spnSem = (Spinner) findViewById(R.id.spnSem);
        spnDiv = (Spinner) findViewById(R.id.spnDiv);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        llClass = (LinearLayout) findViewById(R.id.llClass);
        llSem = (LinearLayout) findViewById(R.id.llSem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_post);
        initialization();
        hideSpinnersByPost();
    }

    void hideSpinnersByPost() {
        spnPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                llClass.setVisibility(View.VISIBLE);
                llSem.setVisibility(View.VISIBLE);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        llClass.setVisibility(View.GONE);
                        break;
                    case 2:
                        llSem.setVisibility(View.GONE);
                        llClass.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


}
