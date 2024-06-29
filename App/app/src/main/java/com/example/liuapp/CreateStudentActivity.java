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

public class CreateStudentActivity extends AppCompatActivity {
EditText edtStudentId,edtStudentName,edtStudentPhone,edtStudentEmail,edtStudentPassword;
Button btnStudent;
SharedPreferences sp;
SharedPreferences.Editor editor;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        edtStudentEmail=findViewById(R.id.editDeleteStudentEmail);
        edtStudentId=findViewById(R.id.edtstudentid);
        edtStudentName=findViewById(R.id.edtDeleteStudentName);
        edtStudentPassword=findViewById(R.id.edtDeleteStudentPassword);
        edtStudentPhone=findViewById(R.id.edtDeleteStudentPhone);
        btnStudent=findViewById(R.id.btnUpdateStudent);
        sp=CreateStudentActivity.this.getSharedPreferences("StudentID",MODE_PRIVATE);
        editor=sp.edit();
        edtStudentId.setText(sp.getString("id",""));
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,id,email,phone,pass;
                name=edtStudentName.getText().toString();
                email=edtStudentEmail.getText().toString();
                id=edtStudentId.getText().toString();
                phone=edtStudentPhone.getText().toString();
                pass=edtStudentPassword.getText().toString();
                if(TextUtils.isEmpty(id) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)){
                    if(TextUtils.isEmpty(id))edtStudentId.setError("Please Enter student's ID");
                    if(TextUtils.isEmpty(pass))edtStudentPassword.setError("Please Enter student's password");
                    if(TextUtils.isEmpty(email))edtStudentEmail.setError("Please Enter student's Email");
                    if(TextUtils.isEmpty(phone))edtStudentPhone.setError("Please Enter student's phone number");
                    if(TextUtils.isEmpty(name))edtStudentName.setError("Please Enter student's name");
                }
                else {
                    String url = "http://192.168.0.109/data/createUser.php?id=" + id + "&pass=" + pass+"&email="+email+"&name="+name+"&phone="+phone+"&type=student";
                    RequestQueue queue = Volley.newRequestQueue(CreateStudentActivity.this);
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("success")) {
                                if (response.equalsIgnoreCase("success")){
                                    editor.putString("id","The last id used "+id);
                                    editor.commit();
                                    edtStudentName.setText("");
                                    edtStudentEmail.setText("");
                                    edtStudentId.setText(sp.getString("id",""));
                                    edtStudentPhone.setText("");
                                    edtStudentPassword.setText("");
                                    Toast.makeText(getApplicationContext(), "Successfully created", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                                if(response.equalsIgnoreCase("exist"))
                                Toast.makeText(getApplicationContext(), "This student is already exist" , Toast.LENGTH_SHORT).show();
                                else Toast.makeText(getApplicationContext(), "Creation failure.Maybe the ID used for another Student" , Toast.LENGTH_SHORT).show();
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
