package com.mydarkappfactory.bpitextracurriculars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SportsFragment extends Fragment {

    View view;
    RecyclerViewAdapter adapter;
    SQLiteDatabase db;
    DatabaseReference firebaseDb;
    ProgressDialog firstDialog;

    public SportsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_frag_sports, container, false);

        System.out.println(getContext().toString());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_sports);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        firebaseDb = FirebaseDatabase.getInstance().getReference();
        SQLiteOpenHelper dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();

        firstDialog = new ProgressDialog(getContext(), R.style.ProgressDialogTheme);

        firstDialog.setTitle("Loading");
        firstDialog.setMessage("Downloading content, please wait...");
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.setCancelable(false);
        firstDialog.show();


        Cursor cursor = db.query("EMAIL_PASSWORD", new String[]{"EMAIL", "PASSWORD"},
                "_id = 1", null, null, null, null);
        cursor.moveToFirst();
        final String email = cursor.getString(0);
        cursor.close();




        final ArrayList<Society> x = new ArrayList<>();
        addInArrayList(x);
        final ArrayList<String> invalidEvents = new ArrayList<>();


        adapter = new RecyclerViewAdapter(x);

        firebaseDb.child(FirebaseModel.Users.USERS)
                .child(email.substring(0, email.indexOf('.')))
                .child(FirebaseModel.Users.EVENTS_REGISTERED)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        firstDialog.show();
                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> register = dataSnapshot.getValue(t);
                        for (String reg: register) {
                            invalidEvents.add(reg);
                        }
                        int i = 0;
                        while (i < x.size()) {
                            boolean isRemoved = false;
                            for (String p: invalidEvents) {
                                if (x.get(i).getName().equals(p)) {
                                    x.remove(i);
                                    isRemoved = true;
                                    break;
                                }
                            }
                            if (!isRemoved) {
                                i++;
                            }

                        }
                        adapter.notifyDataSetChanged();

                        Log.d("Bpit registered", register.toString());
                        firstDialog.cancel();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new RecyclerViewAdapter.Listener() {
            @Override
            public void onClick(int position) {
                SQLiteOpenHelper dbHelper = new DBHelper(getContext());
                db = dbHelper.getWritableDatabase();

                Cursor cursor = db.query("IS_LOGGED_IN", new String[]{"ANSWER"},
                        "_id = 1", null, null, null, null);
                cursor.moveToFirst();

                int answer = cursor.getInt(0);

                Log.d("Bpit", "" + answer);

                cursor.close();
                Intent intent;
                if (answer == -1 || answer == 0) {
                    intent = new Intent(getContext(), LoginActivity.class);
                }  else {
                    intent = new Intent(getContext(), SportsMeetRegisterationActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra("Event", gson.toJson(x.get(position)));
                    intent.putExtra("Email", email);
                    intent.putExtra("InvalidEvents", invalidEvents);
                    Log.d("Bpit", gson.toJson(x.get(position)));
                }
                startActivity(intent);
            }
        });

        return view;
    }

    public void addInArrayList(ArrayList<Society> x) {
        x.add(new Society("Football", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BG, "Coord1", "Coord2")); //B G
        x.add(new Society("Basketball", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BG, "Coord1", "Coord2")); //B G
        x.add(new Society("Volleyball", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BG, "Coord1", "Coord2")); //B G
        x.add(new Society("Chess", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BG, "Coord1", "Coord2")); //B G
        x.add(new Society("Shot put", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BG, "Coord1", "Coord2")); //B G
        x.add(new Society("Throw ball", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BG, "Coord1", "Coord2")); //B G
        x.add(new Society("Discuss throw", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BG, "Coord1", "Coord2")); //B G
        x.add(new Society("Long jump", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BG, "Coord1", "Coord2")); //B G
        x.add(new Society("Carrom", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BGSD, "Coord1", "Coord2")); //B G S D
        x.add(new Society("Badminton", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BGSD, "Coord1", "Coord2")); //B G S D
        x.add(new Society("Table tennis", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BGSD, "Coord1", "Coord2")); //B G S D
        x.add(new Society("Arm wrestling", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.BwtGwt, "Coord1", "Coord2")); //B 55-65 65-75 75+ G 60- 60+
        x.add(new Society("Cricket", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.SPORTS.B, "Coord1", "Coord2"));

    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
