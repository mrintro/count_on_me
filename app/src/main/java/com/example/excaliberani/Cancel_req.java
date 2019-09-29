package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Cancel_req extends AppCompatActivity {

    private String user_mail;
    RememberUser userx;
    DatabaseReference db;
    ArrayList<FeedData> feed = new ArrayList<FeedData>();
    ArrayList<String> names = new ArrayList<String>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_req);

        lv=(ListView)findViewById(R.id.lst);
        userx=new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        user_mail = reg_user.get(RememberUser.EMAIL);

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

    }

    private void runadap() {
        Adapter_cancel cd = new Adapter_cancel(this,feed,names);
        lv.setAdapter(cd);
    }

    private void fetch(DataSnapshot dataSnapshot) {
        String email=convert_mail(user_mail);
//        Toast.makeText(ew_feed_page.this,email,Toast.LENGTH_LONG).show();
        String name = dataSnapshot.child("users").child(email).child("name").getValue(String.class);
        DataSnapshot ds1 = dataSnapshot.child("Requests").child(email);
        for(DataSnapshot ds:ds1.getChildren()) {
            FeedData feed2;
            feed2 = ds.getValue(FeedData.class);
            feed.add(feed2);
            names.add(name);
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




/*

userx=new RememberUser(context);
        HashMap<String, String> reg_user = userx.getUserDetails();
        user_mail = reg_user.get(RememberUser.EMAIL);

        Email email = new Email(user_mail);
final String str = email.convert_mail();

        db = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Registering...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        db.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Toast.makeText(Main_Menu.this,"here",Toast.LENGTH_SHORT).show();

        if(dataSnapshot.child("Requests").child(str).exists()){
        progressDialog.dismiss();
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setMessage("You Already Have A REQUEST. DO YOU WANT TO REMOVE IT")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
        db.child("Requests").child(str).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });*/
