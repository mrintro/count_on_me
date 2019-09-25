//NAVIGATION VIEW CORRESPONDING NEWS FEED

package com.example.excaliberani;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

public class mainpage extends AppCompatActivity implements OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        drawer = findViewById(R.id.drawer_layout);
        abdt = new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(abdt);
        abdt.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);



//        Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,R.id.nav_host_fragment,R.id.nav_gallery,R.id.nav_profile, R.id.nav_feed, R.id.nav_slideshow, R.id.nav_tools, R.id.nav_share, R.id.nav_send).setDrawerLayout(drawer).build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                if(destination.getId() == R.id.nav_feed){
//                    Toast.makeText(mainpage.this,"news",Toast.LENGTH_SHORT).show();
//                }
//                else if(destination.getId() == R.id.nav_profile){
//                    Toast.makeText(mainpage.this,"profile",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        if(savedInstanceState==null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new News_feed_page()).commit();
//            navigationView.setCheckedItem(R.id.nav_feed);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(abdt.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Toast.makeText(mainpage.this,"here",Toast.LENGTH_SHORT).show();
        int x = menuItem.getItemId();
        switch (x) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile_Page()).commit();
                break;
            case R.id.nav_feed:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new News_feed_page()).commit();
                break;
            case R.id.nav_aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new about_us_page()).commit();
                break;
            case R.id.nav_logout:
                Toast.makeText(mainpage.this,"log out button called", Toast.LENGTH_SHORT);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

}
