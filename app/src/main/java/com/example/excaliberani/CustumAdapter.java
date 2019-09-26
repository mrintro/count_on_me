//CUSTOM ADAPTER FOR NEWS FEED ACTIVITY
//git change
package com.example.excaliberani;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.Inflater;


public class CustumAdapter extends BaseAdapter {
    Context context;
    private ArrayList<FeedData> queries;
    LayoutInflater inflater;
    private ArrayList<String> name;
    DatabaseReference db;
    String str;

    public CustumAdapter(Context context, ArrayList<FeedData> queries,ArrayList<String> name) {
        this.context = context;
        this.queries = queries;
        this.name = name;
    }

    @Override
    public int getCount() {
        return queries.size();
    }

    @Override
    public Object getItem(int position) {
        return queries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View x = null;
        if(inflater==null){
            inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            x=inflater.inflate(R.layout.feed_list_view,parent,false);
        }

        TextView t1 = (TextView)x.findViewById(R.id.excal_name);
        TextView t2 = (TextView)x.findViewById(R.id.excal_id);
        TextView t3 = (TextView)x.findViewById(R.id.excal_Data);
        TextView t4 = (TextView)x.findViewById(R.id.excal_pickup);
        TextView t5 = (TextView)x.findViewById(R.id.excal_dropdown);
        Button btn = (Button)x.findViewById(R.id.excalacc);
//        TextView t6 = (TextView)x.findViewById(R.id.excal_t1);
        t1.setText(name.get(position));
        t2.setText(queries.get(position).getEmail());
        t3.setText(queries.get(position).getReq());
        t4.setText(queries.get(position).getPickup());
        t5.setText(queries.get(position).getDropdown());
        Bundle b = new Bundle();

        b.putString("email",queries.get(position).getEmail());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();

//                b.putString("email",queries.get(position).getEmail());
                Intent intent = new Intent(context,accept_req_page.class);
                intent.putExtra("email",queries.get(position).getEmail());
                context.startActivity(intent);

            }
        });

        return x;
    }

    private String getname(FeedData feedData) {
        db = FirebaseDatabase.getInstance().getReference();
        str = feedData.getEmail();
        final String str1 = convert_mail(str);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                str = dataSnapshot.child("users").child(str1).child("email").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return str;
    }

    public String convert_mail(String orig_mail){
        int n=orig_mail.length();
        for(int i=0;i<n;i++){

//            Toast.makeText(getApplicationContext(),orig_mail.substring(i,i+1),Toast.LENGTH_SHORT).show();
            String tmp=orig_mail.substring(i,i+1);
            if(orig_mail.charAt(i)=='.'){
                orig_mail=orig_mail.substring(0,i)+"_dot_"+orig_mail.substring(i+1,n);
                n+=4;
                i+=5;

//                Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_LONG).show();
            }

        }
        return orig_mail;
    }

    public String extract_mail(String orig_mail){
        int n=orig_mail.length();
        for(int i=0;i<n-5;i++){
            String tmp=orig_mail.substring(i,i+5);
            if(tmp.equals("_dot_")){
                String temp1=orig_mail.substring(0,i);
                String temp2=orig_mail.substring(i+5,n);
                orig_mail=temp1+"."+temp2;
                n-=4;
            }
        }
        return orig_mail;
    }
}
