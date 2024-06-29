package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CreateCourseActivity extends AppCompatActivity {
    EditText edtCourseName,edtCourseCode,edtCourseDescription;
    Spinner spTime,spCredit,spT;
    Button btnCourseSubmit;
    CheckBox rbFall,rbSummer,rbSpring;
    Intent i;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        edtCourseCode=findViewById(R.id.edtCourseCode);
        edtCourseDescription=findViewById(R.id.edtCourseDescription);
        edtCourseName=findViewById(R.id.edtCourseName);
        spT=findViewById(R.id.spT);
        spTime=findViewById(R.id.spTime);
        spCredit=findViewById(R.id.spCredit);
        rbFall=findViewById(R.id.rbFall);
        rbSpring=findViewById(R.id.rbSpring);
        rbSummer=findViewById(R.id.rbSummer);
        btnCourseSubmit=findViewById(R.id.btnCourseSubmit);
        i=getIntent();
        String name=i.getStringExtra("name");
        String id=i.getStringExtra("id");
        btnCourseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code,courseName,time,description,days;
                courseName=edtCourseName.getText().toString();
                code=edtCourseCode.getText().toString();
                time=spT.getSelectedItem().toString();
                days=spTime.getSelectedItem().toString();
                description=edtCourseDescription.getText().toString();
                if(TextUtils.isEmpty(courseName) || TextUtils.isEmpty(code) || time.equalsIgnoreCase("choose") || TextUtils.isEmpty(description) || days.equals("Choose")|| spCredit.getSelectedItem().toString().equals("Choose") ||(!rbFall.isChecked() && !rbSummer.isChecked() && !rbSpring.isChecked())){
                    if(TextUtils.isEmpty(code))edtCourseCode.setError("Please Enter course's code");
                    if(TextUtils.isEmpty(courseName))edtCourseName.setError("Please Enter course's name");
                    if(spT.getSelectedItem().toString().equals("Choose")) Toast.makeText(getApplicationContext(), "Please Choose the time", Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(description))edtCourseDescription.setError("Please Enter course's description");
                    if(spTime.getSelectedItem().toString().equals("Choose")) Toast.makeText(getApplicationContext(), "Please Choose the Days", Toast.LENGTH_SHORT).show();
                    if(spCredit.getSelectedItem().toString().equals("Choose")) Toast.makeText(getApplicationContext(), "Please Choose course credit", Toast.LENGTH_SHORT).show();
                    if(!rbFall.isChecked() && !rbSummer.isChecked() && !rbSpring.isChecked())Toast.makeText(getApplicationContext(), "Please select the semesters in which this course will be available", Toast.LENGTH_SHORT).show();
                }
                else {
                    String fullTime=days + time;
                    String available="";
                    if(rbFall.isChecked())
                        available+=rbFall.getText().toString()+"-";
                    if(rbSpring.isChecked())
                        available+=rbSpring.getText().toString()+"-";
                    if(rbSummer.isChecked())
                        available+=rbSummer.getText().toString()+"-";
                    String url = "http://192.168.0.109/data/addCourse.php?course=" + courseName + "&instructor="+name+"&credit=" + spCredit.getSelectedItem().toString()+"&time="+fullTime+"&code="+code+"&available="+available+"&description="+description+"&id="+id;
                    RequestQueue queue = Volley.newRequestQueue(CreateCourseActivity.this);
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")){
                                edtCourseCode.setText("");
                                edtCourseDescription.setText("");
                                edtCourseName.setText("");
                                spT.setSelection(0);
                                rbFall.setChecked(false);
                                rbSummer.setChecked(false);
                                rbSpring.setChecked(false);
                                spCredit.setSelection(0);
                                spTime.setSelection(0);
                                Toast.makeText(getApplicationContext(), "Successfully created", Toast.LENGTH_SHORT).show();
                            }
                            if(response.equalsIgnoreCase("exist")){
                                String a="";
                                if(rbFall.isChecked())
                                    a+=rbFall.getText().toString()+"-";
                                if(rbSpring.isChecked())
                                    a+=rbSpring.getText().toString()+"-";
                                if(rbSummer.isChecked())
                                    a+=rbSummer.getText().toString();
                                AlertDialog.Builder adb=new AlertDialog.Builder(CreateCourseActivity.this);
                                adb.setTitle("Exist");
                                adb.setMessage("This course is already created.Do you want to replace it");
                                String finalA = a;
                                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = "http://192.168.0.109/data/updateCourse.php?course=" + courseName + "&credit=" + spCredit.getSelectedItem().toString() + "&time=" + fullTime + "&code=" + code + "&available=" + finalA + "&description=" + description + "&id=" + id;
                                        RequestQueue queue = Volley.newRequestQueue(CreateCourseActivity.this);
                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if(response.equalsIgnoreCase("success")){
                                                edtCourseCode.setText("");
                                                edtCourseDescription.setText("");
                                                edtCourseName.setText("");
                                                spT.setSelection(0);
                                                rbFall.setChecked(false);
                                                rbSummer.setChecked(false);
                                                rbSpring.setChecked(false);
                                                spCredit.setSelection(0);
                                                spTime.setSelection(0);
                                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();}
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                        queue.add(request);
                                    }
                                });
                                adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                    }
                                });
                                adb.create().show();
                            }
                            if(response.equalsIgnoreCase("same time"))
                                Toast.makeText(getApplicationContext(), "You have already created another course in the same time" , Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);
                }
                }
        });
    }
}