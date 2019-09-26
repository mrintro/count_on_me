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
import java.util.List;

public class searchbar extends AppCompatActivity {

    ArrayList<String> mylist=new ArrayList<>();
    private SearchView searchView;
    ListView lv;
    DatabaseReference db;
    xyz adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_searchbar);

        lv = (ListView)findViewById(R.id.lisss);

        db= FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds=dataSnapshot.child("users");
                for(DataSnapshot ds1 : ds.getChildren()){
                    String nameaaja=ds1.child("name").getValue(String.class);
//                    Toast.makeText(searchbar.this,nameaaja,Toast.LENGTH_SHORT).show();
                    mylist.add(nameaaja);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getdata(String query){
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> filtered = new ArrayList<>();

        for(String x:mylist){
            output.add(x);
        }

        if(searchView!=null){
            for(String x: output){
                if(x.toLowerCase().startsWith(query.toLowerCase()))
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
