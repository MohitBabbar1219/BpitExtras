package com.mydarkappfactory.bpitextracurriculars;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    ProgressDialog firstDialog;
    DatabaseReference firebaseDb;
    EventRecyclerViewAdapter adapter;
    ArrayList<String> titles, contents, dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        firebaseDb = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.toolbar_events);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Events");

        firstDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);

        firstDialog.setTitle("Loading");
        firstDialog.setMessage("Downloading content, please wait...");
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.setCancelable(false);
        firstDialog.show();

        titles = new ArrayList<>();
        contents = new ArrayList<>();
        dates = new ArrayList<>();

        firebaseDb.child("EVENTS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    titles.add(snapshot.child("head").getValue(String.class));
                    contents.add(snapshot.child("body").getValue(String.class));
                    dates.add(snapshot.child("date").getValue(String.class));
                }
                adapter.notifyDataSetChanged();
                firstDialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view_events);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new EventRecyclerViewAdapter(titles, contents, dates);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
