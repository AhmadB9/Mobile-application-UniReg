package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ChangePassActivity extends AppCompatActivity {
    EditText newPass,ConfirmPass;
    Button submit;
    CheckBox cbSave;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        Intent i=getIntent();
        String id=i.getStringExtra("id");
        int value=i.getIntExtra("v",0);
        newPass=findViewById(R.id.editTextNewPassword);
        ConfirmPass=findViewById(R.id.editTextConfirmPassword);
        submit=findViewById(R.id.submit);
        cbSave=findViewById(R.id.cbsave);
        sp=getSharedPreferences("remember",MODE_PRIVATE);
        editor=sp.edit();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = newPass.getText().toString();
                String confirmPass = ConfirmPass.getText().toString();
                if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)) {
                    if (TextUtils.isEmpty(pass))
                        newPass.setError("Please enter your new password");
                    if (TextUtils.isEmpty(pass))
                        ConfirmPass.setError("Please enter your confirm-password");
                }
                else if (pass.equals(confirmPass)) {
                    String url = "http://192.168.0.109/data/update.php?id=" + id + "&pass=" + pass;
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                Toast.makeText(getApplicationContext(), "Your password has been changed successfully", Toast.LENGTH_SHORT).show();
                                if(cbSave.isChecked()){
                                    editor.putString("id",id);
                                    editor.putString("pass",pass);
                                }
                                else{editor.putString("id","");
                                    editor.putString("pass","");
                                }
                                editor.commit();
                                if(value==1)
                                    startActivity(new Intent(ChangePassActivity.this,MainActivity.class));
                                else finish();
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
                else Toast.makeText(getApplicationContext(), "Error:Different Password.Please check it", Toast.LENGTH_SHORT).show();

            }
        });

    }
}