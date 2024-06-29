package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AdminActivity extends AppCompatActivity {
TextView adminName,adminId;
ListView adminList;
ImageView imageLogout;
Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        i=getIntent();
        adminList=findViewById(R.id.adminList);
        adminId=findViewById(R.id.adminId);
        adminName=findViewById(R.id.adminName);
        imageLogout=findViewById(R.id.imgAdminLogout);
        String ID=i.getStringExtra("id");
        adminId.setText("ID: "+ID);
        String url = "http://192.168.0.109/data/userName.php?id=" + ID ;
        RequestQueue queue = Volley.newRequestQueue(AdminActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                adminName.setText(response);
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
        adminList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case (0):
                        startActivity(new Intent(getApplicationContext(),CreateStudentActivity.class));
                        break;

                    case (1):
                        startActivity(new Intent(getApplicationContext(),CreateInstructorActivity.class));
                        break;
                    case (2):
                        startActivity(new Intent(getApplicationContext(),EnrollCoursesActivity.class));
                        break;
                    case (3):
                        startActivity(new Intent(getApplicationContext(),SetStatusActivity.class));
                        break;
                    case (4):
                        startActivity(new Intent(getApplicationContext(),StudentUpdateActivity.class));
                        break;

                    case (5):
                        startActivity(new Intent(getApplicationContext(),InstructorUpdatesActivity.class));
                        break;
                    case (6):
                        startActivity(new Intent(getApplicationContext(),EnrollStudentActivity.class));
                        break;
                    case (7):
                        Intent intent=new Intent(getApplicationContext(),ChangePassActivity.class);
                        intent.putExtra("id",ID);
                        startActivity(intent);
                        break;
                }
            }

        });
    }
}














