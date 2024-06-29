package com.example.liuapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomAdapter extends BaseAdapter {
    JSONArray list;
    String id,semester;
    Context context;
    LayoutInflater inflater;
    int credit;
    public CustomAdapter(JSONArray list, String id, Context context,String semester) {
        this.list = list;
        this.id = id;
        this.context = context;
        this.semester=semester;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public static class holder{
       ImageView imgDrop,imgGrade;
       TextView txtCourse,txtTime,txtInstructor;
    }

    public CustomAdapter() {
        super();
    }

    @Override
    public int getCount() {
        return list.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder holder=new holder();
        final View rowView;
        rowView=inflater.inflate(R.layout.rowview,null);
        holder.imgDrop=rowView.findViewById(R.id.imgDrop);
        holder.imgGrade=rowView.findViewById(R.id.imgNote);
        holder.txtCourse=rowView.findViewById(R.id.txtClasseCourse);
        holder.txtInstructor=rowView.findViewById(R.id.txtClassInstructor);
        holder.txtTime=rowView.findViewById(R.id.txtClassTime);
        try {
            JSONObject object=list.getJSONObject(position);
            holder.txtCourse.setText(object.getString("course"));
            holder.txtInstructor.setText(object.getString("instructor"));
            credit=object.getInt("credit");
            holder.txtTime.setText(object.getString("time"));
            holder.imgGrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        String url = "http://192.168.0.109/data/getGrade.php?id=" + id + "&course=" + holder.txtCourse.getText().toString();
                        RequestQueue queue = Volley.newRequestQueue(context);
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AlertDialog.Builder adb=new AlertDialog.Builder(context);
                                adb.setTitle("Grade");
                                adb.setMessage("Your Grade: "+response);
                                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                adb.create().show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                    });queue.add(request);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.imgDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb=new AlertDialog.Builder(context);
                adb.setTitle("Delete");
                adb.setMessage("Are you sure that you want to drop this course: "+holder.txtCourse.getText().toString());
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://192.168.0.109/data/dropcourse.php?id=" + id + "&course=" + holder.txtCourse.getText().toString()+"&time="+holder.txtTime.getText().toString()+"&instructor="+holder.txtInstructor.getText().toString()+"&semester="+semester;
                        RequestQueue queue = Volley.newRequestQueue(context);
                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("success")){
                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                    ((ClassesActivity)context).onResume();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });queue.add(request);
                    }
                });
                adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                adb.create().show();
            }
        });
        return rowView;
    }
}
