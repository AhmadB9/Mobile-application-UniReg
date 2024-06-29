package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsCourseActivity extends AppCompatActivity {
    TextView course,credit,available,description;
    ListView lvInstructor;
    ArrayList<String>instructorArray=new ArrayList<>();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailscourse);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        course=findViewById(R.id.coursename);
        lvInstructor=findViewById(R.id.lvInstructor);
        available=findViewById(R.id.available);
        description=findViewById(R.id.description);
        credit=findViewById(R.id.credit);
        Intent i=getIntent();
        String t=i.getStringExtra("time");
        String name=i.getStringExtra("course");
        String inst=i.getStringExtra("instructor");
        String url = "http://192.168.0.109/data/detailscourse.php?name="+name;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String jCourse = "";
                StringBuilder jInstructor= new StringBuilder();
                String jCredit="";
                StringBuilder jAvailable= new StringBuilder();
                String jDescription="";
                String [] s;
                ArrayList<String>arrayList;
                arrayList=new ArrayList<>();
                StringBuilder ava= new StringBuilder();
                try {
                    JSONArray list = response.getJSONArray("list");
                    for (int i=0;i<list.length();i++){
                        JSONObject object=list.getJSONObject(i);
                        jCourse=object.getString("course");
                        if(!instructorArray.contains(object.getString("instructor")))
                            instructorArray.add(object.getString("instructor"));
                        jCredit=object.getString("credit");
                        jDescription=object.getString("description");
                        ava.append(object.getString("available"));
                    }
                    s= ava.toString().split("-",10000);
                    for (String value : s) {
                        if (!arrayList.contains(value))
                            arrayList.add(value);
                    }
                    ArrayAdapter<String> sAdapter=new ArrayAdapter<>(DetailsCourseActivity.this, android.R.layout.simple_list_item_1,instructorArray);
                    lvInstructor.setAdapter(sAdapter);
                    for(int i=0;i<arrayList.size();i++){
                        jAvailable.append(arrayList.get(i)).append(" ");
                    }
                    course.setText(jCourse);
                    credit.setText(jCredit);
                    available.setText(jAvailable.toString());
                    description.setText(jDescription);
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


















