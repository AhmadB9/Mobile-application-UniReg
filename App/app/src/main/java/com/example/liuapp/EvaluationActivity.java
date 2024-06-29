package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class EvaluationActivity extends AppCompatActivity {
EditText edtComment;
Button btnSubmit;
RatingBar ratingBar;
Spinner spInstructor;
Intent i;
CheckBox cbBad,cbPerfect,cbInter;
ArrayList<String> instructor= new ArrayList<>();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        i=getIntent();
        String id=i.getStringExtra("id");
        cbBad=findViewById(R.id.cbbad);
        cbInter=findViewById(R.id.cbInter);
        cbPerfect=findViewById(R.id.cbperfect);
        edtComment=findViewById(R.id.edtEvaInstructor);
        btnSubmit=findViewById(R.id.btnEvaluation);
        ratingBar=findViewById(R.id.ratingBar);
        spInstructor=findViewById(R.id.spEvalInstructor);
        instructor.clear();
        instructor.add(0,"choose");
        String Url = "http://192.168.0.109/data/allInstructor.php?id=" + id;
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = list.getJSONObject(i);
                        String inst = object.getString("instructor");
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
                Toast.makeText(getApplicationContext(), "You do not have instructors to evaluate them", Toast.LENGTH_SHORT).show();
            }
        });
        Queue.add(objectRequest);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EvaluationActivity.this, android.R.layout.simple_list_item_1, instructor);
        spInstructor.setAdapter(adapter);
        cbPerfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbPerfect.isChecked()){
                    cbInter.setChecked(false);
                    cbBad.setChecked(false);
                }
            }
        });
        cbBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbBad.isChecked()){
                    cbInter.setChecked(false);
                    cbPerfect.setChecked(false);
                }
            }
        });
        cbInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbInter.isChecked()){
                    cbBad.setChecked(false);
                    cbPerfect.setChecked(false);
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rate;
                String performance="";
                String comment="null";
                if(!TextUtils.isEmpty(edtComment.getText().toString()))
                    comment=edtComment.getText().toString();
                String name=spInstructor.getSelectedItem().toString();
                rate=ratingBar.getRating();
                if(cbPerfect.isChecked())performance=cbPerfect.getText().toString();
                else if(cbBad.isChecked())performance=cbBad.getText().toString();
                else if(cbInter.isChecked())performance=cbInter.getText().toString();
                if((!cbInter.isChecked() && !cbBad.isChecked()&&!cbPerfect.isChecked())|| rate==0 || name.equalsIgnoreCase("choose"))
                    Toast.makeText(getApplicationContext(), "Please enter your information correctly", Toast.LENGTH_SHORT).show();
                else{
                    String url = "http://192.168.0.109/data/evaluation.php?rate=" + rate+"&comment="+comment+"&instructor="+name+"&performance="+performance;
                    RequestQueue queue = Volley.newRequestQueue(EvaluationActivity.this);
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("success")) {
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                edtComment.setText("");
                                spInstructor.setSelection(0);
                                ratingBar.setRating(0);
                                cbBad.setChecked(false);
                                cbInter.setChecked(false);
                                cbPerfect.setChecked(false);
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
        });
    }
}