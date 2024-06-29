package com.example.liuapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
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

public class PaymentsActivity extends AppCompatActivity {
Spinner spSemester;
TextView txtCredit,txtFirst,txtSecond,txtThird,txtFourth;
Intent i;
int nbCredit;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_payments);
        spSemester=findViewById(R.id.spPayment);
        txtCredit=findViewById(R.id.txtNbCredits);
        txtFirst=findViewById(R.id.txtFirst);
        txtSecond=findViewById(R.id.txtSecond);
        txtThird=findViewById(R.id.txtThird);
        txtFourth=findViewById(R.id.txtFourth);
        i=getIntent();
        String ID=i.getStringExtra("id");
        spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCredit.setText("");
                txtFirst.setText("");
                txtThird.setText("");
                txtFourth.setText("");
                txtSecond.setText("");
                if (!spSemester.getSelectedItem().toString().equalsIgnoreCase("choose")) {
                    String Url = "http://192.168.0.109/data/credits.php?id=" + ID + "&semester=" + spSemester.getSelectedItem().toString();
                    RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray list = response.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    txtCredit.setText(object.getString("credit"));
                                    nbCredit = object.getInt("credit");
                                    float payment=nbCredit*215;
                                    txtFirst.setText(payment*0.4+"$");
                                    txtSecond.setText(payment*0.2+"$");
                                    txtThird.setText(payment*0.2+"$");
                                    txtFourth.setText(payment*0.2+"$");
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
                    Queue.add(objectRequest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}