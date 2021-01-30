package com.example.notebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView nav_bar;
    ActionBarDrawerToggle toggle;
    Button myProfile,mynotes,aboutus,rateus,share,logout;
    SharedPreferences sharedPreferences;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarColor));

        final DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.activity_update, null),
//                new ActionBar.LayoutParams(
//                        ActionBar.LayoutParams.WRAP_CONTENT,
//                        ActionBar.LayoutParams.MATCH_PARENT,
//                        Gravity.CENTER
//                )
//        );
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        nav_bar = findViewById(R.id.nav_bar);
        nav_bar.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        drawerLayout.closeDrawer(GravityCompat.START);
        sharedPreferences = this.getSharedPreferences("com.example.notebase", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("DarkMode",false)){
            toggle.setHomeAsUpIndicator(R.drawable.hamburger);
        }
        else{
            toggle.setHomeAsUpIndicator(R.drawable.ic_hamburger_light);
        }

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        myProfile=findViewById(R.id.myprofile);
        mynotes=findViewById(R.id.mynotes);
        aboutus=findViewById(R.id.aboutus);
        rateus=findViewById(R.id.rateus);
        share=findViewById(R.id.share);
        logout=findViewById(R.id.logout);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this,Profile.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        mynotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this,MainActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this,About.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(About.this, "Coming Soon", Toast.LENGTH_SHORT).show();
//            }
//        });
//        rateus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(About.this, "Coming Soon", Toast.LENGTH_SHORT).show();
//            }
//        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(About.this);
                builder.setMessage("Are you sure you want to log out?");
                builder.setTitle("LOGOUT?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(About.this,Home.class));
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            default:
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}