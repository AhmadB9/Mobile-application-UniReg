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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class OnlineServiceActivity extends AppCompatActivity {
EditText edtName,edtNbCivil,edtNbCopies;
Spinner spType;
Button btnSubmit;
Intent in;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_service);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        in=getIntent();
        String ID=in.getStringExtra("id");
        edtName=findViewById(R.id.edtOnlineName);
        edtNbCivil=findViewById(R.id.edtNcivil);
        spType=findViewById(R.id.spType);
        btnSubmit=findViewById(R.id.btnOnlineService);
        edtNbCopies=findViewById(R.id.edtNbCopies);
        String url = "http://192.168.0.109/data/userName.php?id=" + ID ;
        RequestQueue queue = Volley.newRequestQueue(OnlineServiceActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                edtName.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type,name,nbC,nbCopies;
                type=spType.getSelectedItem().toString();
                name=edtName.getText().toString();
                nbC=edtNbCivil.getText().toString();
                nbCopies=edtNbCopies.getText().toString();
                if(type.equalsIgnoreCase("choose") || TextUtils.isEmpty(name) || TextUtils.isEmpty(nbC) ||TextUtils.isEmpty(nbCopies))
                    Toast.makeText(OnlineServiceActivity.this,"Please Check your information.",Toast.LENGTH_LONG).show();
                else {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"onlineservice@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "" + spType.getSelectedItem().toString());
                    email.putExtra(Intent.EXTRA_TEXT, "ID:" + ID + "\n Name:" + name + "\n No.Civil Registry:" + nbC+"\n nb of copies:"+nbCopies);
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose:"));
                }
            }
        });

    }
}
