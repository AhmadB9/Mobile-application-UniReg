package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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

public class CreateInstructorActivity extends AppCompatActivity {
EditText edtInstructorId,edtInstructorName,edtInstructorPass,edtInstructorEmail,edtInstructorPhone,edtInstructorCer;
Button btnInstructorSubmit;
SharedPreferences sp;
SharedPreferences.Editor editor;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_instructor);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        edtInstructorCer=findViewById(R.id.edtIntructorCertificateup);
        edtInstructorId=findViewById(R.id.edtInstructorID);
        edtInstructorEmail=findViewById(R.id.edtInstructorEmailup);
        edtInstructorName=findViewById(R.id.edtInstructorNameup);
        edtInstructorPass=findViewById(R.id.edtInstructorPasswordup);
        edtInstructorPhone=findViewById(R.id.edtInstructorPhoneup);
        btnInstructorSubmit=findViewById(R.id.btnInstructorUpdate);
        sp=CreateInstructorActivity.this.getSharedPreferences("InstructorID",MODE_PRIVATE);
        editor=sp.edit();

        edtInstructorId.setText(sp.getString("id",""));
        btnInstructorSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,id,email,phone,pass,cert;
                name=edtInstructorName.getText().toString();
                email=edtInstructorEmail.getText().toString();
                id=edtInstructorId.getText().toString();
                phone=edtInstructorPhone.getText().toString();
                pass=edtInstructorPass.getText().toString();
                cert=edtInstructorCer.getText().toString();
                if(TextUtils.isEmpty(id) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||TextUtils.isEmpty(name)|| TextUtils.isEmpty(cert)){
                    if(TextUtils.isEmpty(id))edtInstructorId.setError("Please Enter Instructor's ID");
                    if(TextUtils.isEmpty(pass))edtInstructorPass.setError("Please Enter Instructor's password");
                    if(TextUtils.isEmpty(email))edtInstructorEmail.setError("Please Enter Instructor's Email");
                    if(TextUtils.isEmpty(phone))edtInstructorPhone.setError("Please Enter Instructor's phone number");
                    if(TextUtils.isEmpty(name))edtInstructorName.setError("Please Enter Instructor's name");
                    if(TextUtils.isEmpty(name))edtInstructorCer.setError("Please Enter Instructor's certificates");
                }
                else {
                    String url = "http://192.168.0.109/data/createUser.php?id=" + id + "&pass=" + pass+"&email="+email+"&name="+name+"&phone="+phone+"&type=instructor";
                    RequestQueue queue = Volley.newRequestQueue(CreateInstructorActivity.this);
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("success")) {
                                String url = "http://192.168.0.109/data/insertInstructorInfo.php?id=" + id + "&certificate=" +cert ;
                                RequestQueue queue = Volley.newRequestQueue(CreateInstructorActivity.this);
                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equalsIgnoreCase("success")){
                                            editor.putString("id","The last id used "+id);
                                            editor.commit();
                                            edtInstructorCer.setText("");
                                            edtInstructorName.setText("");
                                            edtInstructorEmail.setText("");
                                            edtInstructorId.setText(sp.getString("id",""));
                                            edtInstructorPass.setText("");
                                            edtInstructorPhone.setText("");
                                            Toast.makeText(getApplicationContext(), "Successfully created", Toast.LENGTH_SHORT).show();
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
                            else {
                                if(response.equalsIgnoreCase("exist"))
                                    Toast.makeText(getApplicationContext(), "This instructor is already exist" , Toast.LENGTH_SHORT).show();
                                else Toast.makeText(getApplicationContext(), "Creation failure.Maybe the ID used for another Instructor" , Toast.LENGTH_SHORT).show();}
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