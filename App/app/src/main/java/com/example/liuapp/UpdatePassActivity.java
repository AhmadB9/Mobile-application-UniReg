package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class UpdatePassActivity extends AppCompatActivity {
Button btnSubmit;
EditText edId,edName,edEmail,edPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass);
        btnSubmit=findViewById(R.id.btnsubmit);
        edId=findViewById(R.id.EdID);
        edName=findViewById(R.id.EdName);
        edEmail=findViewById(R.id.EdEmail);
        edPhone=findViewById(R.id.editphone);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edEmail.getText().toString();
                String name=edName.getText().toString();
                String id=edId.getText().toString();
                String phone=edPhone.getText().toString();
                if(TextUtils.isEmpty(id) ||TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)){
                    if(TextUtils.isEmpty(id))edId.setError("Please enter your ID");
                    if(TextUtils.isEmpty(name))edName.setError("Please enter your Name");
                    if(TextUtils.isEmpty(email))edEmail.setError("Please enter your Email");
                    if(TextUtils.isEmpty(phone))edPhone.setError("Please enter your phone number");
                }
                else{
                    String url = "http://192.168.0.109/data/authentication.php?id=" + id + "&name=" +name+"&email="+email+"&phone="+phone;
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("success")){
                              Intent i=new Intent(getApplicationContext(),ChangePassActivity.class);
                              i.putExtra("id",id);
                              i.putExtra("v",1);
                              Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                              startActivity(i);
                            }
                            else if(response.equals("empty")) Toast.makeText(getApplicationContext(), "Error:Check your info" , Toast.LENGTH_SHORT).show();
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