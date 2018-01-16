package com.mydarkappfactory.bpitextracurriculars;

import android.app.ProgressDialog;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SportsMeetRegisterationActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    TextView coordinators, cricketText;
    RadioGroup genderGroup, categoryGroup, maleWeightGroup, femaleWeightGroup;
    Button submitButt;
//    RadioButton maleRadio, femaleRadio, singlesRadio, doublesRadio;
    EditText partnerName, partnerEnrollment;
    RelativeLayout genderLayout, categoryLayout, partnerDetailsLayout, maleWeightLayout, femaleWeightLayout;
    boolean isCricket, isArmWrestling, isSingleDouble;
    String category = null, gender = null, partnerNameTxt = null, partnerEnrollmentText = null;
    DatabaseReference firebaseDb;
    Society society;
    String email;
    ArrayList<String> invalidEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_meet_registeration);

        toolbar = findViewById(R.id.toolbar_sports_reg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");

        firebaseDb = FirebaseDatabase.getInstance().getReference();

        invalidEvents = getIntent().getStringArrayListExtra("InvalidEvents");

        invalidEvents = removeDuplicates(invalidEvents);

        society = new Society(getIntent().getStringExtra("Event"));
        email = getIntent().getStringExtra("Email");

        submitButt = findViewById(R.id.sportsRegFormSubmitButton);
        genderLayout = findViewById(R.id.genderLayout);
        partnerDetailsLayout = findViewById(R.id.partnerDetails);
        categoryLayout = findViewById(R.id.categoryLayout);
        cricketText = findViewById(R.id.cricketMessage);
        coordinators = findViewById(R.id.coordinators);
        genderGroup = findViewById(R.id.genderRadioGroup);
        categoryGroup = findViewById(R.id.categoryRadioGroup);
        partnerName = findViewById(R.id.partnerName);
        partnerEnrollment = findViewById(R.id.partnerEnrollment);
        femaleWeightLayout = findViewById(R.id.femaleWeightLayout);
        maleWeightLayout = findViewById(R.id.maleWeightLayout);
        maleWeightGroup = findViewById(R.id.maleWeightRadioGroup);
        femaleWeightGroup = findViewById(R.id.femaleWeightRadioGroup);

        if (society.getName().equals("Cricket")) {
            clearUI();
            cricketText.setVisibility(View.VISIBLE);
            isCricket = true;
        } else if (society.getName().equals("Arm wrestling")) {
            clearUI();
            genderLayout.setVisibility(View.VISIBLE);
            isArmWrestling = true;
        } else {
            clearUI();
            genderLayout.setVisibility(View.VISIBLE);
            if (society.getCategory().equals(Society.SPORTS.BGSD)) {
                categoryLayout.setVisibility(View.VISIBLE);
                isSingleDouble = true;
            } else {
                isSingleDouble = false;
            }

        }

        partnerDetailsLayout.setVisibility(View.GONE);

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
                submitButt.setAlpha(0);
                partnerDetailsLayout.setVisibility(View.GONE);
                submitButt.animate().alpha(1).setDuration(500);
                category = Society.SPORTS_CATEGORIES.SINGLES;
                break;
            case R.id.radioDoubles:
                submitButt.setAlpha(0);
                partnerDetailsLayout.setTranslationX(-1000);
                partnerDetailsLayout.setVisibility(View.VISIBLE);
                partnerDetailsLayout.animate().translationXBy(1000).setDuration(500);
                submitButt.animate().alpha(1).setDuration(500);
                category = Society.SPORTS_CATEGORIES.DOUBLES;
                break;
        }
    }

    public void attemptRegister(View view) {
        ProgressDialog firstDialog = new ProgressDialog(SportsMeetRegisterationActivity.this, R.style.ProgressDialogTheme);

        firstDialog.setTitle("Loading");
        firstDialog.setMessage("Downloading content, please wait...");
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.setCancelable(false);
        firstDialog.show();
        if (society.getName().equals("Cricket")) {
            SportsForm form = new SportsForm(Society.SPORTS_GENDERS.MALE,
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
        } else if (society.getCategory().equals(Society.SPORTS.BGSD)) {
            if (category == null || gender == null) {
                Toast.makeText(this, "All details are required", Toast.LENGTH_SHORT).show();
            } else if (category.equals(Society.SPORTS_CATEGORIES.DOUBLES)) {
                partnerNameTxt = partnerName.getText().toString();
                partnerEnrollmentText = partnerEnrollment.getText().toString();
                if (partnerNameTxt.isEmpty() || partnerEnrollmentText.isEmpty()) {
                    Toast.makeText(this, "All details are required", Toast.LENGTH_SHORT).show();
                } else {
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
                    invalidEvents.add(society.getName());
                    firebaseDb.child(FirebaseModel.Users.USERS)
                            .child(email.substring(0, email.indexOf('.')))
                            .child(FirebaseModel.Users.EVENTS_REGISTERED)
                            .setValue(invalidEvents);
                    Toast.makeText(this, "Registration has been successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
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

    public void clearUI() {
        categoryLayout.setVisibility(View.GONE);
        cricketText.setVisibility(View.GONE);
        maleWeightLayout.setVisibility(View.GONE);
        femaleWeightLayout.setVisibility(View.GONE);
        genderLayout.setVisibility(View.GONE);
        partnerDetailsLayout.setVisibility(View.GONE);
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
