package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ew_feed_page extends AppCompatActivity {

//    private Button log_out;

    private String user_mail;
    public ArrayList<String> req;
    DatabaseReference db;
    ListView lv;
    RememberUser userx;
    private ProgressDialog progressDialog;

    ArrayList<FeedData> feed = new ArrayList<FeedData>();
    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ew_feed_page);
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("LOADING");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getSupportActionBar().hide();


        userx=new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        user_mail = reg_user.get(RememberUser.EMAIL);
        lv = (ListView) findViewById(R.id.recycle);
//        lv.setLayoutManager(new LinearLayoutManager(this));

        db= FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetch(dataSnapshot);
                runadap();
                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        Toast.makeText(MainActivity.this,"here",Toast.LENGTH_SHORT).show();


    }


    private void runadap() {
        CustumAdapter cd = new CustumAdapter(this,feed,names);
        lv.setAdapter(cd);
    }

    private void fetch(DataSnapshot dataSnapshot) {

        FirebaseUser firebaseUser;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

       String email=convert_mail(user_mail);
//        Toast.makeText(ew_feed_page.this,email,Toast.LENGTH_LONG).show();
        DataSnapshot ds1 = dataSnapshot.child("Requests"),dsFriends= dataSnapshot.child("friend_list").child(convert_mail(user_mail));
        for(DataSnapshot ds:ds1.getChildren()){
            for(DataSnapshot inds:ds.getChildren()) {
                FeedData feed2;
                feed2 = inds.getValue(FeedData.class);
                if (feed2.getEmail().equals(user_mail)) {
                    continue;
                }

                String feed3=feed2.getEmail();
                feed3=convert_mail(feed3);
                String a=dsFriends.getKey().toString().trim();
                if(dsFriends.child(convert_mail(feed3)).child("email").exists()) {
                    if ((dsFriends.child(convert_mail(feed3)).child("email").getValue()).equals(extract_mail(feed3))) {
                        feed.add(feed2);
                    }
                    Toast.makeText(ew_feed_page.this,feed.get(0).getEmail(),Toast.LENGTH_LONG).show();

                }
            }
        }
        int p = feed.size();
        String y = String.valueOf(p);
        for(int i=0;i<feed.size();i++){
            String str = feed.get(i).getEmail();
            str = convert_mail(str);
            String st =dataSnapshot.child("users").child(str).child("name").getValue(String.class);
            names.add(st);
        }
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
