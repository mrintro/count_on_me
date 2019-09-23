//TO SEND FRIEND REQUEST TO ANY USER PRESENT IN FIREBASE..SEARCH BAR NEEDS TO BE ADDED..

package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sendFriendRequest extends AppCompatActivity {

    EditText senderId,receiverId;
    Button sendReq,friendList,notifications;
    String mCurrentStatus;
    DatabaseReference mFriendReqDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_friend_request);

        mFriendReqDatabase= FirebaseDatabase.getInstance().getReference().child("friend_req");
        senderId=findViewById(R.id.excal_senderId);
        notifications=findViewById(R.id.excal_notifications);
        receiverId=findViewById(R.id.excal_receiverId);
        sendReq=findViewById(R.id.excal_sendReq);
        friendList=findViewById(R.id.excal_friend_list);
        mCurrentStatus="not_friends";
        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReq.setEnabled(false);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");
                final String edit_senderID,edit_receiverID;
                edit_senderID=senderId.getText().toString().trim();
                edit_receiverID=receiverId.getText().toString().trim();
                myRef.setValue("Hello, World!");
                if(mCurrentStatus.equals("not_friends")){
                    mFriendReqDatabase.child(edit_senderID).child(edit_receiverID).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFriendReqDatabase.child(edit_receiverID).child(edit_senderID).child("request_type")
                                    .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendReq.setEnabled(true);
                                    mCurrentStatus="req_sent";
                                    sendReq.setText("Cancel friend request");
                                    Toast.makeText(sendFriendRequest.this,"Ho Gaya",Toast.LENGTH_LONG);
                                }
                            });
                        }
                    });
                }
                else{
                    Toast.makeText(sendFriendRequest.this,"Nahi ho paya Bhaiya!",Toast.LENGTH_LONG);
                }
                if(mCurrentStatus.equals("req_sent")){
                    sendReq.setEnabled(false);
                    mFriendReqDatabase.child(edit_senderID).child(edit_receiverID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendReqDatabase.child(edit_receiverID).child(edit_senderID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendReq.setEnabled(true);
                                    mCurrentStatus="not_friends";
                                    sendReq.setText("Send request");
                                }
                            });
                        }
                    });
                }
            }
        });
        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friends=new Intent(sendFriendRequest.this,friendRequest.class);
                startActivity(friends);
            }
        });
    }
}
