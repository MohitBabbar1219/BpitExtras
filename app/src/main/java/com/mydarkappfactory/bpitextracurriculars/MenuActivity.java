package com.mydarkappfactory.bpitextracurriculars;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import me.relex.circleindicator.CircleIndicator;

public class MenuActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    ViewPager viewPager;
    SQLiteDatabase db;
    SlideshowAdapter adapter;
    CircleIndicator indicator;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BPIT Extracurriculars");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        SQLiteOpenHelper dbHelper = new DBHelper(MenuActivity.this);
        db = dbHelper.getWritableDatabase();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                SQLiteOpenHelper dbHelper = new DBHelper(MenuActivity.this);
                db = dbHelper.getWritableDatabase();

                Cursor cursor = db.query("IS_LOGGED_IN", new String[]{"ANSWER"},
                        "_id = 1", null, null, null, null);
                cursor.moveToFirst();

                int answer = cursor.getInt(0);

                Log.d("Bpit", "" + answer);

                cursor.close();
                Intent intent;
                switch (id) {
                    case R.id.notice_id:
                        //TODO
                        intent = new Intent(MenuActivity.this, NoticeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.event_id:
                        //TODO
                        intent = new Intent(MenuActivity.this, EventsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.sportsroom_id:
                        //TODO

                        if (answer == -1) {
                            intent = new Intent(MenuActivity.this, LoginActivity.class);
                        } else if (answer == 0) {
                            intent = new Intent(MenuActivity.this, FirstLoginActivity.class);
                        } else {
                            intent = new Intent(MenuActivity.this, EquipmentsActivity.class);
                            intent.putExtra("CategoryIndex", 2);
                        }
//                        Intent intent = new Intent(MenuActivity.this, EquipmentsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.accountSettings:
                        //TODO

                        if (answer == -1) {
                            intent = new Intent(MenuActivity.this, LoginActivity.class);
                        } else if (answer == 0) {
                            intent = new Intent(MenuActivity.this, FirstLoginActivity.class);
                        } else {
                            intent = new Intent(MenuActivity.this, AccountSettingsActivity.class);
                            intent.putExtra("CategoryIndex", 2);
                        }
//                        Intent intent = new Intent(MenuActivity.this, EquipmentsActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        adapter = new SlideshowAdapter(MenuActivity.this);
        viewPager.setAdapter(adapter);

        indicator = findViewById(R.id.circle_indicator);
        indicator.setViewPager(viewPager);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MenuActivity.this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }


    //for menu of toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //for menu of toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.toolbar_item_1:
                //TODO
                break;
            case R.id.toolbar_item_2:
                //TODO
                break;
            case R.id.castle_button:
                //TODO
                Toast.makeText(this, "Castle", Toast.LENGTH_SHORT).show();
                break;
            //This is executed when the top left corner back is pressed
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
