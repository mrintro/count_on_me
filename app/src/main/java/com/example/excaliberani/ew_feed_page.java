package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ew_feed_page extends AppCompatActivity {


    private TextView t;
    private Button log_out;

    public ArrayList<String> req;
    DatabaseReference db;
    ListView lv;
    RememberUser userx;

    ArrayList<FeedData> feed = new ArrayList<FeedData>();
    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ew_feed_page);

        t = (TextView)findViewById(R.id.xy);
        log_out =(Button)findViewById(R.id.log_out);
        userx=new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        String user_mail = reg_user.get(RememberUser.EMAIL);

        lv = (ListView) findViewById(R.id.excal_list_feed);
        t.setText(user_mail);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userx.logOutUser();
                Toast.makeText(ew_feed_page.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });
        db= FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetch(dataSnapshot);
                runadap();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        Toast.makeText(MainActivity.this,"here",Toast.LENGTH_SHORT).show();


    }


    private void runadap() {
        CustumAdapter cd = new CustumAdapter(ew_feed_page.this,feed,names);
        lv.setAdapter(cd);
    }

    private void fetch(DataSnapshot dataSnapshot) {
        /*FeedData feed2;
        feed2 = dataSnapshot.child("Requests").child("aniketpanwar_dot_1998@gmail_dot_com").getValue(FeedData.class);
        String str = feed2.getEmail();
        t1.setText(str);*/
        DataSnapshot ds1 = dataSnapshot.child("Requests");
        for(DataSnapshot ds:ds1.getChildren()){
            FeedData feed2;
            feed2 = ds.getValue(FeedData.class);
            feed.add(feed2);
        }
        int p = feed.size();
        String y = String.valueOf(p);
        Toast.makeText(ew_feed_page.this,y,Toast.LENGTH_SHORT).show();
        for(int i=0;i<feed.size();i++){
            String str = feed.get(i).getEmail();
            str = convert_mail(str);
            String st =dataSnapshot.child("users").child(str).child("name").getValue(String.class);
            names.add(st);
//            Toast.makeText(MainActivity.this, st,Toast.LENGTH_SHORT).show();
        }
//        for(FeedData obj:feed)
//            t1.setText(obj.getRequest());
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
