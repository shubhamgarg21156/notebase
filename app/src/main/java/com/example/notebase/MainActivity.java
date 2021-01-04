package com.example.notebase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    @Override
    protected void onStart() {
        super.onStart();
        new NoteLoadTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(Color.BLACK);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav_bar = findViewById(R.id.nav_bar);
        nav_bar.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        toggle.setHomeAsUpIndicator(R.drawable.hamburger);
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
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
//
//                ApplicationInfo app = getApplicationContext().getApplicationInfo();
//                String filePath = app.sourceDir;
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("application/vnd.android.package-archive");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
//                intent.putExtra(Intent.EXTRA_TEXT, Uri.fromFile(new File(filePath)));
//                startActivity(Intent.createChooser(intent, "choose one"));
//
//            }
//        });
//        rateus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
//            }
//        });
        logout.setOnClickListener(new View.OnClickListener() {
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
}
