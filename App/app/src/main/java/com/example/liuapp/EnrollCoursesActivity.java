package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class EnrollCoursesActivity extends AppCompatActivity {
Spinner spEnrollStudent,spEnrollCourse,spEnrollSemester;
Button btnEnrollSubmit;
ArrayList <String> course=new ArrayList<String>();
ArrayList<String> studentID=new ArrayList<String>();
ArrayList<String> seme=new ArrayList<String>();
String [] s;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_courses);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        spEnrollCourse=findViewById(R.id.spEnrollCourse);
        spEnrollSemester=findViewById(R.id.spEnrollSemester);
        spEnrollStudent=findViewById(R.id.spEnrollStudent);
        btnEnrollSubmit=findViewById(R.id.btnEnrollSubmit);
        course.add(0,"choose");
        studentID.add(0,"choose");
        seme.add(0, "choose");
        String Url = "http://192.168.0.109/data/allCourses.php";
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
                Toast.makeText(getApplicationContext(), "No courses found", Toast.LENGTH_SHORT).show();
            }
        });Queue.add(objectRequest);
        ArrayAdapter<String>adapter=new ArrayAdapter<>(EnrollCoursesActivity.this, android.R.layout.simple_list_item_1,course);
        spEnrollCourse.setAdapter(adapter);
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
                Toast.makeText(getApplicationContext(), "No student's ID found", Toast.LENGTH_SHORT).show();
            }
        });queue.add(jsonObjectRequest);
        ArrayAdapter<String>myAdapter=new ArrayAdapter<>(EnrollCoursesActivity.this, android.R.layout.simple_list_item_1,studentID);
        spEnrollStudent.setAdapter(myAdapter);
        ArrayAdapter<String>sAdapter=new ArrayAdapter<>(EnrollCoursesActivity.this, android.R.layout.simple_list_item_1,seme);
        spEnrollSemester.setAdapter(sAdapter);
        spEnrollCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringBuilder av= new StringBuilder();
                seme.clear();
                seme.add(0, "choose");
                if (!spEnrollCourse.getSelectedItem().toString().equalsIgnoreCase("choose")) {
                    String url = "http://192.168.0.109/data/getSemester.php?course=" + spEnrollCourse.getSelectedItem().toString();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray list = response.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    av.append(object.getString("available"));
                                }
                                s=av.toString().split("-",10000);
                                for (String value : s) {
                                    if (!seme.contains(value))
                                        seme.add(value);
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
                    queue.add(jsonObjectRequest);
                    seme.remove(null);
                    ArrayAdapter<String>sAdapter=new ArrayAdapter<>(EnrollCoursesActivity.this, android.R.layout.simple_list_item_1,seme);
                    spEnrollSemester.setAdapter(sAdapter);
                }
            }
                @Override
                public void onNothingSelected (AdapterView < ? > parent){
                }

        });
        btnEnrollSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName,id,semester;
                if(spEnrollCourse.getSelectedItem().toString().equalsIgnoreCase("Choose")|| seme.isEmpty()||spEnrollStudent.getSelectedItem().toString().equalsIgnoreCase("Choose")){
                    Toast.makeText(getApplicationContext(), "Please Choose the data correctly" , Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!spEnrollSemester.getSelectedItem().toString().equalsIgnoreCase("choose")) {
                        courseName = spEnrollCourse.getSelectedItem().toString();
                        id = spEnrollStudent.getSelectedItem().toString();
                        semester = spEnrollSemester.getSelectedItem().toString();
                        String url = "http://192.168.0.109/data/enrollCourse.php?course=" + courseName + "&semester=" + semester + "&id=" + id;
                        RequestQueue queue = Volley.newRequestQueue(EnrollCoursesActivity.this);
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase("Already Enrolled"))
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                                else {
                                    if (response.equalsIgnoreCase("")){
                                        spEnrollCourse.setSelection(0);
                                        spEnrollStudent.setSelection(0);
                                        seme.clear();
                                        seme.add(0, "choose");
                                        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(EnrollCoursesActivity.this, android.R.layout.simple_list_item_1, seme);
                                        spEnrollSemester.setAdapter(sAdapter);
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                    }
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