package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.Window;
import android.widget.SearchView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener  {

    private ListView list;
    private UserListViewAdapter adapter;
  //  private String user_list;
    private SearchView editsearch;
    public static ArrayList<String> namelist;

    public static ArrayList<String> email_list;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_search);
        list=(ListView)findViewById(R.id.listview);
        editsearch= (SearchView) findViewById(R.id.search);
        namelist=new ArrayList<String>();
        email_list=new ArrayList<String>();
        db= FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds=dataSnapshot.child("users");
                for(DataSnapshot ds1 : ds.getChildren()){
                    String name=ds1.child("name").getValue(String.class);
                //    String email=ds1.child("email").getValue(String.class);
                    namelist.add(name);
                //       email_list.add(email);
                }
                //getadap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter=new UserListViewAdapter(this,namelist);
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);
//        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (TextUtils.isEmpty(newText)) {
//                    list.clearTextFilter();
//                    adapter.filter("");
//                } else {
//                    adapter.filter(newText);
//                    //list.setFilterText(newText.toString());
//                }
//                return true;
//            }
//        });
        setupSearchView();
//        editsearch.setSubmitButtonEnabled(true);
//        editsearch.setOnQueryTextListener(listener);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchActivity.this,namelist.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchView() {
        editsearch.setOnQueryTextListener(this);
        editsearch.setSubmitButtonEnabled(true);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
                    //list.clearTextFilter();
                    String s="";
                    adapter.getFilter().filter(s);
                } else {
                    adapter.getFilter().filter(newText);
                   // list.setFilterText(newText);
                   // list.setFilterText(newText.toString());
                }
                return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(TextUtils.isEmpty(query)){
            String s="";
            adapter.getFilter().filter(s);
        }
        return false;
    }

    SearchView.OnCloseListener closeListener=new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            adapter.getFilter().filter("");
            return true;
        }
    };


    //    SearchView.OnQueryTextListener listener=new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String query) {
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String newText) {
//            return false;
//        }
//    };

//    private void setupSearchView() {
//        editsearch.setSubmitButtonEnabled(true);
//        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                String text=newText.toString();
//                adapter.filter(text);
//                return false;
//            }
//        });
//
//    }


//    private void getadap() {
//
//        editsearch= (SearchView) findViewById(R.id.search);
//        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                String text = newText.toString();
//                adapter.filter(text);
//                return false;
//            }
//        });
//    }


}
