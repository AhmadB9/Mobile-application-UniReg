package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class StudentActivity extends AppCompatActivity {
    Intent i;
    TextView txtId,txtStudentName;
    ListView listView;
    ImageView image,imgStudentLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        imgStudentLogout=findViewById(R.id.imgStudentLogout);
        image=findViewById(R.id.image);
        txtId=findViewById(R.id.txtid);
        txtStudentName=findViewById(R.id.txtStudentName);
        listView=findViewById(R.id.studentlistview);
        i=getIntent();
        String ID=i.getStringExtra("id");
        txtId.setText(ID);
        String url = "http://192.168.0.109/data/userName.php?id=" + ID ;
        RequestQueue queue = Volley.newRequestQueue(StudentActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                txtStudentName.setText("Name: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case (0):
                        Intent intent=new Intent(getApplicationContext(),RegistrationActivity.class);
                        intent.putExtra("id",ID);
                        startActivity(intent);
                        break;

                    case (1):
                        Intent a=new Intent(getApplicationContext(),ClassesActivity.class);
                        a.putExtra("id",ID);
                        startActivity(a);
                        break;
                    case (2):
                        Intent b=new Intent(getApplicationContext(),PaymentsActivity.class);
                        b.putExtra("id",ID);
                        startActivity(b);
                        break;

                    case (3):
                        Intent d=new Intent(getApplicationContext(),OnlineServiceActivity.class);
                        d.putExtra("id",ID);
                        startActivity(d);
                        break;
                    case (4):
                        Intent e=new Intent(getApplicationContext(),EvaluationActivity.class);
                        e.putExtra("id",ID);
                        startActivity(e);
                        break;
                    case (5):
                        Intent f=new Intent(getApplicationContext(),ChangePassActivity.class);
                        f.putExtra("id",ID);
                        startActivity(f);
                }
            }
        });
        imgStudentLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}