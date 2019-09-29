package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    Button button1,button2;
    TextView username;
    DatabaseReference databaseReference;
    String email,sender,nameS,nameR;
    DatabaseReference mData,mFriendData,mFriendList,userDatabase;
    String mCurrentState,senderUsername,receiverUsername,send,receive;
    FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        sender=mCurrentUser.getEmail();
        Intent intent=getIntent();
        email=intent.getStringExtra("email");

        mCurrentState="not_friends";
        button1=findViewById(R.id.send);
        button2=findViewById(R.id.doosra);
        username=findViewById(R.id.naam);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mData= FirebaseDatabase.getInstance().getReference();
        mFriendData= FirebaseDatabase.getInstance().getReference();
        mFriendList= FirebaseDatabase.getInstance().getReference();
        userDatabase= FirebaseDatabase.getInstance().getReference();
        final String senderName=convert_mail(sender);
        final String receiverName=convert_mail(email);
//        Toast.makeText(UserProfile.this,senderName+" "+receiverName,Toast.LENGTH_LONG).show();
        button2.setEnabled(false);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("users").child(convert_mail(email)).child("name").getValue().toString().trim();
                username.setText(name);
                mFriendData.child("friend_list").child(senderName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(receiverName)){
                            mCurrentState="friends";
                            button1.setText("Unfriend");

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mFriendList.child("friend_req").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(senderName)) {
                            if (dataSnapshot.child(senderName).hasChild(receiverName)) {
                                String req_type = dataSnapshot.child(senderName).child(receiverName).child("req_type").getValue().toString().trim();
                                Toast.makeText(UserProfile.this, req_type, Toast.LENGTH_LONG).show();
                                if (req_type.equals("received")) {
                                    mCurrentState = "req_received";
                                    button2.setEnabled(true);
                                    button2.setText("Decline Request");
                                    button1.setText("Accept Request");
                                } else if (req_type.equals("sent")) {
                                    mCurrentState = "req_sent";
                                    button1.setText("Cancel Friend Request");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile.this,"An error occured",Toast.LENGTH_LONG).show();
            }
        });

        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameS=dataSnapshot.child("users").child(senderName).child("name").getValue().toString().trim();
                nameR=dataSnapshot.child("users").child(receiverName).child("name").getValue().toString().trim();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setEnabled(false);
                Toast.makeText(UserProfile.this,"CheckPoint1",Toast.LENGTH_LONG).show();

                if(mCurrentState.equals("req_sent")){
                    mFriendData.child("friend_req").child(senderName).child(receiverName).removeValue();
                    mFriendData.child("friend_req").child(receiverName).child(senderName).removeValue();
                    button1.setText("Send Request");
                    button1.setEnabled(true);
                    mCurrentState="not_friends";
                }
                else if(mCurrentState.equals("not_friends")){
                    userDatabase.child("users").child(senderName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            senderUsername=dataSnapshot.child("name").getValue(String.class);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    userDatabase.child("users").child(receiverName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            receiverUsername=dataSnapshot.child("name").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(UserProfile.this,nameR+" "+nameR,Toast.LENGTH_LONG).show();
                    mFriendData.child("friend_req").child(senderName).child(receiverName).child("req_type").setValue("sent");
                    mFriendData.child("friend_req").child(senderName).child(receiverName).child("email").setValue(email);
                    mFriendData.child("friend_req").child(senderName).child(receiverName).child("name").setValue(nameR);
                    mFriendData.child("friend_req").child(receiverName).child(senderName).child("name").setValue(nameS);
                    mFriendData.child("friend_req").child(receiverName).child(senderName).child("req_type").setValue("received");
                    mFriendData.child("friend_req").child(receiverName).child(senderName).child("email").setValue(sender);

                    mCurrentState="req_sent";
                    button1.setEnabled(true);
                    button1.setText("Cancel Friend Request");

                }
                else if(mCurrentState.equals("req_received")){

//                    Toast.makeText(UserProfile.this,nameR+" "+nameR,Toast.LENGTH_LONG).show();

                    mFriendData.child("friend_list").child(senderName).child(receiverName).child("name").setValue(nameR);
                    mFriendData.child("friend_list").child(senderName).child(receiverName).child("email").setValue(email);
                    mFriendData.child("friend_list").child(receiverName).child(senderName).child("name").setValue(nameS);
                    mFriendData.child("friend_list").child(receiverName).child(senderName).child("email").setValue(sender);
                    mFriendData.child("friend_req").child(senderName).child(receiverName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendData.child("friend_req").child(receiverName).child(senderName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mCurrentState="friends";
                                    button1.setEnabled(true);
                                    button1.setText("Unfriend");
                                    button2.setText("");
                                    button2.setEnabled(false);
                                }
                            });

                        }
                    });

                }
                else if(mCurrentState.equals("friends")){
                    mFriendData.child("friend_list").child(senderName).child(receiverName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendData.child("friend_list").child(receiverName).child(senderName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mCurrentState="not_friends";
                                    button1.setEnabled(true);
                                    button1.setText("Send Request");
                                }
                            });

                        }
                    });

                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(UserProfile.this,"CheckPoint1",Toast.LENGTH_LONG).show();
                Toast.makeText(UserProfile.this,senderName+" "+receiverName,Toast.LENGTH_LONG).show();
                mData.child("friend_req").child(receiverName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(UserProfile.this,"CheckPoint2",Toast.LENGTH_LONG).show();
                        if(dataSnapshot.hasChild(senderName)){
                            String req_type=dataSnapshot.child(senderName).child("req_type").getValue().toString().trim();
                            Toast.makeText(UserProfile.this,"CheckPoint3 "+req_type,Toast.LENGTH_LONG).show();
                            if(req_type.equals("sent")){
                                mData.child("friend_req").child(senderName).child(receiverName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mData.child("friend_req").child(receiverName).child(senderName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(UserProfile.this,"Request removed successfully",Toast.LENGTH_LONG);
                                                button1.setEnabled(true);
                                                button1.setText("Send Request");
                                                mCurrentState="not_friends";
                                                button2.setEnabled(false);
                                                button2.setText("");
                                                //sendReq.setEnabled(true);
//                                                mData.child("friend_req").child(senderName).child(receiverName).removeValue();
//                                                mData.child("friend_req").child(receiverName).child(senderName).removeValue();
                                                //mCurrentStatus="not_friends";
                                                //sendReq.setText("Send request");
                                            }
                                        });
                                    }
                                });
                                button1.setEnabled(false);
                                Toast.makeText(UserProfile.this,"CheckPoint4",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(UserProfile.this,"Did not find any req",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UserProfile.this,"Some Error occured",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

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
