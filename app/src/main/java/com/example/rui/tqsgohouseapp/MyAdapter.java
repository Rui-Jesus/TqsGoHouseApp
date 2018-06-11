package com.example.rui.tqsgohouseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rui on 05-06-2018.
 */

public class MyAdapter extends ArrayAdapter<Property> {

    private int layoutResource;

    public MyAdapter(Context context, int layoutResource, List<Property> propertiesList) {
        super(context, layoutResource, propertiesList);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        Property property = getItem(position);

        if (property != null) {
            TextView titleView = (TextView) view.findViewById(R.id.titleView);
            TextView addressView = (TextView) view.findViewById(R.id.addressView);

            if (titleView != null) {
                titleView.setText(property.getName());
            }
            if (addressView != null) {
                addressView.setText(property.getAddress());
            }

        }

        return view;
    }

}
