package com.example.excaliberani;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_cancel extends BaseAdapter {
    Context context;
    private ArrayList<FeedData> queries;
    LayoutInflater inflater;
    private ArrayList<String> name;
    DatabaseReference db;
    String str;
    private String user_mail;
    RememberUser userx;
    ProgressDialog progressDialog;

    public Adapter_cancel(Context context, ArrayList<FeedData> queries, ArrayList<String> name) {
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

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.feed_list_view, parent,false);

        TextView t1 = (TextView)view.findViewById(R.id.excal_name);
        TextView t2 = (TextView)view.findViewById(R.id.excal_id);
        TextView t3 = (TextView)view.findViewById(R.id.excal_Data);
        TextView t4 = (TextView)view.findViewById(R.id.excal_pickup);
        TextView t5 = (TextView)view.findViewById(R.id.excal_dropdown);
        Button btn = (Button) view.findViewById(R.id.excalacc);
//        TextView t6 = (TextView)x.findViewById(R.id.excal_t1);
        t1.setText(name.get(position));
        t2.setText(queries.get(position).getEmail());
        t3.setText(queries.get(position).getReq());
        t4.setText(queries.get(position).getPickup());
        t5.setText(queries.get(position).getDropdown());
        btn.setText("CANCEL REQUEST");
        Bundle b = new Bundle();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userx=new RememberUser(context);
                HashMap<String, String> reg_user = userx.getUserDetails();
                user_mail = reg_user.get(RememberUser.EMAIL);

                Email email = new Email(user_mail);
                final String str = email.convert_mail();

                db = FirebaseDatabase.getInstance().getReference();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Removing Request...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Toast.makeText(Main_Menu.this,"here",Toast.LENGTH_SHORT).show();

                        if(dataSnapshot.child("Requests").child(str).child(str+queries.get(position).getReq()+queries.get(position).getDropdown()).exists()){
                            progressDialog.dismiss();
                            AlertDialog.Builder builder =new AlertDialog.Builder(context);
                            builder.setMessage("You Already Have A REQUEST. DO YOU WANT TO REMOVE IT")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.child("Requests").child(str).child(str+queries.get(position).getReq()+queries.get(position).getDropdown()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Add request to "pending requests" table..

                                                    Toast.makeText(context,"Done!",Toast.LENGTH_LONG).show();

                                                }
                                            });
                                        }
                                    }).setNegativeButton("No",null);
                            AlertDialog alert=builder.create();
                            alert.show();
                        }
                        else{
                            progressDialog.dismiss();
                            Intent intent = new Intent(context,Make_Requests_Activity.class);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(Main_Menu.this,"not good",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        return view;
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

    public String convert_mail(String orig_mail) {
        int n = orig_mail.length();
        for (int i = 0; i < n; i++) {

//            Toast.makeText(getApplicationContext(),orig_mail.substring(i,i+1),Toast.LENGTH_SHORT).show();
            String tmp = orig_mail.substring(i, i + 1);
            if (orig_mail.charAt(i) == '.') {
                orig_mail = orig_mail.substring(0, i) + "_dot_" + orig_mail.substring(i + 1, n);
                n += 4;
                i += 5;

//                Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_LONG).show();
            }

        }
        return orig_mail;
    }

    public String extract_mail(String orig_mail) {
        int n = orig_mail.length();
        for (int i = 0; i < n - 5; i++) {
            String tmp = orig_mail.substring(i, i + 5);
            if (tmp.equals("_dot_")) {
                String temp1 = orig_mail.substring(0, i);
                String temp2 = orig_mail.substring(i + 5, n);
                orig_mail = temp1 + "." + temp2;
                n -= 4;
            }
        }
        return orig_mail;
    }
}
