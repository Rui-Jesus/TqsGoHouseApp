package com.example.rui.tqsgohouseapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PropertiesMenu extends MainActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_properties_menu, null, false);
        mDrawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void propertyClicked(Property property){
        Intent intent = new Intent(this, PropertyActivity.class);
        intent.putExtra("PROPERTY", property);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        listView = (ListView) findViewById(R.id.propertiesList);

        DataHolder data = DataHolder.getInstance();
        final ArrayList<Property> propertiesList = data.getPropertiesList();
        MyAdapter myAdapter = new MyAdapter(this, R.layout.custom_list_view, propertiesList);
        listView.setAdapter(myAdapter);

        // register onClickListener to handle click events on each item
        final Context context = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                Property selectedProperty = propertiesList.get(position);
                Toast.makeText(getApplicationContext(),
                        "Movie Selected : "+selectedProperty, Toast.LENGTH_LONG).show();
                PropertiesMenu.this.propertyClicked(selectedProperty);
            }
        });

    }

}
