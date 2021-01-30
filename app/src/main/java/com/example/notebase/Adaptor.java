package com.example.notebase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Adaptor extends RecyclerView.Adapter<Adaptor.ViewHolder> implements Filterable {

    List<String> title;
    List<String> content;
    List<String> titleColor;
    List<String> contentColor;
    List<String> Refrence;

    List<String> titleAll;
    List<String> contentAll;
    List<String> titleColorAll;
    List<String> contentColorAll;
    List<String> RefrenceAll;

    FirebaseFirestore firebaseFirestore;
    Activity a ;

    public Adaptor(List<String> title,
                   List<String> content,
                   List<String> titleColor,
                   List<String> contentColor,
                   List<String> Refrence) {

        this.title = title;
        this.content = content;
        this.titleColor = titleColor;
        this.contentColor = contentColor;
        this.Refrence=Refrence;

        this.titleAll = new ArrayList<>(title);
        this.contentAll = new ArrayList<>(content);
        this.titleColorAll = new ArrayList<>(titleColor);
        this.contentColorAll = new ArrayList<>(contentColor);
        this.RefrenceAll = new ArrayList<>(Refrence);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.noteTitle.setText(title.get(position));
        holder.noteTitle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(titleColor.get(position))));
        holder.noteContent.setText(content.get(position));
        holder.noteContent.setBackgroundColor(Color.parseColor(contentColor.get(position)));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),Update.class);
                intent.putExtra("position",position);
                intent.putExtra("title",title.get(position));
                intent.putExtra("content",content.get(position));
                intent.putExtra("ccolor",contentColor.get(position));
                intent.putExtra("tcolor",titleColor.get(position));
                intent.putExtra("ref",Refrence.get(position));
                ((Activity)v.getContext()).startActivityForResult(intent,100);
            }
        });
        firebaseFirestore=FirebaseFirestore.getInstance();

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setItems(R.array.options,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==1){
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(v.getContext());
                            builder2.setMessage("Delete the note?");
                            builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                collection("notes").document(Refrence.get(position)).delete();
                        Toast.makeText(v.getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();

                        a=(Activity)v.getContext();
                        final Intent intent = a.getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        a.finish();
                        a.overridePendingTransition(0, 0);
                        a.startActivity(intent);
                        a.overridePendingTransition(0, 0);

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog2=builder2.create();
                dialog2.show();
                        }

                        else{

                            AlertDialog.Builder builder3 = new AlertDialog.Builder(v.getContext());
                            builder3.setItems(R.array.colors,new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String,Object> map = new HashMap<>();
                                switch (which){
                                    case 0:

                                        map.clear();
                                        map.put("tcolor","#FF0000");
                                        map.put("ccolor","#FF9999");

                                        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                collection("notes").document(Refrence.get(position)).update(map);

                                        break;

                                    case 1:

                                        map.clear();
                                        map.put("tcolor","#FFE303");
                                        map.put("ccolor","#FFF49A");

                                        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                collection("notes").document(Refrence.get(position)).update(map);

                                        break;

                                    case 2:

                                        map.clear();
                                        map.put("tcolor","#80AE35");
                                        map.put("ccolor","#D4E8D9");

                                        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                collection("notes").document(Refrence.get(position)).update(map);
                                        break;

                                    case 3:

                                        map.clear();
                                        map.put("tcolor","#1E63B8");
                                        map.put("ccolor","#D8E7F8");

                                        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                collection("notes").document(Refrence.get(position)).update(map);

                                        break;

                                }

                                    a=(Activity)v.getContext();
                                    final Intent intent = new Intent(v.getContext(),MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    a.finish();
                                    a.overridePendingTransition(0, 0);
                                    a.startActivity(intent);
                                    a.overridePendingTransition(0, 0);

                                }
                            });

                            AlertDialog dialog3=builder3.create();
                            dialog3.show();

                        }
                    }
                });

                AlertDialog dialog1=builder1.create();
                dialog1.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Integer> filteredlist = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                for (int i = 0; i < contentAll.size(); i++) filteredlist.add(i);
            } else {

                for (int i = 0; i < contentAll.size(); i++) {

                    String content = contentAll.get(i);
                    if (content.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredlist.add(i);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            content.clear();
            title.clear();
            Refrence.clear();
            contentColor.clear();
            titleColor.clear();
            for (int i : (Collection<? extends Integer>)results.values){
                content.add(contentAll.get(i));
                title.add(titleAll.get(i));
                Refrence.add(RefrenceAll.get(i));
                contentColor.add(contentColorAll.get(i));
                titleColor.add(titleColorAll.get(i));
            }
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView noteTitle, noteContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.notetitle);
            noteContent = itemView.findViewById(R.id.notecontent);
            view = itemView;
        }
    }

}
