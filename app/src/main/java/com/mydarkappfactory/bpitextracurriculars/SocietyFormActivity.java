package com.mydarkappfactory.bpitextracurriculars;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SocietyFormActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText formAnswer;
    SQLiteDatabase db;
    DatabaseReference firebaseDb;
    Society society;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_form);

        formAnswer = findViewById(R.id.society_question);

        toolbar = findViewById(R.id.toolbar_soc_form);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        society = new Society(getIntent().getStringExtra("SocietyDet"));

        getSupportActionBar().setTitle(society.getName() + " Form");

        SQLiteOpenHelper dbHelper = new DBHelper(SocietyFormActivity.this);
        db = dbHelper.getWritableDatabase();

        firebaseDb = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void submitAnswer(View view) {
        String ans = formAnswer.getText().toString();
        Cursor cursor = db.query("EMAIL_PASSWORD", new String[]{"EMAIL"},
                "_id = 1", null, null, null, null);
        cursor.moveToFirst();

        String email = cursor.getString(0);
        cursor.close();

        firebaseDb.child(FirebaseModel.Societies.SOCIETIES)
                .child(society.getName())
                .child(FirebaseModel.Societies.SUBSCRIBED_USERS)
                .child(email.substring(0, email.indexOf('.'))).setValue(ans);

        Toast.makeText(this, "Application submitted", Toast.LENGTH_SHORT).show();
        finish();
    }


    //for menu of toolbar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        return true;
//    }
//
//    //for menu of toolbar
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
