package com.mydarkappfactory.bpitextracurriculars;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EquipmentsActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    RecyclerViewAdapter adapter;
    DatabaseReference firebaseDb;
    SQLiteDatabase db;
    RelativeLayout footballLayout, basketballLayout, volleyballLayout;
    Button footballButt, basketballButt, volleyballButt;
    HashMap<String, Button> buttons;
    HashMap<String, RelativeLayout> layouts;
    HashMap<String, TextView> textViews;
    String email;
    String[] itemNames = {"FOOTBALL", "BASKETBALL", "VOLLEYBALL"};
    boolean hasRequestedItem, isRequestAccepted;
    ProgressDialog firstDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipments);

        buttons = new HashMap<>();
        layouts = new HashMap<>();
        textViews = new HashMap<>();

        footballLayout = findViewById(R.id.football_layout);
        basketballLayout = findViewById(R.id.basketball_layout);
        volleyballLayout = findViewById(R.id.volleyball_layout);

        layouts.put(FirebaseModel.Equipments.FOOTBALL, footballLayout);
        layouts.put(FirebaseModel.Equipments.BASKETBALL, basketballLayout);
        layouts.put(FirebaseModel.Equipments.VOLLEYBALL, volleyballLayout);

        footballButt = findViewById(R.id.football_butt);
        basketballButt = findViewById(R.id.basketball_butt);
        volleyballButt = findViewById(R.id.volleyball_butt);

        buttons.put(FirebaseModel.Equipments.FOOTBALL, footballButt);
        buttons.put(FirebaseModel.Equipments.BASKETBALL, basketballButt);
        buttons.put(FirebaseModel.Equipments.VOLLEYBALL, volleyballButt);

        textViews.put(FirebaseModel.Equipments.FOOTBALL, (TextView) findViewById(R.id.football_text_accepted));
        textViews.put(FirebaseModel.Equipments.BASKETBALL, (TextView) findViewById(R.id.basketball_text_accepted));
        textViews.put(FirebaseModel.Equipments.VOLLEYBALL, (TextView) findViewById(R.id.volleyball_text_accepted));

        toolbar = findViewById(R.id.toolbar_equipment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SQLiteOpenHelper dbHelper = new DBHelper(EquipmentsActivity.this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("EMAIL_PASSWORD", new String[]{"EMAIL", "PASSWORD"},
                "_id = 1", null, null, null, null);
        cursor.moveToFirst();

        firebaseDb = FirebaseDatabase.getInstance().getReference();

        email = cursor.getString(0);

        firstDialog = new ProgressDialog(EquipmentsActivity.this, R.style.ProgressDialogTheme);

        firstDialog.setTitle("Loading");
        firstDialog.setMessage("Downloading content, please wait...");
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.setCancelable(false);
        firstDialog.show();


        firebaseDb.child(FirebaseModel.Users.USERS)
                .child(email.substring(0, email.indexOf('.')))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserDetails details = dataSnapshot.getValue(UserDetails.class);
                        firstDialog.cancel();
                        Log.d("Bpit", "details: " + details.getHasRequestedItem());
                        Log.d("Bpit", "details: " + details.getItemRequestAccepted());
                        isRequestAccepted = details.getItemRequestAccepted();
                        hasRequestedItem = details.getHasRequestedItem();
                        if (details.getHasRequestedItem()) {
                            if (details.getItemRequestAccepted()) {
                                try {
                                    layouts.get(details.getRequestedItem()).setBackgroundColor(Color.parseColor("#6b4c4d"));
                                    textViews.get(details.getRequestedItem()).setVisibility(View.VISIBLE);
                                    for (String itemName: itemNames) {
                                        buttons.get(itemName).setVisibility(View.INVISIBLE);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error 1");
                                }
                                alertDialog.cancel();
                            } else {

                                try {
                                    buttons.get(details.getRequestedItem()).setText("Cancel Request");
                                    layouts.get(details.getRequestedItem()).setBackgroundColor(Color.parseColor("#52744c"));
                                    for (String itemName: itemNames) {
                                        if (!itemName.equals(details.getRequestedItem())) {
                                            buttons.get(itemName).setVisibility(View.INVISIBLE);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("error 2");
                                }

                            }
                        } else {
                            resetUI();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    public void resetUI() {
        for (String itemName: itemNames) {
            buttons.get(itemName).setVisibility(View.VISIBLE);
            buttons.get(itemName).setText("request " + itemName.toLowerCase());
            textViews.get(itemName).setVisibility(View.INVISIBLE);
            layouts.get(itemName).setBackgroundColor(Color.parseColor("#4c5f6b"));
        }
    }

    public void vellaToast(View view) {
        if (isRequestAccepted) {
            Toast.makeText(this, "Return the issued item to issue something else", Toast.LENGTH_SHORT).show();
        }
    }

    public void requestFootball(View view) {
        requestEquipment(0);
    }

    public void requestBasketball(View view) {
        requestEquipment(1);
    }

    public void requestVolleyball(View view) {
        requestEquipment(2);
    }

    public void requestEquipment(final int equipment) {
        firstDialog.show();
        if (alertDialog != null)
            alertDialog.cancel();


        AlertDialog.Builder builder = new AlertDialog.Builder(EquipmentsActivity.this, R.style.AlertDialogTheme);
        builder.setIcon(R.drawable.castle);
        builder.setCancelable(false);

        final Equipment[] item = new Equipment[1];

        firebaseDb.child(FirebaseModel.Equipments.EQUIPMENTS)
                .child(FirebaseModel.Equipments.SPORTS)
                .child(itemNames[equipment])
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> issuedBy = dataSnapshot.child(FirebaseModel.Equipments.ISSUED_BY).getValue(t);
                        ArrayList<String> requestedBy = dataSnapshot.child(FirebaseModel.Equipments.REQUESTED_BY).getValue(t);
                        int quantity = dataSnapshot.child(FirebaseModel.Equipments.QUANTITY).getValue(Integer.class);

                        item[0] = new Equipment(quantity, issuedBy, requestedBy);




                        System.out.println(item[0].getQuantity() + "\n" + item[0].getIssuedBy() + "\n" + item[0].getRequestedBy());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        if (hasRequestedItem) {
            builder.setTitle("Cancel request?");
            builder.setMessage("Are you sure you want to cancel the request for " + itemNames[equipment].toLowerCase() + "?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    resetUI();

                    item[0].getRequestedBy().remove(item[0].getRequestedBy().indexOf(email.substring(0, email.indexOf('.'))));

                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.HAS_REQUESTED_ITEM).setValue(false);
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.REQUESTED_ITEM).setValue("-1");

                    firebaseDb.child(FirebaseModel.Equipments.EQUIPMENTS)
                            .child(FirebaseModel.Equipments.SPORTS)
                            .child(itemNames[equipment])
                            .setValue(item[0]);

                    Toast.makeText(EquipmentsActivity.this, "Request cancelled", Toast.LENGTH_SHORT).show();
                    firstDialog.cancel();
                }
            });
        } else {

            builder.setTitle("Issue " + itemNames[equipment].toLowerCase() + "?");
            builder.setMessage("Are you sure you want to request a " + itemNames[equipment].toLowerCase() + "?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    buttons.get(itemNames[equipment]).setText("Cancel Request");
                    layouts.get(itemNames[equipment]).setBackgroundColor(Color.parseColor("#52744c"));
                    for (String itemName: itemNames) {
                        if (!itemName.equals(itemNames[equipment])) {
                            buttons.get(itemName).setVisibility(View.INVISIBLE);
                        }
                    }
                    item[0].getRequestedBy().add(email.substring(0, email.indexOf('.')));
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.HAS_REQUESTED_ITEM).setValue(true);
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.REQUESTED_ITEM).setValue(itemNames[equipment]);
                    firebaseDb.child(FirebaseModel.Equipments.EQUIPMENTS)
                            .child(FirebaseModel.Equipments.SPORTS)
                            .child(itemNames[equipment])
                            .setValue(item[0]);
                    Toast.makeText(EquipmentsActivity.this, "Request accepted, you can now collect a " + itemNames[equipment].toLowerCase() + " from the store room", Toast.LENGTH_LONG).show();
                    firstDialog.cancel();
                }
            });

        }


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firstDialog.cancel();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

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
