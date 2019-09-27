package com.example.excaliberani;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class xyz extends ArrayAdapter<Emailndname> {

    Context context;
    int layoutResourceId;
    ArrayList<Emailndname> data=new ArrayList<>();


    public xyz(Context context, int resource, ArrayList<Emailndname> objects) {
        super(context, resource, objects);

        this.layoutResourceId = resource;
        this.context= context;
        this.data=objects;
    }

    static class DataHolder
    {
        TextView naam,maal;

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
            holder.maal = (TextView)convertView.findViewById(R.id.tvemail);
            convertView.setTag(holder);
        }
        else
        {
            holder = (DataHolder)convertView.getTag();
        }

        String datanaam = data.get(position).getName();
        final String datamaal = data.get(position).getEmail();
        holder.naam.setText(datanaam);
        holder.maal.setText(datamaal);
        holder.naam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserProfile.class);
                intent.putExtra("email",datamaal);
                context.startActivity(intent);
            }
        });
//        holder.ivFlag.setImageResource(dataItem.resIdThumbnail);

        return  convertView;
    }
}
