package com.mydarkappfactory.bpitextracurriculars;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DatabaseReference firebaseDb;
    android.support.v7.widget.Toolbar toolbar;
    FirebaseUser user;
    String password, email;
    ProgressDialog firstDialog;
    EditText oldPassEdtTxt, newPassEdtTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.toolbar_password_change);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");

        oldPassEdtTxt = findViewById(R.id.oldPassEdtTxt);
        newPassEdtTxt = findViewById(R.id.newPassEdtTxt);

        SQLiteOpenHelper dbHelper = new DBHelper(ChangePasswordActivity.this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("EMAIL_PASSWORD", new String[]{"EMAIL", "PASSWORD"},
                "_id = 1", null, null, null, null);
        cursor.moveToFirst();

        email = cursor.getString(0);
        password = cursor.getString(1);
        cursor.close();

        firstDialog = new ProgressDialog(ChangePasswordActivity.this, R.style.ProgressDialogTheme);

        firstDialog.setTitle("Loading");
        firstDialog.setMessage("Please wait...");
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.setCancelable(false);
//        firstDialog.show();

        firebaseDb = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    public void resetPassword(final String newPassword) {
//        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//
//                } else {
//                    Log.d("Bpit", "Error password not updated");
//                }
//            }
//        });

        Log.d("Bpit", "Password updated");
        ContentValues recordValues = new ContentValues();
        recordValues.put("EMAIL", email);
        recordValues.put("PASSWORD", newPassword);
        db.update("EMAIL_PASSWORD", recordValues, "_id = ?", new String[]{"1"});
        firebaseDb.child(FirebaseModel.Users.USERS)
                .child(email.substring(0, email.indexOf('.')))
                .child("password").setValue(newPassword);
        Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
        firstDialog.cancel();
        finish();

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

    public void changePassword(View view) {

        String oldPassword = oldPassEdtTxt.getText().toString();
        String newPassword = newPassEdtTxt.getText().toString();
        if (!oldPassword.equals(password)) {
            Toast.makeText(this, "Incorrect current password", Toast.LENGTH_SHORT).show();
        } else if (newPassword.length() < 6) {
            Toast.makeText(this, "Password should be atleast 6 characters long", Toast.LENGTH_SHORT).show();
        } else {
            resetPassword(newPassword);
            firstDialog.show();
        }

    }


}
