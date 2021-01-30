package com.example.notebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.widget.Toast.*;


public class Update extends AppCompatActivity {
    EditText Title,Content;
    String reference;
    FloatingActionButton Update;
    int position = -1;
    public FirebaseFirestore firebaseFireStore;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarColor));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPreferences = this.getSharedPreferences("com.example.notebase", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("DarkMode",false)){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
        else{
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_light);
        }

        Title=findViewById(R.id.title);
        Content=findViewById(R.id.content);
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            String title=bundle.getString("title");
            String content=bundle.getString("content");
            Title.setText(title);
            Content.setText(content);
            Title.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(bundle.getString("tcolor"))));
            Content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(bundle.getString("ccolor"))));
            reference=bundle.getString("ref");
        }
        position=intent.getIntExtra("position",-1);

        Content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(position != -1){
                   MainActivity.content.set(position,String.valueOf(s));
                   MainActivity.adapter.notifyDataSetChanged();
                }
                Map<String,Object> map = new HashMap<>();
                map.put("content",Content.getText().toString());
                map.put("title",Title.getText().toString());
                firebaseFireStore=FirebaseFirestore.getInstance();

                try{
                    firebaseFireStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            collection("notes").document(reference).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //makeText(Update.this, "Error Occured", LENGTH_SHORT).show();
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(position != -1){
                    MainActivity.titles.set(position,String.valueOf(s));
                    MainActivity.adapter.notifyDataSetChanged();
                }
                Map<String,Object> map = new HashMap<>();
                map.put("content",Content.getText().toString());
                map.put("title",Title.getText().toString());
                firebaseFireStore=FirebaseFirestore.getInstance();

                try{
                    firebaseFireStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            collection("notes").document(reference).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //makeText(Update.this, "Error Occured", LENGTH_SHORT).show();
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}