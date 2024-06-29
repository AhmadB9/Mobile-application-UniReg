package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class InstructorActivity extends AppCompatActivity {
    TextView txtInstructorName,txtInstructorId;
    ImageView imageGrades,imageChangePass,imageLogout,imageCourse;
    Intent i;
    String name;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        txtInstructorId=findViewById(R.id.txtInstructorId);
        txtInstructorName=findViewById(R.id.txtInstructorName);
        imageChangePass=findViewById(R.id.imageChangePass);
        imageGrades=findViewById(R.id.imagegrades);
        imageCourse=findViewById(R.id.imageCourse);
        imageLogout=findViewById(R.id.imageLogout);
        i=getIntent();
        String id=i.getStringExtra("id");
        txtInstructorId.setText("ID: "+id);
        String url = "http://192.168.0.109/data/userName.php?id=" + id ;
        RequestQueue queue = Volley.newRequestQueue(InstructorActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                txtInstructorName.setText("Name: "+response);
                name=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
        imageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        imageCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CreateCourseActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        imageGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),InsertGradesActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        imageChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChangePassActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }
}