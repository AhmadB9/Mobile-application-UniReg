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

public class InstructorUpdatesActivity extends AppCompatActivity {
EditText edtInstructorName,edtInstructorPass,edtInstructorEmail,edtInstructorPhone,edtInstructorCer;
Button btnInstructorUpdate,btnInstructorDelete;
Spinner spInstructorID;
ArrayList<String> InstructorId=new ArrayList<String>();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_updates);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        edtInstructorCer=findViewById(R.id.edtIntructorCertificateup);
        edtInstructorEmail=findViewById(R.id.edtInstructorEmailup);
        edtInstructorName=findViewById(R.id.edtInstructorNameup);
        edtInstructorPass=findViewById(R.id.edtInstructorPasswordup);
        edtInstructorPhone=findViewById(R.id.edtInstructorPhoneup);
        btnInstructorUpdate=findViewById(R.id.btnInstructorUpdate);
        btnInstructorDelete=findViewById(R.id.btnInstructorDelete);
        spInstructorID=findViewById(R.id.spDeleteInstructor);
        getInstructorID();
        spInstructorID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spInstructorID.getSelectedItem().toString().equalsIgnoreCase("Choose")) {
                String url = "http://192.168.0.109/data/allInstructorInfo.php?id="+spInstructorID.getSelectedItem().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray list = response.getJSONArray("list");
                            for (int i=0;i<list.length();i++){
                                JSONObject object=list.getJSONObject(i);
                                edtInstructorEmail.setText(object.getString("email"));
                                edtInstructorPass.setText(object.getString("pass"));
                                edtInstructorName.setText(object.getString("name"));
                                edtInstructorPhone.setText(object.getString("phone"));
                                edtInstructorCer.setText(object.getString("certificate"));
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnInstructorUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,id,email,phone,pass,cert;
                name=edtInstructorName.getText().toString();
                email=edtInstructorEmail.getText().toString();
                id=spInstructorID.getSelectedItem().toString();
                phone=edtInstructorPhone.getText().toString();
                pass=edtInstructorPass.getText().toString();
                cert=edtInstructorCer.getText().toString();
                if(id.equalsIgnoreCase("Choose") || TextUtils.isEmpty(pass) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||TextUtils.isEmpty(name)|| TextUtils.isEmpty(cert)){
                    if(TextUtils.isEmpty(pass))edtInstructorPass.setError("Please Enter Instructor's password");
                    if(TextUtils.isEmpty(email))edtInstructorEmail.setError("Please Enter Instructor's Email");
                    if(TextUtils.isEmpty(phone))edtInstructorPhone.setError("Please Enter Instructor's phone number");
                    if(TextUtils.isEmpty(name))edtInstructorName.setError("Please Enter Instructor's name");
                    if(TextUtils.isEmpty(name))edtInstructorCer.setError("Please Enter Instructor's certificates");
                    if(id.equalsIgnoreCase("Choose"))Toast.makeText(getApplicationContext(), "Please select instructor's ID and try again", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder adb=new AlertDialog.Builder(InstructorUpdatesActivity.this);
                    adb.setTitle("Update");
                    adb.setMessage("Are you sure?Do you want to update this instructor's data.");
                    adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url = "http://192.168.0.109/data/UpdateUser.php?id=" + id + "&pass=" + pass+"&email="+email+"&name="+name+"&phone="+phone+"&type=instructor";
                            RequestQueue queue = Volley.newRequestQueue(InstructorUpdatesActivity.this);
                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equalsIgnoreCase("success")) {
                                        String url = "http://192.168.0.109/data/UpdateInstructorInfo.php?id=" + id + "&certificate=" +cert ;
                                        RequestQueue queue = Volley.newRequestQueue(InstructorUpdatesActivity.this);
                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equalsIgnoreCase("success")){
                                                    edtInstructorCer.setText("");
                                                    edtInstructorName.setText("");
                                                    edtInstructorEmail.setText("");
                                                    spInstructorID.setSelection(0);
                                                    edtInstructorPass.setText("");
                                                    edtInstructorPhone.setText("");
                                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
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
                                    else Toast.makeText(getApplicationContext(), "Creation failure" , Toast.LENGTH_SHORT).show();
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
        btnInstructorDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb=new AlertDialog.Builder(InstructorUpdatesActivity.this);
                adb.setTitle("Delete");
                adb.setMessage("Are you sure?Do you want to delete this instructor.");
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://192.168.0.109/data/deleteUser.php?id=" + spInstructorID.getSelectedItem().toString();
                        RequestQueue queue = Volley.newRequestQueue(InstructorUpdatesActivity.this);
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "Instructor has been successfully deleted", Toast.LENGTH_SHORT).show();
                                    edtInstructorPhone.setText("");
                                    edtInstructorPass.setText("");
                                    spInstructorID.setSelection(0);
                                    edtInstructorEmail.setText("");
                                    edtInstructorName.setText("");
                                    edtInstructorCer.setText("");
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
    public void getInstructorID(){
        InstructorId.clear();
        InstructorId.add(0,"Choose");
        String url = "http://192.168.0.109/data/allInstructorID.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                    for (int i=0;i<list.length();i++){
                        JSONObject object=list.getJSONObject(i);
                        InstructorId.add(object.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No instructor's ID found", Toast.LENGTH_SHORT).show();
            }
        });queue.add(jsonObjectRequest);
        ArrayAdapter<String> myAdapter=new ArrayAdapter<>(InstructorUpdatesActivity.this, android.R.layout.simple_list_item_1,InstructorId);
        spInstructorID.setAdapter(myAdapter);
    }
}