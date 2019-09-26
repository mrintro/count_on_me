//TO ACCEPT FRIEND REQUEST AND TO CANCEL FRIEND REQUEST PRESENT IN "MY FRIEND REQ"..

package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class friendRequest extends AppCompatActivity {
    EditText receiver,sender;
    Button accept,decline;
    DatabaseReference mData,mFriendData;
    String mCurrentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        sender=findViewById(R.id.excal_sender);
        receiver=findViewById(R.id.excal_receiver);
        accept=findViewById(R.id.excal_accept);
        decline=findViewById(R.id.excal_decline);
        mData= FirebaseDatabase.getInstance().getReference();
        mFriendData= FirebaseDatabase.getInstance().getReference();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(friendRequest.this,"CheckPoint1",Toast.LENGTH_LONG).show();
                final String senderName=sender.getText().toString().trim();
                final String receiverName=receiver.getText().toString().trim();
                mData.child("friend_req").child(receiverName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(friendRequest.this,"CheckPoint2",Toast.LENGTH_LONG).show();
                        if(dataSnapshot.hasChild(senderName)){
                            String req_type=dataSnapshot.child(senderName).child("request_type").getValue().toString().trim();

                            Toast.makeText(friendRequest.this,"CheckPoint3",Toast.LENGTH_LONG).show();
                            if(req_type.equals("received")){
                                String nameSender=mFriendData.child("users").child(senderName).child("name").toString().trim();
                                String mailSender=mFriendData.child("users").child(senderName).child("email").toString().trim();
                                String nameReceiver=mFriendData.child("users").child(senderName).child("name").toString().trim();
                                String mailReceiver=mFriendData.child("users").child(senderName).child("email").toString().trim();
                                mFriendData.child("friendList").child(senderName).child(receiverName).child("name").setValue(nameSender);
                                mFriendData.child("friendList").child(senderName).child(receiverName).child("email").setValue(mailSender);
                                mFriendData.child("friendList").child(receiverName).child(senderName).child("name").setValue(nameReceiver);
                                mFriendData.child("friendList").child(receiverName).child(senderName).child("email").setValue(mailReceiver);
                                decline.setEnabled(false);
                                mData.child("friend_req").child(senderName).child(receiverName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mData.child("friend_req").child(receiverName).child(senderName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(friendRequest.this,"Request removed successfully",Toast.LENGTH_LONG);
                                                //sendReq.setEnabled(true);
                                                mData.child("friend_req").child(senderName).child(receiverName).removeValue();
                                                mData.child("friend_req").child(receiverName).child(senderName).removeValue();
                                                //mCurrentStatus="not_friends";
                                                //sendReq.setText("Send request");
                                            }
                                        });
                                    }
                                });
                                Toast.makeText(friendRequest.this,"CheckPoint4",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(friendRequest.this,"Did not find any req",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(friendRequest.this,"Some Error occured",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(friendRequest.this,"CheckPoint1",Toast.LENGTH_LONG).show();
                final String senderName=sender.getText().toString().trim();
                final String receiverName=receiver.getText().toString().trim();
                mData.child("friend_req").child(receiverName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(friendRequest.this,"CheckPoint2",Toast.LENGTH_LONG).show();
                        if(dataSnapshot.hasChild(senderName)){
                            String req_type=dataSnapshot.child(senderName).child("request_type").getValue().toString().trim();

                            Toast.makeText(friendRequest.this,"CheckPoint3",Toast.LENGTH_LONG).show();
                            if(req_type.equals("received")){
                                mData.child("friend_req").child(senderName).child(receiverName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mData.child("friend_req").child(receiverName).child(senderName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(friendRequest.this,"Request removed successfully",Toast.LENGTH_LONG);
                                                //sendReq.setEnabled(true);
                                                mData.child("friend_req").child(senderName).child(receiverName).removeValue();
                                                mData.child("friend_req").child(receiverName).child(senderName).removeValue();
                                                //mCurrentStatus="not_friends";
                                                //sendReq.setText("Send request");
                                            }
                                        });
                                    }
                                });
                                accept.setEnabled(false);
                                Toast.makeText(friendRequest.this,"CheckPoint4",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(friendRequest.this,"Did not find any req",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(friendRequest.this,"Some Error occured",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
