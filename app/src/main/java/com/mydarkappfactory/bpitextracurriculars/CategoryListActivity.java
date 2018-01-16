package com.mydarkappfactory.bpitextracurriculars;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CategoryListActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        int index = getIntent().getIntExtra("ImageIndex", -1);

        toolbar = findViewById(R.id.toolbar_soc_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Societies");

        viewPager = findViewById(R.id.viewpager_soc_list);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TechnicalFragment(), "Technical");
        adapter.addFragment(new CulturalFragment(), "Cultural");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index - 1);

        tabLayout = findViewById(R.id.tablayout_id);
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
