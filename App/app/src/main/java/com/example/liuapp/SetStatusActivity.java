package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class SetStatusActivity extends AppCompatActivity {
Spinner spStatus,spSemester,spStudentID;
Button btnSubmit;
EditText edtSetMaxCredit;
ArrayList<String> StudentId=new ArrayList<String>();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_status);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        spSemester=findViewById(R.id.spSetSemester);
        spStatus=findViewById(R.id.spSetStatus);
        spStudentID=findViewById(R.id.spStudentID);
        btnSubmit=findViewById(R.id.btnSetSubmit);
        edtSetMaxCredit=findViewById(R.id.edtSetMaxCredits);
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
                Toast.makeText(getApplicationContext(), "No Student's ID found", Toast.LENGTH_SHORT).show();
            }
        });queue.add(jsonObjectRequest);
        ArrayAdapter<String> myAdapter=new ArrayAdapter<>(SetStatusActivity.this, android.R.layout.simple_list_item_1,StudentId);
        spStudentID.setAdapter(myAdapter);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status,id,semester,credit;
                credit=edtSetMaxCredit.getText().toString();
                id=spStudentID.getSelectedItem().toString();
                semester=spSemester.getSelectedItem().toString();
                status=spStatus.getSelectedItem().toString();
                if(id.equalsIgnoreCase("Choose")|| semester.equalsIgnoreCase("choose")||status.equalsIgnoreCase("Choose")|| TextUtils.isEmpty(credit)){
                    Toast.makeText(getApplicationContext(), "Please Choose the data correctly" , Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(credit))edtSetMaxCredit.setError("Please enter numbers of credits");
                }
                else {
                    String url = "http://192.168.0.109/data/insertStatus.php?id=" + id + "&semester=" + semester + "&maxCredit=" + credit+"&status="+status;
                    RequestQueue queue = Volley.newRequestQueue(SetStatusActivity.this);
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("success")){
                                spStatus.setSelection(0);
                                spSemester.setSelection(0);
                                spStudentID.setSelection(0);
                                edtSetMaxCredit.setText("");
                                Toast.makeText(getApplicationContext(), "Success" , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                AlertDialog.Builder adb=new AlertDialog.Builder(SetStatusActivity.this);
                                adb.setTitle("Replace");
                                adb.setMessage("This data is already exist.Do you want to replace it.");
                                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = "http://192.168.0.109/data/UpdateStatus.php?id=" + id + "&semester=" + semester + "&maxCredit=" + credit+"&status="+status;
                                        RequestQueue queue = Volley.newRequestQueue(SetStatusActivity.this);
                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if(response.equalsIgnoreCase("success")){
                                                    spStatus.setSelection(0);
                                                    spSemester.setSelection(0);
                                                    spStudentID.setSelection(0);
                                                    edtSetMaxCredit.setText("");
                                                    Toast.makeText(getApplicationContext(), "Success" , Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                        }
                                    }); queue.add(request);
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
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                });queue.add(request);
                }
            }
        });
    }
}