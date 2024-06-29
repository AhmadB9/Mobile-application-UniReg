package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.TextView;
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
public class RegistrationActivity extends AppCompatActivity {
TextView txtStatus,txtMaxCredits,txtName,txtId;
Intent i;
Spinner sp,spCourse,spInstructor,spTime;
Button btnInstructorDe,btnSubmit,btnCourseDe;
int maxCredit,credit;
ArrayList<String> instructor= new ArrayList<>();
ArrayList<String> time= new ArrayList<>();
ArrayList<String> course= new ArrayList<>();

    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        btnCourseDe=findViewById(R.id.btnCouseDetails);
        btnSubmit=findViewById(R.id.btnReg);
        spTime =findViewById(R.id.spRegSection);
        btnInstructorDe=findViewById(R.id.btnInstructorDetails);
        spCourse=findViewById(R.id.spRegCourse);
        spInstructor=findViewById(R.id.spRegInstructor);
        txtStatus=findViewById(R.id.txtstatus);
        txtMaxCredits=findViewById(R.id.txtcredit);
        txtName=findViewById(R.id.txtname);
        txtId=findViewById(R.id.txtid1);
        sp=findViewById(R.id.sp);
        i=getIntent();
        String ID=i.getStringExtra("id");
        txtId.setText("ID: "+ID);
        String url = "http://192.168.0.109/data/userName.php?id=" + ID ;
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                txtName.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
        course.add(0,"choose");
        instructor.add(0,"choose");
        time.add(0,"choose");
        ArrayAdapter<String>adapter=new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1,course);
        spCourse.setAdapter(adapter);
        ArrayAdapter<String> tAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, time);
        spTime.setAdapter(tAdapter);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, instructor);
        spInstructor.setAdapter(sAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtStatus.setText("");
                txtMaxCredits.setText("");
                if (!sp.getSelectedItem().toString().equalsIgnoreCase("choose")) {
                    String Url = "http://192.168.0.109/data/status.php?ID=" + ID+"&semester="+sp.getSelectedItem().toString();
                    RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray list = response.getJSONArray("list");
                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    txtMaxCredits.setText(object.getString("MaxCredit"));
                                    txtStatus.setText(object.getString("Status"));
                                    maxCredit=object.getInt("MaxCredit");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"You don't have permission to register ", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Queue.add(objectRequest);
                        course.clear();
                        course.add(0,"choose");
                        String url = "http://192.168.0.109/data/requiredcourses.php?ID=" + ID + "&semester=" + sp.getSelectedItem().toString();
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
                                Toast.makeText(getApplicationContext(), "There are no courses available for this student in this semester", Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(jsonObjectRequest);
                        ArrayAdapter<String>adapter=new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1,course);
                        spCourse.setAdapter(adapter);

                }
                if(sp.getSelectedItem().toString().equalsIgnoreCase("choose")){
                    instructor.clear();
                    course.clear();
                    time.clear();
                    ArrayAdapter<String>adapter=new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1,course);
                    spCourse.setAdapter(adapter);
                    ArrayAdapter<String> tAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, time);
                    spTime.setAdapter(tAdapter);
                    ArrayAdapter<String> sAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, instructor);
                    spInstructor.setAdapter(sAdapter);
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
                instructor.add(0,"choose");
                time.clear();
                time.add(0,"choose");
                spInstructor.setSelection(0);
                spTime.setSelection(0);
                if (!spCourse.getSelectedItem().toString().equalsIgnoreCase("Choose")) {
                    String Url = "http://192.168.0.109/data/regcourseInfo.php?id=" + ID + "&course=" + spCourse.getSelectedItem().toString();
                    RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray list = response.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    String inst = object.getString("instructor");
                                    credit=object.getInt("credit");
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, instructor);
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
                String instructorSelected,courseSelected;
                instructorSelected=spInstructor.getSelectedItem().toString();
                courseSelected=spCourse.getSelectedItem().toString();
                time.clear();
                time.add(0,"choose");
                if (!courseSelected.equalsIgnoreCase("choose") && !instructorSelected.equalsIgnoreCase("choose")) {
                    String Url = "http://192.168.0.109/data/allCoursesreqtime.php?id=" + ID + "&course=" + spCourse.getSelectedItem().toString()+"&instructor="+spInstructor.getSelectedItem().toString();
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
                    ArrayAdapter<String> tAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, time);
                    spTime.setAdapter(tAdapter);
                }
        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnInstructorDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RegistrationActivity.this,DetailsInstructorActivity.class);
                if(!instructor.isEmpty()&&!spInstructor.getSelectedItem().toString().equalsIgnoreCase("choose")) {
                    i.putExtra("instructor",spInstructor.getSelectedItem().toString());
                    startActivity(i);
                }
                else Toast.makeText(getApplicationContext(),"Please choose instructor",Toast.LENGTH_LONG).show();
            }
        });
        btnCourseDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!spCourse.getSelectedItem().toString().equalsIgnoreCase("choose")&&!course.isEmpty()){
                    String name = spCourse.getSelectedItem().toString();
                    Intent i = new Intent(RegistrationActivity.this, DetailsCourseActivity.class);
                        i.putExtra("course", name);
                        startActivity(i);
                }
                else Toast.makeText(getApplicationContext(), "Please Choose course" , Toast.LENGTH_SHORT).show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb=new AlertDialog.Builder(RegistrationActivity.this);
                adb.setTitle("Alert");
                adb.setMessage("Are you sure?");
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String courseName, instr, t, s;
                        courseName = spCourse.getSelectedItem().toString();
                        instr = spInstructor.getSelectedItem().toString();
                        t = spTime.getSelectedItem().toString();
                        s = sp.getSelectedItem().toString();
                        if (courseName.equalsIgnoreCase("Choose") || instr.equalsIgnoreCase("Choose") || t.equalsIgnoreCase("Choose") || s.equalsIgnoreCase("Choose") ||time.isEmpty()||course.isEmpty()||instructor.isEmpty())
                            Toast.makeText(getApplicationContext(), "Please choose the data correctly and try again", Toast.LENGTH_SHORT).show();

                        else {
                            if (maxCredit > credit && txtStatus.getText().toString().equalsIgnoreCase("Unblocked")) {
                                String url = "http://192.168.0.109/data/enrollStudent.php?course=" + courseName + "&instructor=" + instr + "&id=" + ID + "&time=" + t + "&semester=" + s;
                                RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equalsIgnoreCase("registered"))
                                            Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_SHORT).show();
                                        if (response.equalsIgnoreCase("success")) {
                                            maxCredit -= credit;
                                            String url = "http://192.168.0.109/data/updateCredit.php?id=" + ID + "&maxCredit=" + maxCredit + "&semester=" + sp.getSelectedItem().toString();
                                            RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
                                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                                @SuppressLint("SetTextI18n")
                                                @Override
                                                public void onResponse(String response) {
                                                    if (response.equalsIgnoreCase("success")) {
                                                        txtMaxCredits.setText("" + maxCredit);
                                                        spCourse.setSelection(0);
                                                        time.clear();
                                                        instructor.clear();
                                                        instructor.add(0, "choose");
                                                        time.add(0, "choose");
                                                        ArrayAdapter<String> Adapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, course);
                                                        spCourse.setAdapter(Adapter);
                                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, instructor);
                                                        spInstructor.setAdapter(adapter);
                                                        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, time);
                                                        spTime.setAdapter(myAdapter);
                                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                                        spInstructor.setSelection(0);
                                                        spTime.setSelection(0);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                }
                                            });
                                            queue.add(request);
                                        }
                                        if (response.equalsIgnoreCase("conflict"))
                                            Toast.makeText(RegistrationActivity.this, "You have already registered another course at the same time\n.Please Change section and try again", Toast.LENGTH_SHORT).show();
                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                queue.add(request);
                            } else {
                                if (txtStatus.getText().toString().equalsIgnoreCase("Blocked"))
                                    Toast.makeText(RegistrationActivity.this, "You are blocked in this semester", Toast.LENGTH_SHORT).show();
                                else Toast.makeText(RegistrationActivity.this, "You can not register you have only"+maxCredit+"credits", Toast.LENGTH_SHORT).show();
                            }
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








