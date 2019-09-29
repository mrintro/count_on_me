package com.example.excaliberani;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public View getView(int position, final View convertView, ViewGroup parent) {

        View xy = null;
        if(inflater==null){
            inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        xy = inflater.inflate(R.layout.notification_adapter,parent,false);

        TextView t1 = (TextView) xy.findViewById(R.id.idnaam);
        TextView t2 = (TextView) xy.findViewById(R.id.idemail);
        Button accept=xy.findViewById(R.id.Accept_btn);
        Button decline =xy.findViewById(R.id.Reject_btn);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        t1.setText(details.get(position).getName());
        t2.setText(details.get(position).getEmail());
        final String curr_email = details.get(position).getEmail();
        final String curr_name = details.get(position).getName();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserMail=firebaseUser.getEmail();
        final String currentUserName=firebaseUser.getDisplayName();
        final String convertedCurr=convert_mail(currentUserMail),convertedSender=convert_mail(curr_email);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"hora hai",Toast.LENGTH_LONG).show();
                databaseReference.child("friend_req").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(convert_mail(curr_email)).exists()){
                            if(dataSnapshot.child(convert_mail(curr_email)).child(convert_mail(currentUserMail)).exists()){
                                String req_type=dataSnapshot.child(convertedCurr).child(convertedSender).child("req_type").getValue().toString().trim();
                                if(req_type.equals("received")){
                                    databaseReference.child("friend_req").child(convertedCurr).child(convertedSender).removeValue();
                                    databaseReference.child("friend_req").child(convertedSender).child(convertedCurr).removeValue();
                                    databaseReference.child("friend_list").child(convertedCurr).child(convertedSender).child("email").setValue(curr_email);
                                    databaseReference.child("friend_list").child(convertedSender).child(convertedCurr).child("email").setValue(currentUserMail);
                                    databaseReference.child("friend_list").child(convertedSender).child(convertedCurr).child("name").setValue(currentUserName);
                                    databaseReference.child("friend_list").child(convertedCurr).child(convertedSender).child("name").setValue(curr_name);
                                    Intent intent=new Intent(context,notificationpage.class);
                                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("friend_req").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(convert_mail(curr_email)).exists()){
                            if(dataSnapshot.child(convert_mail(curr_email)).child(convert_mail(currentUserMail)).exists()){
                                String req_type=dataSnapshot.child(convertedCurr).child(convertedSender).child("req_type").getValue().toString().trim();
                                if(req_type.equals("received")){
                                    databaseReference.child("friend_req").child(convertedCurr).child(convertedSender).removeValue();
                                    databaseReference.child("friend_req").child(convertedSender).child(convertedCurr).removeValue();
//                                    databaseReference.child("friend_list").child(convertedCurr).child(convertedSender).child("email").setValue(curr_email);
//                                    databaseReference.child("friend_list").child(convertedSender).child(convertedCurr).child("email").setValue(currentUserMail);
//                                    databaseReference.child("friend_list").child(convertedSender).child(convertedCurr).child("name").setValue(curr_name);
//                                    databaseReference.child("friend_list").child(convertedCurr).child(convertedSender).child("name").setValue(currentUserName);
                                }
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return xy;
    }


    public String convert_mail(String orig_mail){
        int n=orig_mail.length();
        for(int i=0;i<n;i++){
            String tmp=orig_mail.substring(i,i+1);
            if(orig_mail.charAt(i)=='.'){
                orig_mail=orig_mail.substring(0,i)+"_dot_"+orig_mail.substring(i+1,n);
                n+=4;
                i+=5;
            }
        }
        return orig_mail;
    }
}