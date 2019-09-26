package com.example.excaliberani;

//registration

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;

public class Registration extends AppCompatActivity {


    private ProgressDialog progressDialog;
    Button proceedButton,uploadImage;
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
        setContentView(R.layout.activity_registration);
        Intent intent=getIntent();
        username = (EditText) findViewById(R.id.rname);
        emailid = (EditText) findViewById(R.id.remail);
        phone = (EditText) findViewById(R.id.rmob);
        passwd = (EditText) findViewById(R.id.rpass);
        proceedButton = (Button) findViewById(R.id.rbutton);
        progressDialog = new ProgressDialog(this);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        uploadImage=findViewById(R.id.rprofile);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"SELECT IMAGE"),GALLERY_PICK);
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(Registration.this);
            }
        });


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

                path=storeImg.child("images").child(email+".jpg");
//                path.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Task<Uri>urlTask=taskSnapshot.getStorage().getDownloadUrl();
//                        while(!urlTask.isSuccessful()){
//                            downUrl=urlTask.getResult().toString().trim();
//                        }
//                        userdetails.setImage(downUrl);
//                        aiseHi(key_email,userdetails);
//                    }
//                });
                path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                downUrl=uri.toString().trim();
//                            }
//                        });

                        if (task.isSuccessful()){
                            Toast.makeText(Registration.this, "Working", Toast.LENGTH_LONG).show();
//                            downUrl=task.getResult().getUploadSessionUri().toString();
//                            downUrl = Objects.requireNonNull(task.getResult()).getDownloadUrl().toString();
//                            downUrl=task.getResult().getUploadSessionUri().toString();
                            userdetails.setImage(downUrl);
                            Toast.makeText(Registration.this,downUrl,Toast.LENGTH_LONG).show();
                            aiseHi(key_email,userdetails);
                        }
                        else {
                            Toast.makeText(Registration.this,"Try again",Toast.LENGTH_LONG).show();
                        }

                    }
                });


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
                                            Intent intent1 = new Intent(Registration.this,Login_activity.class);
                                            startActivity(intent1);
//                                                    Toast.makeText(Registration.this,"succ",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
//                                                    Toast.makeText(Registration.this,"pfft",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Toast.makeText(Registration.this,"yaha?",Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);

        //FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        //String mail=currentUser.getEmail().toString().trim();
        Uri imgUri;
        if(requestCode== GALLERY_PICK ){
            Toast.makeText(Registration.this,"yaha bhi?",Toast.LENGTH_LONG).show();
            imgUri=data.getData();
            CropImage.activity(imgUri).setAspectRatio(1,1).start(Registration.this);
//            CropImage.activity(imgUri).setAspectRatio(1,1).start(this);
//            CropImage.activity(imgUri)
//                    .setAspectRatio(1,1)
//                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(Registration.this," aur yaha?",Toast.LENGTH_LONG).show();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                storeImg= FirebaseStorage.getInstance().getReference();
                Toast.makeText(Registration.this,"Idhr tk to agya",Toast.LENGTH_LONG).show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(Registration.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }

    }
}
