package com.mydarkappfactory.bpitextracurriculars;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    FirebaseAuth mAuth;
    SQLiteDatabase db;
    boolean fromFragment;
    DatabaseReference firebaseDb;
    String societyObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        fromFragment = intent.getBooleanExtra("fromFrag", false);

        Log.d("Bpit", "fromFrag " + fromFragment);
        if (fromFragment) {
            societyObject = intent.getStringExtra("SocietyObject");
        }

        emailText = (EditText) findViewById(R.id.email_edttxt);
        passwordText = (EditText) findViewById(R.id.password_edttxt);

        firebaseDb = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        SQLiteOpenHelper dbHelper = new DBHelper(LoginActivity.this);
        db = dbHelper.getWritableDatabase();

    }

    public void attemptLogin(View view) {
        final String email = emailText.getText().toString() + "@bpit.com";
        final String password = passwordText.getText().toString();
//        final SharedPreferences sp = this.getSharedPreferences("com.mydarkappfactory.hudsoncafe", Context.MODE_PRIVATE);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter your enrollment number and password", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "Login in progress...", Toast.LENGTH_SHORT).show();
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Bpit", "Sign in status: " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.d("Bpit", task.getException().toString());
                        } else {

                            ContentValues recordValues = new ContentValues();
                            recordValues.put("EMAIL", email);
                            recordValues.put("PASSWORD", password);
                            db.update("EMAIL_PASSWORD", recordValues, "_id = ?", new String[]{"1"});

                            firebaseDb.child(FirebaseModel.Users.USERS).child(email.substring(0, email.indexOf('.'))).child(FirebaseModel.Users.IS_FIRST_LOGIN).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        boolean isFirstLogin = dataSnapshot.getValue(Boolean.class);
                                        if (dataSnapshot.getValue(Boolean.class) != null) {
                                            if (isFirstLogin) {
                                                Log.d("Bpit", "MainActivity logged in first time");
                                                ContentValues recValues = new ContentValues();
                                                recValues.put("ANSWER", 0);
                                                db.update("IS_LOGGED_IN", recValues, "_id = 1", null);
                                                Intent intent = new Intent(LoginActivity.this, FirstLoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.d("Bpit", "MainActivity logged in multiple times");
                                                ContentValues recValues = new ContentValues();
                                                recValues.put("ANSWER", 1);
                                                db.update("IS_LOGGED_IN", recValues, "_id = 1", null);
                                                finish();

                                            }
                                        }
                                    } catch (Exception e) {
                                        firebaseDb.child(FirebaseModel.Users.USERS).child(email.substring(0, email.indexOf('.'))).child(FirebaseModel.Users.IS_FIRST_LOGIN).setValue(true);
                                        Log.d("Bpit", "MainActivity logged in first time");
                                        ContentValues recValues = new ContentValues();
                                        recValues.put("ANSWER", 0);
                                        db.update("IS_LOGGED_IN", recValues, "_id = 1", null);

                                        Intent intent = new Intent(LoginActivity.this, FirstLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
