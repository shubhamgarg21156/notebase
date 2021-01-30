package com.example.notebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Note extends AppCompatActivity {
    EditText Title,Content;
    FloatingActionButton Save;
    public FirebaseFirestore firebaseFireStore;
    String ccolor,tcolor;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarColor));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences("com.example.notebase", Context.MODE_PRIVATE);
        Drawable drawable;
        if(sharedPreferences.getBoolean("DarkMode",false)){
            drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_arrow_drop_down_circle_24);
        }
        else{
            drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_arrow_drop_down_circle_24_light);
        }

        toolbar.setOverflowIcon(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(sharedPreferences.getBoolean("DarkMode",false)){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
        else{
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_light);
        }

       Title=findViewById(R.id.title);
        Content=findViewById(R.id.content);
        Save=findViewById(R.id.save);
        tcolor="#1E63B8";
        ccolor="#D8E7F8";

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNote();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        if(item.getItemId()==R.id.color){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(R.array.colors,new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which){
                        case 0:
                            Title.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                            Content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9999")));
                            tcolor="#FF0000";
                            ccolor="#FF9999";
                            break;
                        case 1:
                            Title.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFE303")));
                            Content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF49A")));
                            tcolor="#FFE303";
                            ccolor="#FFF49A";
                            break;

                        case 2:
                            Title.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#80AE35")));
                            Content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D4E8D9")));
                            tcolor="#80AE35";
                            ccolor="#D4E8D9";
                            break;

                        case 3:
                            Title.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1E63B8")));
                            Content.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D8E7F8")));
                            tcolor="#1E63B8";
                            ccolor="#D8E7F8";
                    }
                }
            });

            AlertDialog dialog=builder.create();
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void AddNote(){

        long timeStamp = new Date().getTime();

        Map<String,Object> map = new HashMap<>();
        map.put("title",Title.getText().toString());
        map.put("content",Content.getText().toString());
        map.put("ccolor",ccolor);
        map.put("tcolor",tcolor);
        map.put("createdAt",timeStamp);

        firebaseFireStore=FirebaseFirestore.getInstance();

        firebaseFireStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                collection("notes").document(Long.toString(timeStamp)).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(Note.this, "Note Added", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Note.this, "Ah!! Error occurred Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;

    }

}
