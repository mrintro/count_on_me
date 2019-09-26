package com.example.excaliberani;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class xyz extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    List<String> data=null;


    public xyz(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

        this.layoutResourceId = resource;
        this.context= context;
        this.data=objects;
    }

    static class DataHolder
    {
        TextView naam;

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataHolder holder =null;

        if(convertView==null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            convertView = inflater.inflate(layoutResourceId,parent,false);

            holder = new DataHolder();
//            holder.ivFlag = (ImageViewew)convertView.findViewById(R.id.ivCountry);
            holder.naam = (TextView)convertView.findViewById(R.id.tvCountry);

            convertView.setTag(holder);
        }
        else
        {
            holder = (DataHolder)convertView.getTag();
        }

        String dataItem = data.get(position);
        holder.naam.setText(dataItem);
//        holder.ivFlag.setImageResource(dataItem.resIdThumbnail);

        return  convertView;
    }
}
