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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InsertGradesActivity extends AppCompatActivity {
Spinner spCourse,spStudentID;
EditText edtGrade;
Button btnSubmit;
ArrayList<String> studentID= new ArrayList<>();
ArrayList<String> course= new ArrayList<>();
Intent i;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_grades);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        spCourse=findViewById(R.id.spGradeCourse);
        spStudentID=findViewById(R.id.spGradeStudentID);
        btnSubmit=findViewById(R.id.btnInserGrade);
        edtGrade=findViewById(R.id.edtGrade);
        i=getIntent();
        if(!course.contains("choose"))
            course.add(0, "choose");
            String name = i.getStringExtra("name");
            String Url = "http://192.168.0.109/data/instructorAllCourses.php?name=" + name;
            RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray list = response.getJSONArray("list");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject object = list.getJSONObject(i);
                            String c = object.getString("course");
                            if (!course.contains(c))
                                course.add(c);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "You have not created any course yet ", Toast.LENGTH_SHORT).show();
                }
            });
            Queue.add(objectRequest);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(InsertGradesActivity.this, android.R.layout.simple_list_item_1, course);
            spCourse.setAdapter(adapter);
            spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    studentID.clear();
                    studentID.add(0,"choose");
                    if (!spCourse.getSelectedItem().toString().equalsIgnoreCase("choose")){
                        String Url = "http://192.168.0.109/data/allStudentIdreg.php?course="+spCourse.getSelectedItem().toString();
                        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
                        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray list = response.getJSONArray("list");
                                    for (int i=0;i<list.length();i++){
                                        JSONObject object=list.getJSONObject(i);
                                        String c=object.getString("id");
                                        if(!studentID.contains(c))
                                            studentID.add(c);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "No student has yet registered for this course" , Toast.LENGTH_SHORT).show();
                            }
                        });Queue.add(objectRequest);
                        ArrayAdapter<String>adapter=new ArrayAdapter<>(InsertGradesActivity.this, android.R.layout.simple_list_item_1,studentID);
                        spStudentID.setAdapter(adapter);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        spStudentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spCourse.getSelectedItem().toString().equalsIgnoreCase("Choose") && !spStudentID.getSelectedItem().toString().equalsIgnoreCase("Choose")) {
                    String url = "http://192.168.0.109/data/studentGrade.php?id=" + spStudentID.getSelectedItem().toString() + "&course=" + spCourse.getSelectedItem().toString();
                    RequestQueue queue = Volley.newRequestQueue(InsertGradesActivity.this);
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            edtGrade.setText(response);
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spCourse.getSelectedItem().toString().equalsIgnoreCase("Choose") && !spStudentID.getSelectedItem().toString().equalsIgnoreCase("Choose") && !TextUtils.isEmpty(edtGrade.getText().toString())){
                    if(TextUtils.isEmpty(edtGrade.getText().toString()))edtGrade.setError("Please enter the grade");
                    if(edtGrade.getText().toString().equalsIgnoreCase("0")){
                        AlertDialog.Builder adb=new AlertDialog.Builder(InsertGradesActivity.this);
                        adb.setTitle("Alert");
                        adb.setMessage("Are you sure that grade is zero ?");
                        adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "http://192.168.0.109/data/InsertGrade.php?id=" +spStudentID.getSelectedItem().toString()+"&course="+spCourse.getSelectedItem().toString()+"&grade="+edtGrade.getText().toString() ;
                                RequestQueue queue = Volley.newRequestQueue(InsertGradesActivity.this);
                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equalsIgnoreCase("success")){
                                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                                    }}
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                queue.add(request);
                            }
                        });
                        adb.create().show();
                    }
                    else {
                        String url = "http://192.168.0.109/data/InsertGrade.php?id=" +spStudentID.getSelectedItem().toString()+"&course="+spCourse.getSelectedItem().toString()+"&grade="+edtGrade.getText().toString() ;
                        RequestQueue queue = Volley.newRequestQueue(InsertGradesActivity.this);
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                    edtGrade.setText("");
                                    spCourse.setSelection(0);
                                    studentID.clear();
                                    ArrayAdapter<String>adapter=new ArrayAdapter<>(InsertGradesActivity.this, android.R.layout.simple_list_item_1,studentID);
                                    spStudentID.setAdapter(adapter);
                                }
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
            }
        });
    }

}
