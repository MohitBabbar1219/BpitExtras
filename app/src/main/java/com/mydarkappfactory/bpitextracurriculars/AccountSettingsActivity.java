package com.mydarkappfactory.bpitextracurriculars;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSettingsActivity extends AppCompatActivity {

    DatabaseReference firebaseDb;
    SQLiteDatabase db;
    android.support.v7.widget.Toolbar toolbar;
    TextView emailTextView, phoneTextView, nameTextView, enrollmentTextView;
    ProgressDialog firstDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);


        toolbar = findViewById(R.id.toolbar_account_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account");

        emailTextView = findViewById(R.id.emailTextView);
        enrollmentTextView = findViewById(R.id.enrollmentNumberTextView);
        nameTextView = findViewById(R.id.nameTextView);
        phoneTextView = findViewById(R.id.phoneNumberTextView);

        firebaseDb = FirebaseDatabase.getInstance().getReference();

        SQLiteOpenHelper dbHelper = new DBHelper(AccountSettingsActivity.this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("EMAIL_PASSWORD", new String[]{"EMAIL"},
                "_id = 1", null, null, null, null);
        cursor.moveToFirst();

        String email = cursor.getString(0);
        cursor.close();

        firstDialog = new ProgressDialog(AccountSettingsActivity.this, R.style.ProgressDialogTheme);

        firstDialog.setTitle("Loading");
        firstDialog.setMessage("Downloading content, please wait...");
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.setCancelable(false);
        firstDialog.show();

        firebaseDb.child(FirebaseModel.Users.USERS)
                .child(email.substring(0, email.indexOf('.')))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("firstName").getValue(String.class) + " " + dataSnapshot.child("lastName").getValue(String.class);
                String enrollmentNumber = dataSnapshot.child("enrollmentNumber").getValue(String.class);
                String mobileNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                String email = dataSnapshot.child("emailAddress").getValue(String.class);
                emailTextView.setText(email);
                phoneTextView.setText(mobileNumber);
                enrollmentTextView.setText(enrollmentNumber);
                nameTextView.setText(name);
                firstDialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void attemptLogOut(View view) {
        ContentValues recordValues = new ContentValues();
        recordValues.put("EMAIL", "null");
        recordValues.put("PASSWORD", "null");
        db.update("EMAIL_PASSWORD", recordValues, "_id = ?", new String[]{"1"});
        Intent intent = new Intent(AccountSettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void changePasswordRequest(View view) {
        Intent intent = new Intent(AccountSettingsActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
