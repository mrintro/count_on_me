package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class searchbar extends AppCompatActivity {

    ArrayList<Emailndname>mylist = new ArrayList<>();
    private SearchView searchView;
    ListView lv;
    DatabaseReference db;
    xyz adapter;

    private RememberUser userx;
    private String user_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_searchbar);

        userx=new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        user_mail = reg_user.get(RememberUser.EMAIL);

        lv = (ListView)findViewById(R.id.lisss);

        if(mylist.size()==0) {
            db = FirebaseDatabase.getInstance().getReference();
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot ds = dataSnapshot.child("users");
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        if(ds1.child("email").getValue().toString().equals(user_mail)){
                            continue;
                        }
                        Emailndname nameaaja = ds1.getValue(Emailndname.class);
//                    Toast.makeText(searchbar.this,nameaaja,Toast.LENGTH_SHORT).show();
                        mylist.add(nameaaja);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void getdata(String query){
        ArrayList<Emailndname> output = new ArrayList<>();
        ArrayList<Emailndname> filtered = new ArrayList<>();

        for(Emailndname x:mylist){
            output.add(x);
        }

        if(searchView!=null){
            for(Emailndname x: output){
                if(x.getName().toLowerCase().startsWith(query.toLowerCase()))
                    filtered.add(x);
            }
        }
        else
            filtered=output;

        adapter = new xyz(searchbar.this,R.layout.adapview,filtered);
        lv.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search,menu);

        MenuItem searchitem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getdata(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}
