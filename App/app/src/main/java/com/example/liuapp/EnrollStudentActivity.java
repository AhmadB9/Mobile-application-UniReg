package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class EnrollStudentActivity extends AppCompatActivity {
Spinner spStudentID,spCourse,spInstructor,spTime,spSemester;
Button btnSubmit;
ArrayList<String> studentID= new ArrayList<>();
ArrayList<String> course= new ArrayList<>();
ArrayList<String> instructor= new ArrayList<>();
ArrayList<String> time= new ArrayList<>();
ArrayList<String> semester= new ArrayList<>();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_student);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        spStudentID=findViewById(R.id.spEnrollStudentID);
        spCourse=findViewById(R.id.spEnrollStudentCourse);
        spInstructor=findViewById(R.id.spEnrollStudentInstructor);
        spTime=findViewById(R.id.spEnrollStudentTime);
        spSemester=findViewById(R.id.spEnrollStudentSemester);
        btnSubmit=findViewById(R.id.btnEnrollStudent);
        if(!studentID.contains("choose"))
            studentID.add(0,"choose");
        String url = "http://192.168.0.109/data/allStudentID.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                    for (int i=0;i<list.length();i++){
                        JSONObject object=list.getJSONObject(i);
                        studentID.add(object.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No Student's ID found", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
        ArrayAdapter<String> myAdapter=new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1,studentID);
        spStudentID.setAdapter(myAdapter);
        course.add(0,"choose");
        instructor.add(0,"choose");
        time.add(0,"choose");
        semester.add(0,"choose");
        ArrayAdapter<String>adapter1=new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1,semester);
        spSemester.setAdapter(adapter1);
        ArrayAdapter<String>adapter=new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1,course);
        spCourse.setAdapter(adapter);
        ArrayAdapter<String> tAdapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, time);
        spTime.setAdapter(tAdapter);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, instructor);
        spInstructor.setAdapter(sAdapter);

        spStudentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course.clear();
                instructor.clear();
                time.clear();
                semester.clear();
                if(!course.contains("choose")){
                    course.add(0,"choose");
                    spCourse.setSelection(0);
                }
                if (!spStudentID.getSelectedItem().toString().equalsIgnoreCase("choose")){
                    String Url = "http://192.168.0.109/data/allCoursesreq.php?id="+spStudentID.getSelectedItem().toString();
                    RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray list = response.getJSONArray("list");
                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String c=object.getString("course");
                                    if(!course.contains(c))
                                        course.add(c);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "There are no courses available for this student", Toast.LENGTH_SHORT).show();
                        }
                    });Queue.add(objectRequest);
                    ArrayAdapter<String>adapter=new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1,course);
                    spCourse.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                instructor.clear();
                time.clear();
                semester.clear();
                if(!instructor.contains("choose")){
                    instructor.add(0,"choose");
                    spInstructor.setSelection(0);
                }
                if(!semester.contains("choose")){
                    semester.add(0,"choose");
                    spSemester.setSelection(0);
                }
                time.clear();
                time.add(0,"choose");
                spTime.setSelection(0);
                if (!spCourse.getSelectedItem().toString().equalsIgnoreCase("Choose") && !spStudentID.getSelectedItem().toString().equalsIgnoreCase("Choose")) {
                    String Url = "http://192.168.0.109/data/allCoursesreqInfo.php?id=" + spStudentID.getSelectedItem().toString() + "&course=" + spCourse.getSelectedItem().toString();
                    RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray list = response.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    String inst = object.getString("instructor");
                                    String s=object.getString("semester");
                                    if(!semester.contains(s))
                                        semester.add(s);
                                    if (!instructor.contains(inst))
                                        instructor.add(inst);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Queue.add(objectRequest);
                    ArrayAdapter<String> sAdapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, semester);
                    spSemester.setAdapter(sAdapter);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, instructor);
                    spInstructor.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spInstructor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String idSelected,instructorSelected,courseSelected;
                idSelected=spStudentID.getSelectedItem().toString();
                instructorSelected=spInstructor.getSelectedItem().toString();
                courseSelected=spCourse.getSelectedItem().toString();
                time.clear();
                time.add(0,"choose");
                if (!courseSelected.equalsIgnoreCase("choose") && !instructorSelected.equalsIgnoreCase("choose") && !idSelected.equalsIgnoreCase("choose")) {
                    String Url = "http://192.168.0.109/data/allCoursesreqtime.php?id=" + spStudentID.getSelectedItem().toString() + "&course=" + spCourse.getSelectedItem().toString()+"&instructor="+spInstructor.getSelectedItem().toString();
                    RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray list = response.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    String t = object.getString("time");
                                    if (!time.contains(t))
                                        time.add(t);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Queue.add(objectRequest);
                    ArrayAdapter<String> tAdapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, time);
                    spTime.setAdapter(tAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb=new AlertDialog.Builder(EnrollStudentActivity.this);
                adb.setTitle("Alert");
                adb.setMessage("If any of data or information is missing,the application will crash.Are you sure?");
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id,courseName,instr,t,s;
                        id=spStudentID.getSelectedItem().toString();
                        courseName=spCourse.getSelectedItem().toString();
                        instr=spInstructor.getSelectedItem().toString();
                        t=spTime.getSelectedItem().toString();
                        s=spSemester.getSelectedItem().toString();
                        if(id.equalsIgnoreCase("Choose")||courseName.equalsIgnoreCase("Choose")||instr.equalsIgnoreCase("Choose")||t.equalsIgnoreCase("Choose")||s.equalsIgnoreCase("Choose"))
                            Toast.makeText(getApplicationContext(), "Please choose the data correctly and try again", Toast.LENGTH_SHORT).show();

                        else {
                            String url = "http://192.168.0.109/data/enrollStudent.php?course=" + courseName + "&instructor=" + instr + "&id=" + id + "&time=" + t + "&semester=" + s;
                            RequestQueue queue = Volley.newRequestQueue(EnrollStudentActivity.this);
                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equalsIgnoreCase("registered"))
                                        Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_SHORT).show();
                                    if (response.equalsIgnoreCase("success")) {
                                        spStudentID.setSelection(0);
                                        course.clear();
                                        time.clear();
                                        instructor.clear();
                                        semester.clear();
                                        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, semester);
                                        spSemester.setAdapter(sAdapter);
                                        ArrayAdapter<String> Adapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, course);
                                        spCourse.setAdapter(Adapter);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, instructor);
                                        spInstructor.setAdapter(adapter);
                                        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(EnrollStudentActivity.this, android.R.layout.simple_list_item_1, time);
                                        spTime.setAdapter(myAdapter);
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                    }
                                    if (response.equalsIgnoreCase("conflict"))
                                        Toast.makeText(EnrollStudentActivity.this, "You have already registered another course at the same time\n.Please Change section and try again", Toast.LENGTH_SHORT).show();
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
                adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
                adb.create().show();
                }
        });
    }
}