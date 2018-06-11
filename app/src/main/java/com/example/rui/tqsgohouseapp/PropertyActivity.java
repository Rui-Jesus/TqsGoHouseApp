package com.example.rui.tqsgohouseapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PropertyActivity extends MainActivity {

    private Property property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_property, null, false);
        mDrawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        property = (Property) getIntent().getSerializableExtra("PROPERTY");
        TextView propertyName = (TextView) findViewById(R.id.propertyName);
        TextView propertyAddress = (TextView) findViewById(R.id.propertyAddress);
        propertyName.setText(property.getName());
        propertyAddress.setText(property.getAddress());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                float rate = ratingBar.getRating();
                if(rate <= 0){
                    Snackbar.make(view, "Attention: Select a rate first", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else{
                    Snackbar.make(view, "Submitted Rating", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    submit(rate);
                }

            }
        });

    }


    private void submit(float rate){
        //Create the post method here and send it
        //new ApiTask().execute(rate+"", property.getId() + "");
        DataHolder data = DataHolder.getInstance();
        int convertedRate = Math.round(rate);
        Log.println(Log.INFO, "PROPERTY", "Property id: " + property.getId() +  " Property rate: " + convertedRate + " User id: " + data.getId());
        postRate(property.getId(), convertedRate, data.getId());
        data.removeProperty(property.getId());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void postRate(final int propertyId, final int rate, final int userId){
        final com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody;
        String url = "http://192.168.160.224:8080/tqs-gohouse-1.0-SNAPSHOT/REST/properties/rate";
        try {
            jsonBody = new JSONObject();
            jsonBody.put("id", propertyId);
            jsonBody.put("rate", rate);
            jsonBody.put("delegate", userId);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(1, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.println(Log.INFO, "PROPERTY", "Property Rated " + response);
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.println(Log.INFO, "PROPERTY", "Error rating property " + error.getMessage());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return String.format("application/json; charset=utf-8");
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                requestBody, "utf-8");
                        return null;
                    }
                }
            };
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void doResquest(final int id, final float rate, final int delegateId){
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                "http://192.168.160.224:8080/tqs-gohouse-1.0-SNAPSHOT/REST/properties/rate",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(PropertyActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                Intent intent = new Intent(PropertyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rate", "" + rate);
                params.put("id", "" + id );
                params.put("delegate", "" + delegateId);
                return params;
            }

        };
        queue.add(jsonObjRequest);
    }

}
