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

public class DetailsInstructorActivity extends AppCompatActivity {
TextView name,email,certificate,rate,voters,performance;
ListView lvFeedback;
    String [] f;
    String [] p;
    int totalVoters;
    ArrayList<String> feedBack=new ArrayList<>();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_instructor);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        name=findViewById(R.id.txtInstructorDName);
        email=findViewById(R.id.txtInstructorEmail);
        certificate=findViewById(R.id.txtInstructorCer);
        rate=findViewById(R.id.txtRate);
        voters=findViewById(R.id.txtVoters);
        lvFeedback=findViewById(R.id.lvfeedback);
        performance=findViewById(R.id.txtperformance);
        Intent i=getIntent();
        String url = "http://192.168.0.109/data/detailInstructor.php?name=" +i.getStringExtra("instructor");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                    for (int i=0;i<list.length();i++){
                        JSONObject object=list.getJSONObject(i);
                        name.setText(object.getString("name"));
                        email.setText(object.getString("email"));
                        certificate.setText(object.getString("certificates"));
                        voters.setText(object.getString("voters"));
                        totalVoters=object.getInt("voters");
                        if(object.getInt("rate")!=0)
                            rate.setText(object.getString("rate"));

                        if(!object.getString("feedback").equalsIgnoreCase("null")){
                            String feed=object.getString("feedback");
                            f=feed.split(":",10000000);
                            for (String value : f) {
                                if (!feedBack.contains(value))
                                    feedBack.add(value);
                            }
                            ArrayAdapter<String> sAdapter=new ArrayAdapter<>(DetailsInstructorActivity.this, android.R.layout.simple_list_item_1,feedBack);
                            lvFeedback.setAdapter(sAdapter);
                        }
                        if(!object.getString("performance").equalsIgnoreCase("null")) {
                            String per=object.getString("performance");
                            int countP=0,countI=0,countB=0;
                            p = per.split(":", 10000);
                            for (String value : p) {
                                if(value.equals("Perfect"))
                                    countP++;
                                if(value.equals("Intermediate"))
                                    countI++;
                                if(value.equals("Bad"))
                                    countB++;
                            }
                            performance.setText(countP +" students said that the performance of this instructor is perfect,"+countI+" said intermediate and "+countB+" said is bad");
                        }
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


















