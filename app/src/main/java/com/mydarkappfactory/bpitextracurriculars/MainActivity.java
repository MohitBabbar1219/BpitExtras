package com.mydarkappfactory.bpitextracurriculars;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference firebaseDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteOpenHelper dbHelper = new DBHelper(MainActivity.this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("EMAIL_PASSWORD", new String[]{"EMAIL", "PASSWORD"},
                "_id = 1", null, null, null, null);
        cursor.moveToFirst();

        firebaseDb = FirebaseDatabase.getInstance().getReference();

        final String email = cursor.getString(0);
        String password = cursor.getString(1);


        cursor.close();
        mAuth = FirebaseAuth.getInstance();

        Log.d("Bpit", "email & password: " + email + ", " + password);

        synchronized (mAuth) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Bpit", "Sign in status: " + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Log.d("Bpit", task.getException().toString());
                                Log.d("Bpit", "MainActivity login failed");

                                ContentValues recValues = new ContentValues();
                                recValues.put("ANSWER", -1);
                                db.update("IS_LOGGED_IN", recValues, "_id = 1", null);
                                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Welcome " + email.substring(0, email.indexOf('@')), Toast.LENGTH_SHORT).show();
                                firebaseDb.child(FirebaseModel.Users.USERS).child(email.substring(0, email.indexOf('.'))).child(FirebaseModel.Users.IS_FIRST_LOGIN).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue(Boolean.class)) {
                                            Log.d("Bpit", "MainActivity logged in first time");
                                            ContentValues recValues = new ContentValues();
                                            recValues.put("ANSWER", 0);
                                            db.update("IS_LOGGED_IN", recValues, "_id = 1", null);
                                        } else {
                                            Log.d("Bpit", "MainActivity logged in multiple times");
                                            ContentValues recValues = new ContentValues();
                                            recValues.put("ANSWER", 1);
                                            db.update("IS_LOGGED_IN", recValues, "_id = 1", null);

                                        }
                                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    });
        }


    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}