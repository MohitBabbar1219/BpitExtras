package com.mydarkappfactory.bpitextracurriculars;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstLoginActivity extends AppCompatActivity {

    EditText emailEdt, mobileNumEdt;
    String mobileNum, email, password, emailAddress;
    DatabaseReference firebaseDb;
    SQLiteDatabase db;
    ProgressDialog firstDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        SQLiteOpenHelper dbHelper = new DBHelper(FirstLoginActivity.this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("EMAIL_PASSWORD", new String[]{"EMAIL", "PASSWORD"},
                "_id = 1", null, null, null, null);
        cursor.moveToFirst();

        firebaseDb = FirebaseDatabase.getInstance().getReference();

        email = cursor.getString(0);
        password = cursor.getString(1);

        cursor.close();

        emailEdt = findViewById(R.id.emailEdtTxt);
        mobileNumEdt = findViewById(R.id.mobile_edttxt);

        firebaseDb = FirebaseDatabase.getInstance().getReference();

        firstDialog = new ProgressDialog(FirstLoginActivity.this, R.style.ProgressDialogTheme);

        firstDialog.setTitle("Loading");
        firstDialog.setMessage("Downloading content, please wait...");
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.setCancelable(false);
        firstDialog.show();

        firebaseDb.child(FirebaseModel.Users.USERS)
                .child(email.substring(0, email.indexOf('.')))
                .child("emailAddress").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firebaseFetchedEmail = dataSnapshot.getValue(String.class);
                if (!firebaseFetchedEmail.equals("abc@xyz.com")) {
                    emailEdt.setText(dataSnapshot.getValue(String.class));
                }
                firstDialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void submitUserDetails(View view) {

        emailAddress = emailEdt.getText().toString();
        mobileNum = mobileNumEdt.getText().toString();

        boolean isMobileValid = isPhnNumberValid(mobileNum), isEmailValid = isEmailValid(emailAddress);

        if (isEmailValid && isMobileValid) {

            firebaseDb.child(FirebaseModel.Users.USERS).child(email.substring(0, email.indexOf('.'))).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                UserDetails details1 = new UserDetails(fname, lname, mobileNum, emailAddress, password, enrollment, stream, false, false);

                    UserDetails details = dataSnapshot.getValue(UserDetails.class);
                    details.setEmailAddress(emailAddress);
                    details.setPhoneNumber(mobileNum);
                    details.setFirstLogin(false);

                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .setValue(details);
                    ContentValues recValues = new ContentValues();
                    recValues.put("ANSWER", 1);
                    db.update("IS_LOGGED_IN", recValues, "_id = 1", null);

                    Intent intent = new Intent(FirstLoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            new CountDownTimer(3000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Log.d("Bpit", "FirstLogin: finished");

                    finish();
                }
            }.start();
        } else if (!isEmailValid) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        } else if (!isMobileValid) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter valid email and phone number", Toast.LENGTH_SHORT).show();
        }


    }

    public boolean isEmailValid(String emailAddress) {
        return emailAddress.contains("@") && !emailAddress.substring(0, emailAddress.indexOf('@')).isEmpty();
    }

    public boolean isPhnNumberValid(String phone) {
        boolean isNumbers = true;
        for (char x: phone.toCharArray()) {
            if (Character.isAlphabetic(x)) {
                isNumbers = false;
            }
        }
        return isNumbers && (phone.length() == 10);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
