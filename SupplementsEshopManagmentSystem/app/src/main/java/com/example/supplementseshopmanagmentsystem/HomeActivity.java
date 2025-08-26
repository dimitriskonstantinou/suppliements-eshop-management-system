package com.example.supplementseshopmanagmentsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private sharedPreferenceConfig sharedPreference;
    private DrawerLayout drawerLayout;
    private TextView navTextView;
    private NavigationView navigationView;
    private List<ProductRDB> productRDBList;
    private ProSupDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreference = new sharedPreferenceConfig(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout =  findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navTextView = navigationView.getHeaderView(0).findViewById(R.id.navMenuTextview);
        navTextView.setText("User: "+sharedPreference.readUsername());

        db = Room.databaseBuilder(this,ProSupDB.class,"pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        productRDBList = db.proSupDAO().getAllProducts();


        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new dashboard()).addToBackStack(null).commit();

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.dashboarditem) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new dashboard()).addToBackStack(null).commit();
        } else if (itemId == R.id.customersitem) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new customers()).addToBackStack(null).commit();
        } else if (itemId == R.id.productsitem) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new productsFrag(),"productsFrag").addToBackStack(null).commit();
        } else if (itemId == R.id.salesitem) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new sales()).addToBackStack(null).commit();
        }else if(itemId == R.id.suppliersitem) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new manageSuppliers(),"manageSuppliers").addToBackStack(null).commit();
        }else if (itemId == R.id.logout) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            sharedPreference.writeLoginStatus(false);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new aboutUs()).addToBackStack(null).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            if (fragmentManager.getBackStackEntryCount() > 0) {
                if(fragmentManager.getBackStackEntryCount()==1){
                    fragmentManager.popBackStack();
                    super.onBackPressed();
                }
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }

    }
}

