package com.example.rui.tqsgohouseapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mDrawer;

    protected GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            String personId = acct.getId();
        }

        DataHolder data = DataHolder.getInstance();
        if(!(data.getPropertiesList().size() > 0)){
            new RetrieveProperties().execute();
        }

    }

    @Override
    protected void onStart(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_properties) {
            goToProperties();
        } else if (id == R.id.nav_map) {
            goToMap();
        } else if (id == R.id.nav_logout) {
            signOut();
        } else if (id == R.id.nav_share) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        goBack();
                    }
                });
    }

    public void goToMap(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToProperties(){
        Intent intent = new Intent(this, PropertiesMenu.class);
        startActivity(intent);
    }

    public void goBack(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    class RetrieveProperties extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            //responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            String API_URL = "http://192.168.160.224:8080/tqs-gohouse-1.0-SNAPSHOT/REST/properties";
            try {
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            //progressBar.setVisibility(View.GONE);
            Log.i("MAIN_ACTIVITY", "Json info: " + response);
            try {
                JSONArray jr = new JSONArray(response);
                ArrayList<Property> resList = new ArrayList<>();
                for(int i = 0; i<jr.length(); i++){
                    JSONObject jb = (JSONObject)jr.getJSONObject(i);
                    String owner = jb.getJSONObject("owner").getString("email");
                    int nRooms = jb.getJSONArray("rooms").length();
                    String address = jb.getString("address");
                    String name = jb.getString("type") + " " +  jb.getString("floor") +
                            " " + jb.getString("block");
                    double Lat = jb.getDouble("latitude");
                    double Lng = jb.getDouble("longitude");
                    Property p = new Property(name, owner, address, nRooms, Lat, Lng);
                    int id = jb.getInt("id");
                    p.setId(id);
                    resList.add(p);
                }

                if(resList.size() > 0){
                    DataHolder data = DataHolder.getInstance();
                    data.setPropertiesList(resList);
                }

                else{
                    backUp();
                }

            } catch (JSONException e) {
                // Appropriate error handling code
                Log.i("ERROR", "more errors");
            }
            //responseView.setText(response);
        }
    }

    private void backUp(){
        DataHolder data = DataHolder.getInstance();
        final ArrayList<Property> propertiesList = new ArrayList<>();
        LatLng PERTH = new LatLng(-31.952854, 115.857342);
        LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
        LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
        Property property = new Property("Casa com 3 quartos", "Rui"
                , "Rua 23 Lisboa", 3, -31.952854, 115.857342);
        Property property2 = new Property("Casa com 2 quartos", "Rui"
                , "Rua 23 Lisboa", 2, -33.87365, 151.20689);
        Property property3 = new Property("Casa com 4 quartos", "Rui"
                , "Rua 23 Lisboa", 4, -27.47093, 153.0235);
        propertiesList.add(property);
        propertiesList.add(property2);
        propertiesList.add(property3);
        data.setPropertiesList(propertiesList);
    }

}
