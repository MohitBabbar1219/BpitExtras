package com.mydarkappfactory.bpitextracurriculars;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SportsMeetActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_meet);

        int index = getIntent().getIntExtra("ImageIndex", -1);

        toolbar = findViewById(R.id.toolbar_sports_meet);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sports Meet 2018");

        viewPager = findViewById(R.id.viewpager_sports_meet);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SportsFragment(), "Registration");
        adapter.addFragment(new ScheduleFragment(), "Schedule");
        adapter.addFragment(new ResultFragment(), "Results");

        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tablayout_id_sports_meet);
        tabLayout.setupWithViewPager(viewPager);

    }



    //for menu of toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
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
