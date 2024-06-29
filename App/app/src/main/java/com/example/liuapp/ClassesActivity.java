package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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

public class ClassesActivity extends AppCompatActivity {
ListView listView;
Spinner spSemester;
Intent i;
String [] s={""};
String iD;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes2);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        listView=findViewById(R.id.listView);
        spSemester=findViewById(R.id.spClass);
        i=getIntent();
        iD=i.getStringExtra("id");
        spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spSemester.getSelectedItem().toString().equalsIgnoreCase("choose")){
                    getClasses();
                }
                else {
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(ClassesActivity.this, android.R.layout.simple_list_item_1,s);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!spSemester.getSelectedItem().toString().equalsIgnoreCase("choose"))
            getClasses();
    }
    public void getClasses(){
        ArrayAdapter<String> adapter=new ArrayAdapter<>(ClassesActivity.this, android.R.layout.simple_list_item_1,s);
        listView.setAdapter(adapter);
        String Url = "http://192.168.0.109/data/allcoursesreg.php?id=" + iD+"&semester="+spSemester.getSelectedItem().toString();
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                    CustomAdapter costumeAdapter=new CustomAdapter(list,iD,ClassesActivity.this,spSemester.getSelectedItem().toString());
                    listView.setAdapter(costumeAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "You have not registered for any course or class yet", Toast.LENGTH_SHORT).show();
            }
        });
        Queue.add(objectRequest);
    }
}