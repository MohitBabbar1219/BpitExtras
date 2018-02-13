package com.mydarkappfactory.bpitextracurriculars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SportsMeetRegisterationActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    TextView coordinatorsTextView;
    DatabaseReference firebaseDb;
    ProgressDialog firstDialog;
    TextView coordinators, cricketText;
    RadioGroup genderGroup, distanceGroup, categoryGroup, maleWeightGroup, femaleWeightGroup;
    Button submitButt;
    //    RadioButton maleRadio, femaleRadio, singlesRadio, doublesRadio;
    EditText partnerName, partnerEnrollment;
    RelativeLayout genderLayout, distanceLayout, categoryLayout, partnerDetailsLayout, maleWeightLayout, femaleWeightLayout;
    boolean isCricket, isArmWrestling, isSingleDouble;
    String category = null, gender = null, partnerNameTxt = null, partnerEnrollmentText = null;
    Society society;
    String email;
    ArrayList<String> invalidEvents;
    String regCat;
    String eventCat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_meet_registeration);

        toolbar = findViewById(R.id.toolbar_sports_reg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        coordinatorsTextView = findViewById(R.id.coordinators);
        firebaseDb = FirebaseDatabase.getInstance().getReference();

        Gson gson = new Gson();

        Intent getIntent = getIntent();

        society = gson.fromJson(getIntent.getStringExtra("Event"), Society.class);
        getSupportActionBar().setTitle(society.getName() + " Registration");
        firstDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);

        firstDialog.setTitle("Loading");
        firstDialog.setMessage("Downloading content, please wait...");
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.setCancelable(false);
        firstDialog.show();
        email = getIntent.getStringExtra("Email");

        invalidEvents = getIntent().getStringArrayListExtra("InvalidEvents");

        invalidEvents = removeDuplicates(invalidEvents);
        Log.d("Bpit", "invalid events " + invalidEvents.toString());
        eventCat = society.getCategory();
        if (eventCat.equals("BGSD")) {
            if (invalidEvents.contains(society.getName() + "S")) {
                regCat = "Singles";
            } else if (invalidEvents.contains(society.getName() + "D")) {
                regCat = "Doubles";
            } else {
                regCat = "null";
            }
        } else if (eventCat.equals("BdistGdist")) {
            if (invalidEvents.contains(society.getName() + "100")) {
                regCat = "100";
            } else if (invalidEvents.contains(society.getName() + "200")) {
                regCat = "200";
            } else {
                regCat = "null";
            }
        }
        Log.d("Bpit", "regCat: " + regCat);

        setCoordinators(society.getName());

        submitButt = findViewById(R.id.sportsRegFormSubmitButton);
        genderLayout = findViewById(R.id.genderLayout);
        distanceLayout = findViewById(R.id.distanceLayout);
        partnerDetailsLayout = findViewById(R.id.partnerDetails);
        categoryLayout = findViewById(R.id.categoryLayout);
        cricketText = findViewById(R.id.throwBallMessage);
        coordinators = findViewById(R.id.coordinators);
        distanceGroup = findViewById(R.id.distanceGroup);
        genderGroup = findViewById(R.id.genderRadioGroup);
        categoryGroup = findViewById(R.id.categoryRadioGroup);
        partnerName = findViewById(R.id.partnerName);
        partnerEnrollment = findViewById(R.id.partnerEnrollment);
        femaleWeightLayout = findViewById(R.id.femaleWeightLayout);
        maleWeightLayout = findViewById(R.id.maleWeightLayout);
        maleWeightGroup = findViewById(R.id.maleWeightRadioGroup);
        femaleWeightGroup = findViewById(R.id.femaleWeightRadioGroup);

        if (this.society.getName().equals("Throw ball")) {
            clearUI();
            cricketText.setVisibility(View.VISIBLE);
            isCricket = true;
        } else if (this.society.getName().equals("Arm wrestling")) {
            clearUI();
            genderLayout.setVisibility(View.VISIBLE);
            isArmWrestling = true;
        } else if (this.society.getName().equals("Athletics")) {
            clearUI();
            genderLayout.setVisibility(View.VISIBLE);
            distanceLayout.setVisibility(View.VISIBLE);
        }
        else {
            clearUI();
            genderLayout.setVisibility(View.VISIBLE);
            if (this.society.getCategory().equals(Society.SPORTS.BGSD)) {
                categoryLayout.setVisibility(View.VISIBLE);
                isSingleDouble = true;
            } else {
                isSingleDouble = false;
            }

        }

        partnerDetailsLayout.setVisibility(View.GONE);

    }



    public void setCoordinators(String eventName) {
        DatabaseReference dbref = firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE).child(FirebaseModel.SPORTS_MEET.EVENTS).child(FirebaseModel.SPORTS_MEET.COORDINATORS).child(eventName);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder coordinators = new StringBuilder("");
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    coordinators.append(snapshot.getKey() + ": " + snapshot.getValue(String.class) + "\n");
                }
                coordinatorsTextView.setText(coordinators.toString());
                firstDialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public ArrayList<String> removeDuplicates(ArrayList<String> x) {
        HashMap<String, Integer> duplicateRemoval = new HashMap<>();
        for (String y: x) {
            duplicateRemoval.put(y, 0);
        }
        ArrayList<String> z = new ArrayList<>();
        Log.d("Bpit", "duprem " + duplicateRemoval.keySet().toString());
        for (String y: duplicateRemoval.keySet()) {
            if (y != null && !y.equals("-1")) {
                Log.d("Bpit", "kjdsnsa");
                z.add(y);
            }
        }
        z.add(0, "-1");
        Log.d("Bpit", x.toString());
        Log.d("Bpit", z.toString());
        return z;
    }

    public void setWeightMale(View view) {
        int id = maleWeightGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.wt55to65:
                category = Society.SPORTS_CATEGORIES.FIFTYFIVE_TO_SIXTYFIVE;
                break;
            case R.id.wt65to75:
                category = Society.SPORTS_CATEGORIES.SIXTYFIVE_TO_SEVENTYFIVE;
                break;
            case R.id.wt75plus:
                category = Society.SPORTS_CATEGORIES.SEVENTYFIVE_PLUS;
                break;

        }
    }

    public void setWeightFemale(View view) {
        int id = femaleWeightGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.wtPlus60:
                category = Society.SPORTS_CATEGORIES.SIXTY_PLUS;
                break;
            case R.id.wtMinus60:
                category = Society.SPORTS_CATEGORIES.SIXTY_MINUS;
                break;

        }
    }

    public void setDistance(View view) {
        int id = distanceGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.hundredMeter:
                if (regCat.equals("100")) {
                    Toast.makeText(this, "You are already registered for 100m.", Toast.LENGTH_SHORT).show();
                    RadioButton singleButton = findViewById(R.id.hundredMeter);
                    singleButton.setChecked(false);
                    category = null;
                } else {
                    category = Society.SPORTS_CATEGORIES.ONE_HUNDRED;
                }
                break;
            case R.id.twoHundredMeter:
                if (regCat.equals("200")) {
                    Toast.makeText(this, "You are already registered for 200m.", Toast.LENGTH_SHORT).show();
                    RadioButton singleButton = findViewById(R.id.twoHundredMeter);
                    singleButton.setChecked(false);
                    category = null;
                } else {
                    category = Society.SPORTS_CATEGORIES.TWO_HUNDRED;
                }
                break;

        }
    }

    public void setGender(View view) {
        int id = genderGroup.getCheckedRadioButtonId();
        if (society.getCategory().equals(Society.SPORTS.BwtGwt)) {
            switch (id) {
                case R.id.radioMale:
                    maleWeightLayout.setTranslationX(-1000);
                    maleWeightLayout.setVisibility(View.VISIBLE);
                    maleWeightLayout.animate().translationXBy(1000).setDuration(500);
                    femaleWeightLayout.setVisibility(View.GONE);
                    gender = Society.SPORTS_GENDERS.MALE;
                    break;
                case R.id.radioFemale:
                    femaleWeightLayout.setTranslationX(-1000);
                    femaleWeightLayout.setVisibility(View.VISIBLE);
                    femaleWeightLayout.animate().translationXBy(1000).setDuration(500);
                    maleWeightLayout.setVisibility(View.GONE);
                    gender = Society.SPORTS_GENDERS.FEMALE;
                    break;
            }
        } else {
            switch (id) {
                case R.id.radioMale:
                    gender = Society.SPORTS_GENDERS.MALE;
                    break;
                case R.id.radioFemale:
                    gender = Society.SPORTS_GENDERS.FEMALE;
                    break;
            }
        }
    }

    public void setCategory(View view) {
        int id = categoryGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.radioSingles:
                //TODO
                if (!regCat.equals("Singles")) {
                    submitButt.setAlpha(0);
                    partnerDetailsLayout.setVisibility(View.GONE);
                    submitButt.animate().alpha(1).setDuration(500);
                    category = Society.SPORTS_CATEGORIES.SINGLES;
                } else {
                    Toast.makeText(this, "You are already registered for singles.", Toast.LENGTH_SHORT).show();
                    RadioButton singleButton = findViewById(R.id.radioSingles);
                    singleButton.setChecked(false);
                    category = null;
                }
                break;
            case R.id.radioDoubles:
                if (!regCat.equals("Doubles")) {
                    submitButt.setAlpha(0);
                    partnerDetailsLayout.setTranslationX(-1000);
                    partnerDetailsLayout.setVisibility(View.VISIBLE);
                    partnerDetailsLayout.animate().translationXBy(1000).setDuration(500);
                    submitButt.animate().alpha(1).setDuration(500);
                    category = Society.SPORTS_CATEGORIES.DOUBLES;
                } else {
                    Toast.makeText(this, "You are already registered for doubles", Toast.LENGTH_SHORT).show();
                    RadioButton singleButton = findViewById(R.id.radioDoubles);
                    singleButton.setChecked(false);
                    category = null;
                }
                break;
        }
    }

    public void attemptRegister(View view) {
//        ProgressDialog firstDialog = new ProgressDialog(SportsMeetRegisterationActivity.this, R.style.ProgressDialogTheme);

        firstDialog.show();
        if (society.getName().equals("Throw ball")) {
            SportsForm form = new SportsForm(Society.SPORTS_GENDERS.FEMALE,
                    Society.SPORTS.G,
                    "null",
                    "null");
            firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                    .child(FirebaseModel.SPORTS_MEET.EVENTS)
                    .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                    .child(society.getName())
                    .child(email.substring(0, email.indexOf('.')))
                    .setValue(form);
            invalidEvents.add(society.getName());
            firebaseDb.child(FirebaseModel.Users.USERS)
                    .child(email.substring(0, email.indexOf('.')))
                    .child(FirebaseModel.Users.EVENTS_REGISTERED)
                    .setValue(invalidEvents);
            Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
            finish();
        } else if (society.getName().equals("Arm wrestling")) {
            if (category == null || gender == null) {
                Toast.makeText(this, "All details are required", Toast.LENGTH_SHORT).show();
            } else {
                SportsForm form = new SportsForm(gender,
                        category,
                        "null",
                        "null");
                firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                        .child(FirebaseModel.SPORTS_MEET.EVENTS)
                        .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                        .child(society.getName())
                        .child(email.substring(0, email.indexOf('.')))
                        .setValue(form);

                invalidEvents.add(society.getName());
                firebaseDb.child(FirebaseModel.Users.USERS)
                        .child(email.substring(0, email.indexOf('.')))
                        .child(FirebaseModel.Users.EVENTS_REGISTERED)
                        .setValue(invalidEvents);
                Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (society.getCategory().equals(Society.SPORTS.BdistGdist)) {
            if (category == null || gender == null) {
                Toast.makeText(this, "All details are required", Toast.LENGTH_SHORT).show();
            } else if (category.equals(Society.SPORTS_CATEGORIES.ONE_HUNDRED)){
                if (regCat.equals("null")) {
                    SportsForm form = new SportsForm(gender,
                            category,
                            "null",
                            "null");
                    firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                            .child(FirebaseModel.SPORTS_MEET.EVENTS)
                            .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                            .child(society.getName())
                            .child(email.substring(0, email.indexOf('.')))
                            .setValue(form);
                    invalidEvents.add(society.getName() + "100");
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.EVENTS_REGISTERED)
                            .setValue(invalidEvents);
                    Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                            .child(FirebaseModel.SPORTS_MEET.EVENTS)
                            .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                            .child(society.getName())
                            .child(email.substring(0, email.indexOf('.')))
                            .child("category")
                            .setValue("100200");
                    invalidEvents.add(society.getName() + "100200");
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.EVENTS_REGISTERED)
                            .setValue(invalidEvents);
                    Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                if (regCat.equals("null")) {
                    SportsForm form = new SportsForm(gender,
                            category,
                            "null",
                            "null");
                    firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                            .child(FirebaseModel.SPORTS_MEET.EVENTS)
                            .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                            .child(society.getName())
                            .child(email.substring(0, email.indexOf('.')))
                            .setValue(form);
                    invalidEvents.add(society.getName() + "200");
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.EVENTS_REGISTERED)
                            .setValue(invalidEvents);
                    Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                            .child(FirebaseModel.SPORTS_MEET.EVENTS)
                            .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                            .child(society.getName())
                            .child(email.substring(0, email.indexOf('.')))
                            .child("category")
                            .setValue("100200");
                    invalidEvents.add(society.getName() + "100200");
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.EVENTS_REGISTERED)
                            .setValue(invalidEvents);
                    Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } else if (society.getCategory().equals(Society.SPORTS.BGSD)) {
            if (category == null || gender == null) {
                Toast.makeText(this, "All details are required", Toast.LENGTH_SHORT).show();
            } else if (category.equals(Society.SPORTS_CATEGORIES.DOUBLES)) {
                partnerNameTxt = partnerName.getText().toString();
                partnerEnrollmentText = partnerEnrollment.getText().toString();
                if (partnerNameTxt.isEmpty() || partnerEnrollmentText.isEmpty()) {
                    Toast.makeText(this, "All details are required", Toast.LENGTH_SHORT).show();
                } else {
                    if (regCat.equals("null")) {
                        SportsForm form = new SportsForm(gender,
                                category,
                                partnerNameTxt,
                                partnerEnrollmentText);
                        firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                                .child(FirebaseModel.SPORTS_MEET.EVENTS)
                                .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                                .child(society.getName())
                                .child(email.substring(0, email.indexOf('.')))
                                .setValue(form);
                        invalidEvents.add(society.getName() + "D");
                        firebaseDb.child(FirebaseModel.Users.USERS)
                                .child(email.substring(0, email.indexOf('.')))
                                .child(FirebaseModel.Users.EVENTS_REGISTERED)
                                .setValue(invalidEvents);
                        Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                                .child(FirebaseModel.SPORTS_MEET.EVENTS)
                                .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                                .child(society.getName())
                                .child(email.substring(0, email.indexOf('.')))
                                .child("category")
                                .setValue("SD");
                        invalidEvents.add(society.getName() + "SD");
                        firebaseDb.child(FirebaseModel.Users.USERS)
                                .child(email.substring(0, email.indexOf('.')))
                                .child(FirebaseModel.Users.EVENTS_REGISTERED)
                                .setValue(invalidEvents);
                        Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            } else {
                if (regCat.equals("null")) {
                    SportsForm form = new SportsForm(gender,
                            category,
                            "null",
                            "null");
                    firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                            .child(FirebaseModel.SPORTS_MEET.EVENTS)
                            .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                            .child(society.getName())
                            .child(email.substring(0, email.indexOf('.')))
                            .setValue(form);
                    invalidEvents.add(society.getName() + "S");
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.EVENTS_REGISTERED)
                            .setValue(invalidEvents);
                    Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                            .child(FirebaseModel.SPORTS_MEET.EVENTS)
                            .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                            .child(society.getName())
                            .child(email.substring(0, email.indexOf('.')))
                            .child("category")
                            .setValue("SD");
                    invalidEvents.add(society.getName() + "SD");
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.EVENTS_REGISTERED)
                            .setValue(invalidEvents);
                    Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } else {
            if (gender == null) {
                Toast.makeText(this, "All details are required", Toast.LENGTH_SHORT).show();
            } else {
                SportsForm form = new SportsForm(gender,
                        Society.SPORTS_CATEGORIES.TEAM_MEMBER,
                        "null",
                        "null");
                firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                        .child(FirebaseModel.SPORTS_MEET.EVENTS)
                        .child(FirebaseModel.SPORTS_MEET.REGISTRATIONS)
                        .child(society.getName())
                        .child(email.substring(0, email.indexOf('.')))
                        .setValue(form);
                invalidEvents.add(society.getName());
                firebaseDb.child(FirebaseModel.Users.USERS)
                        .child(email.substring(0, email.indexOf('.')))
                        .child(FirebaseModel.Users.EVENTS_REGISTERED)
                        .setValue(invalidEvents);
                Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        firstDialog.cancel();

    }



    public void clearUI() {
        categoryLayout.setVisibility(View.GONE);
        cricketText.setVisibility(View.GONE);
        maleWeightLayout.setVisibility(View.GONE);
        femaleWeightLayout.setVisibility(View.GONE);
        genderLayout.setVisibility(View.GONE);
        partnerDetailsLayout.setVisibility(View.GONE);
        distanceLayout.setVisibility(View.GONE);
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
