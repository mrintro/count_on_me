package com.example.excaliberani;

//registration

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

public class Registration extends AppCompatActivity {


    private ProgressDialog progressDialog;
    Button proceedButton;
    EditText username,emailid,phone,passwd; //for fetching details from editText(registration form)
    String name,email,phonenumber,password,body,downUrl; //where actual details of user are stored
    private DatabaseReference databaseReference,imgDatabase;
    private FirebaseAuth mAuth;
    private static final int GALLERY_PICK=1;
    StorageReference storeImg,path;
    Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);
        Intent intent=getIntent();
        username = (EditText) findViewById(R.id.rname);
        emailid = (EditText) findViewById(R.id.remail);
        phone = (EditText) findViewById(R.id.rmob);
        passwd = (EditText) findViewById(R.id.rpass);
        proceedButton = (Button) findViewById(R.id.rbutton);
        progressDialog = new ProgressDialog(this);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        final Userdetails userdetails= new Userdetails();





        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateDataFromEditText();
                progressDialog.setMessage("Registering...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Email orig_mail= new Email(email);
                final String key_email=orig_mail.convert_mail();
                mAuth = FirebaseAuth.getInstance();

                userdetails.setemail(email);
                userdetails.setname(name);
                userdetails.setphonenumber(phonenumber);
                userdetails.setpassword(password);


            }
        });
    }

    public void aiseHi(final String key_email,final Userdetails userdetails){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("users").child(key_email).exists()){
                    Task<Void> Tb= databaseReference.child("users").child(key_email).setValue(userdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    if(task.isSuccessful()){

                                                        Toast.makeText(Registration.this,"Registered Successfully\n Please Verify Your Mail Id",Toast.LENGTH_LONG).show();

                                                        Intent intent1 = new Intent(Registration.this,Login_activity.class);
                                                        startActivity(intent1);
                                                    }
                                                    else{
                                                        Toast.makeText(Registration.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            progressDialog.dismiss();
                                                    Toast.makeText(Registration.this,"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "Error! Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validateDataFromEditText(){

        name=username.getText().toString().trim();
        email=emailid.getText().toString().trim();
        phonenumber=phone.getText().toString().trim();
        password=passwd.getText().toString().trim();
        if(name.isEmpty() || email.isEmpty() || phonenumber.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show();
            else if (phonenumber.length() != 10) {
                Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            }
            else if(password.length()<6){
                Toast.makeText(this, "Password should contain at least 6 characters", Toast.LENGTH_SHORT).show();
            }
            else {
                return;
            }
        }
        return;

    }
}
