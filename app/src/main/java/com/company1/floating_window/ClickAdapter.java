package com.company1.floating_window;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ClickAdapter extends ArrayAdapter<Click> {
    private int resourceId;

    public ClickAdapter(@NonNull Context context, int textViewResourceId, List<Click> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Click click = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView Clicktime = (TextView) view.findViewById(R.id.click_time);
        TextView Clickx = (TextView) view.findViewById(R.id.click_x);
        TextView Clicky = (TextView) view.findViewById(R.id.click_y);
        Clicktime.setText(Integer.toString(click.getTime()));
        Clickx.setText(Integer.toString(click.getX()));
        Clicky.setText(Integer.toString(click.getY()));
        return view;
    }
}