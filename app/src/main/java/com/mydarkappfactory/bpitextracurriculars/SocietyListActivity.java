package com.mydarkappfactory.bpitextracurriculars;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SocietyListActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;

    RecyclerViewAdapter adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_list);

        toolbar = findViewById(R.id.toolbar_socs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int categoryIndex = getIntent().getIntExtra("CategoryIndex", -1);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_society);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SocietyListActivity.this);
        final ArrayList<Society> x = new ArrayList<>();
        if (categoryIndex == 1) {
            x.add(new Society("One Technical Society", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.TECH , "Coord1", "Coord2"));
            x.add(new Society("Two Technical Society", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.TECH , "Coord1", "Coord2"));
            x.add(new Society("Three Technical Society", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.TECH , "Coord1", "Coord2"));
            getSupportActionBar().setTitle("Technical societies");
        } else {
            x.add(new Society("One Cultural Society", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.CULT , "Coord1", "Coord2"));
            x.add(new Society("Two Cultural Society", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.CULT , "Coord1", "Coord2"));
            getSupportActionBar().setTitle("Cultural societies");
        }
        adapter = new RecyclerViewAdapter(x);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        SQLiteOpenHelper dbHelper = new DBHelper(SocietyListActivity.this);
        db = dbHelper.getWritableDatabase();

        adapter.setListener(new RecyclerViewAdapter.Listener() {
            @Override
            public void onClick(int position) {
                SQLiteOpenHelper dbHelper = new DBHelper(SocietyListActivity.this);
                db = dbHelper.getWritableDatabase();

                Cursor cursor = db.query("IS_LOGGED_IN", new String[]{"ANSWER"},
                        "_id = 1", null, null, null, null);
                cursor.moveToFirst();

                int answer = cursor.getInt(0);

                Log.d("Bpit", "" + answer);

                cursor.close();
                Intent intent;
                if (answer == -1) {
                    intent = new Intent(SocietyListActivity.this, LoginActivity.class);
                } else if (answer == 0) {
                    intent = new Intent(SocietyListActivity.this, FirstLoginActivity.class);
                } else {
                    intent = new Intent(SocietyListActivity.this, SocietyFormActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra("SocietyDet", gson.toJson(x.get(position)));
                }
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
//            case R.id.toolbar_item_1:
//                //TODO
//                break;
//            case R.id.toolbar_item_2:
//                //TODO
//                break;
//            case R.id.castle_button:
//                //TODO
//                Toast.makeText(this, "Castle", Toast.LENGTH_SHORT).show();
//                break;
            //This is executed when the top left corner back is pressed
            case android.R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
