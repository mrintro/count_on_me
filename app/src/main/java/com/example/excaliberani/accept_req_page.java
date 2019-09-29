//TO ACCEPT REQUESTS PRESENT IN NEWS FEED..

package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class accept_req_page extends AppCompatActivity {


    DatabaseReference requestDatabase,fetch;
    TextView emails;
    String email,phoneNo;
    TextView submitTo,request,dropLocation,pickUpLocation;
    Button acceptOrder,showInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_req_page);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        submitTo=findViewById(R.id.excal_submit_to);
        pickUpLocation=findViewById(R.id.excal_pick_up);
        dropLocation=findViewById(R.id.excal_drop_location);
        request=findViewById(R.id.excal_request);
//        emails=findViewById(R.id.excal_order_of);
        acceptOrder=findViewById(R.id.excal_accept_order);

        emails.setText(email);

        requestDatabase= FirebaseDatabase.getInstance().getReference("Requests").child(convert_mail(email));
        fetch= FirebaseDatabase.getInstance().getReference();
//        showInfo=findViewById(R.id.excal_show_info);
        requestDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                submitTo.setText(dataSnapshot.child("email").getValue().toString().trim());
                pickUpLocation.setText(dataSnapshot.child("pickup").getValue().toString().trim());
                request.setText(dataSnapshot.child("req").getValue().toString().trim());
                dropLocation.setText(dataSnapshot.child("dropdown").getValue().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String semail=email;
                AlertDialog.Builder builder =new AlertDialog.Builder(accept_req_page.this);
                builder.setMessage("Do you want to confirm "+semail+"'s request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestDatabase.child(convert_mail(semail)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Add request to "pending requests" table..

                                        Toast.makeText(accept_req_page.this,"Done!",Toast.LENGTH_LONG).show();

                                    }
                                });
                                Intent i=new Intent(accept_req_page.this,accept_req_page.class);
                                //send sms
                                fetch.child("users").child(convert_mail(semail)).child("phonenumber").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        phoneNo=dataSnapshot.getValue().toString().trim();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                sendSms(phoneNo);
                                startActivity(i);
                            }
                        }).setNegativeButton("No",null);
                AlertDialog alert=builder.create();
                alert.show();
            }
        });
    }
    private String extract_mail(String orig_mail) {
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

    protected void sendSms(final String phoneNo){
        final int SEND_SMS_PERMISSION_REQUEST_CODE=1;
        Button send;

//        setContentView(R.layout.activity_send_sms);
//        send=findViewById(R.id.send_sms);

        if(checkPermission(Manifest.permission.SEND_SMS)){
//            send.setEnabled(true);
//            send.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onSend(phoneNo);
//                }
//            });
            onSend(phoneNo);
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }
    public void onSend(String phoneNo){
        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null,"Hey.I am near your request location.I'll be doing your task.Open the app for further details and contact me for the updated.",null,null);
            Toast.makeText(accept_req_page.this,"Message sent!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(accept_req_page.this,"Try again!",Toast.LENGTH_LONG).show();
        }
    }
    public boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED);
    }

}