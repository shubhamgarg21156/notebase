package com.example.notebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView nav_bar;
    ActionBarDrawerToggle toggle;
    Button myProfile,mynotes,aboutus,logout;
    TextView profileName,profileEmail;
    ImageView profileImage;
    FirebaseFirestore firebaseFirestore;

    SharedPreferences sharedPreferences;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        logout=findViewById(R.id.logout);
        profileEmail=findViewById(R.id.profileEmail);
        profileName=findViewById(R.id.profileName);
        profileImage=findViewById(R.id.profileImage);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,Profile.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        mynotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,MainActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,About.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setMessage("Are you sure you want to log out?");
                builder.setTitle("LOGOUT?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Profile.this,Home.class));
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if(user.getPhotoUrl() != null) {
            new ImageLoadTask(user.getPhotoUrl().toString(), profileImage).execute();

            profileName.setText(user.getDisplayName());
            profileEmail.setText(user.getEmail());
        }
        else{
            firebaseFirestore=FirebaseFirestore.getInstance();

            firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(
                    new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            try {
                                profileName.setText(documentSnapshot.get("displayName").toString());
                                profileEmail.setText(documentSnapshot.get("email").toString());
                                new ImageLoadTask(documentSnapshot.get("photoURL").toString(),profileImage).execute();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
            );

        }

    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            if(result==null)return;
            super.onPostExecute(result);
            imageView.setImageBitmap(result);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

}