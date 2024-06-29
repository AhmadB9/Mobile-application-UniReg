package com.example.liuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnWeb, btnPass;
    EditText id,pass;
    CheckBox cb;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnWeb =findViewById(R.id.btnweb);
        btnLogin =findViewById(R.id.btnlogin);
        btnPass =findViewById(R.id.btnpss);
        id=findViewById(R.id.edtID);
        pass=findViewById(R.id.edtpass);
        cb=findViewById(R.id.cb);
        sp=MainActivity.this.getSharedPreferences("remember",MODE_PRIVATE);
        editor=sp.edit();
        id.setText(sp.getString("id",""));
        pass.setText(sp.getString("pass",""));
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri url=Uri.parse("https://www.liu.edu.lb");
                startActivity(new Intent(Intent.ACTION_VIEW,url));

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String Id=id.getText().toString();
            String password=pass.getText().toString();
            if(TextUtils.isEmpty(Id) || TextUtils.isEmpty(password)){
                if(TextUtils.isEmpty(Id))id.setError("Please entre your ID");
                if(TextUtils.isEmpty(password))pass.setError("Please entre your ID");
            }
            else {
                String url = "http://192.168.0.109/data/login.php?id=" + Id + "&pass=" + password;
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String type = response;
                        if (type.equalsIgnoreCase("student")) {
                            if(cb.isChecked()){
                            editor.putString("id",Id);
                            editor.putString("pass",password);
                            editor.commit();}
                            Intent i = new Intent(getApplicationContext(), StudentActivity.class);
                            i.putExtra("id", Id);
                            startActivity(i);
                        }
                        if (type.equalsIgnoreCase("administrator")) {
                            if(cb.isChecked()){
                            editor.putString("id",Id);
                            editor.putString("pass",password);
                            editor.commit();}
                            Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                            i.putExtra("id", Id);
                            startActivity(i);
                        }
                        if (type.equalsIgnoreCase("instructor")) {
                            if(cb.isChecked()){
                            editor.putString("id",Id);
                            editor.putString("pass",password);
                            editor.commit();}
                            Intent i = new Intent(getApplicationContext(), InstructorActivity.class);
                            i.putExtra("id", Id);
                            startActivity(i);
                        }
                        if(type.equals("empty")){
                            Toast.makeText(getApplicationContext(), "ID or Password is incorrect", Toast.LENGTH_SHORT).show();
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
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this,UpdatePassActivity.class));

            }
        });
    }
}