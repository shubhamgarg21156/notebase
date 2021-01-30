package com.example.notebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.core.OrderBy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView nav_bar;
    ActionBarDrawerToggle toggle;
    private static RecyclerView noteList;
    static Adaptor adapter;
    Button myProfile,mynotes,aboutus,rateus,share,logout;
    public FirebaseFirestore firebaseFireStore;
    FloatingActionButton BtnFloat;
    SwipeRefreshLayout mySwipeRefreshLayout;
    final  static List<String> titles = new ArrayList<>();
    final static  List<String> content = new ArrayList<>();
    final  List<String> titlesColor = new ArrayList<>();
    final  List<String> contentColor = new ArrayList<>();
    final  List<String> reference=new ArrayList<>();

    SharedPreferences sharedPreferences;
    @Override
    protected void onStart() {
        super.onStart();
        new NoteLoadTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarColor));

        final DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav_bar = findViewById(R.id.nav_bar);
        nav_bar.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
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

        Drawable drawable;
        if(sharedPreferences.getBoolean("DarkMode",false)){
           drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_arrow_drop_down_circle_24);
        }
        else{
            drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_arrow_drop_down_circle_24_light);
        }

        toolbar.setOverflowIcon(drawable);


        //SharedPreferences
        final boolean isDarkModeOn = sharedPreferences.getBoolean("DarkMode",false);

        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        myProfile=findViewById(R.id.myprofile);
        mynotes=findViewById(R.id.mynotes);
        aboutus=findViewById(R.id.aboutus);
        rateus=findViewById(R.id.rateus);
        share=findViewById(R.id.share);
        logout=findViewById(R.id.logout);
        BtnFloat=findViewById(R.id.BtnFloat);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Profile.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        mynotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,About.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to log out?");
                builder.setTitle("LOGOUT?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this,Home.class));
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

             //dialog.getWindow().setBackgroundDrawableResource(getResources().getColor(R.color.NavBarColor));
             //dialog.getWindow().setColorMode(getResources().getColor(R.color.textColor));

            }
        });
        BtnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Note.class);
                startActivityForResult(intent,100);
            }
        });

        BtnFloat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "Add new Note", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        noteList = findViewById(R.id.noteList);

        new NoteLoadTask().execute();

        mySwipeRefreshLayout=findViewById(R.id.refresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        new NoteLoadTask().execute();

                    }
                }
        );

    }

    public  class NoteLoadTask extends AsyncTask<Void, Void, Void> {

        private FirebaseFirestore firebaseFireStore;

        @Override
        public Void doInBackground(Void... voids) {

            firebaseFireStore=FirebaseFirestore.getInstance();

            DocumentReference Docref = firebaseFireStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            Docref.collection("notes").orderBy("createdAt", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {


                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (queryDocumentSnapshots.isEmpty()) {

                        titles.clear();
                        content.clear();
                        titlesColor.clear();
                        contentColor.clear();
                    } else {

                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        titles.clear();
                        content.clear();
                        titlesColor.clear();
                        contentColor.clear();
                        reference.clear();
                        for (DocumentSnapshot d : list) {

                            DataFile data = d.toObject(DataFile.class);
                            String ref = d.getId();
                            reference.add(ref);
                            titles.add(data.title);
                            content.add(data.content);
                            titlesColor.add(data.tcolor);
                            contentColor.add(data.ccolor);
                        }
                    }
                        adapter = new Adaptor(titles, content, titlesColor, contentColor,reference);
                        noteList.setAdapter(adapter);
                        noteList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                        mySwipeRefreshLayout.setRefreshing(false);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new NoteLoadTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.modeChange){

            final boolean isDarkModeOn = sharedPreferences.getBoolean("DarkMode",false);

            if(isDarkModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPreferences.edit().putBoolean("DarkMode",false).apply();
                Toast.makeText(this, "Dark Mode Disabled", Toast.LENGTH_SHORT).show();
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPreferences.edit().putBoolean("DarkMode",true).apply();
                Toast.makeText(this, "DarkModeEnabled", Toast.LENGTH_SHORT).show();
            }

        }
        else if(item.getItemId() == R.id.action_search){
            SearchView searchView = (SearchView)item.getActionView();
            searchView.setBackgroundColor(getResources().getColor(R.color.backgroundColor));

            EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
            txtSearch.setTextColor(getResources().getColor(R.color.textColor));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
