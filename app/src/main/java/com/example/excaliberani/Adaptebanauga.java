package com.example.excaliberani;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import javax.xml.transform.Templates;

public class Adaptebanauga extends BaseAdapter {
    public Context context;
    public LayoutInflater inflater;
    ArrayList<Emailndname> details;

    public Adaptebanauga(Context context, ArrayList<Emailndname> details) {
        this.context = context;
        this.details = details;

    }

    @Override
    public int getCount() {
        return details.size();
    }

    @Override
    public Object getItem(int position) {
        return details.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View xy = null;
        if(inflater==null){
            inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        xy = inflater.inflate(R.layout.notification_adapter,parent,false);

        TextView t1 = (TextView) xy.findViewById(R.id.idnaam);
        TextView t2 = (TextView) xy.findViewById(R.id.idemail);
        t1.setText(details.get(position).getName());
        t2.setText(details.get(position).getEmail());

        return xy;
    }
}
