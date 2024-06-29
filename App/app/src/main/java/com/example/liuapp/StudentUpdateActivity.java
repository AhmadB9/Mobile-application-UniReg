package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

public class StudentUpdateActivity extends AppCompatActivity {
Button btnUpdate,btnDelete;
EditText edtFullName,edtPass,edtEmail,edtPhone;
Spinner spStudentID;
ArrayList<String> StudentId=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_student_update);
        btnDelete=findViewById(R.id.btnDeleteStudent);
        btnUpdate=findViewById(R.id.btnUpdateStudent);
        edtEmail=findViewById(R.id.edtDeleteStudentEmail);
        edtFullName=findViewById(R.id.edtDeleteStudentName);
        edtPass=findViewById(R.id.edtDeleteStudentPassword);
        edtPhone=findViewById(R.id.edtDeleteStudentPhone);
        spStudentID=findViewById(R.id.spDeleteStudentID);
        getAllStudentID();
        spStudentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spStudentID.getSelectedItem().toString().equalsIgnoreCase("Choose")) {
                    String url = "http://192.168.0.109/data/allStudentInfo.php?id=" + spStudentID.getSelectedItem().toString();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray list = response.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    edtEmail.setText(object.getString("email"));
                                    edtPass.setText(object.getString("pass"));
                                    edtFullName.setText(object.getString("name"));
                                    edtPhone.setText(object.getString("phone"));
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
                }
                }

                @Override
                public void onNothingSelected (AdapterView < ? > parent){

                }

        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, id, email, phone, pass;
                name = edtFullName.getText().toString();
                email = edtEmail.getText().toString();
                id = spStudentID.getSelectedItem().toString();
                phone = edtPhone.getText().toString();
                pass = edtPass.getText().toString();
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
                    if (TextUtils.isEmpty(pass)) edtPass.setError("Please Enter student's password");
                    if (TextUtils.isEmpty(email)) edtEmail.setError("Please Enter student's Email");
                    if (TextUtils.isEmpty(phone)) edtPhone.setError("Please Enter student's phone number");
                    if (TextUtils.isEmpty(name)) edtFullName.setError("Please Enter student's name");
                    if (id.equalsIgnoreCase("choose")) Toast.makeText(getApplicationContext(), "Please select student's ID", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder adb=new AlertDialog.Builder(StudentUpdateActivity.this);
                    adb.setTitle("Update");
                    adb.setMessage("Are you sure?Do you want to update this student's data.");
                    adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url = "http://192.168.0.109/data/UpdateUser.php?id=" + id + "&pass=" + pass + "&email=" + email + "&name=" + name + "&phone=" + phone;
                            RequestQueue queue = Volley.newRequestQueue(StudentUpdateActivity.this);
                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equalsIgnoreCase("success")) {
                                        if (response.equalsIgnoreCase("success")) {
                                            edtFullName.setText("");
                                            edtEmail.setText("");
                                            spStudentID.setSelection(0);
                                            edtPhone.setText("");
                                            edtPass.setText("");
                                            Toast.makeText(getApplicationContext(), "The data of student has been successfully updated", Toast.LENGTH_SHORT).show();
                                        }
                                    } else
                                        Toast.makeText(getApplicationContext(), "Creation failure.Maybe the ID used for another Student", Toast.LENGTH_SHORT).show();
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
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb=new AlertDialog.Builder(StudentUpdateActivity.this);
                adb.setTitle("Delete");
                adb.setMessage("Are you sure?Do you want to delete this student.");
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://192.168.0.109/data/deleteUser.php?id=" + spStudentID.getSelectedItem().toString();
                        RequestQueue queue = Volley.newRequestQueue(StudentUpdateActivity.this);
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "Student has been successfully deleted", Toast.LENGTH_SHORT).show();
                                    edtFullName.setText("");
                                    edtEmail.setText("");
                                    spStudentID.setSelection(0);
                                    edtPhone.setText("");
                                    edtPass.setText("");
                                    getAllStudentID();

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
    public void getAllStudentID(){
        StudentId.clear();
        StudentId.add(0,"choose");
        String url = "http://192.168.0.109/data/allStudentID.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                    for (int i=0;i<list.length();i++){
                        JSONObject object=list.getJSONObject(i);
                        StudentId.add(object.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No student's ID found" , Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
        ArrayAdapter<String> myAdapter=new ArrayAdapter<>(StudentUpdateActivity.this, android.R.layout.simple_list_item_1,StudentId);
        spStudentID.setAdapter(myAdapter);
    }
}