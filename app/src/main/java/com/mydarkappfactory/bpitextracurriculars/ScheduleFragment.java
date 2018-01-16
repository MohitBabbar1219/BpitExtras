package com.mydarkappfactory.bpitextracurriculars;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by dragonslayer on 21/12/17.
 */

public class ScheduleFragment extends Fragment {

    View view;
    DatabaseReference firebaseDb;
    ScheduleRecyclerViewAdapter adapter;
    ArrayList<String> titles, contents;

    public ScheduleFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_frag_schedule, container, false);

        titles = new ArrayList<>();
        contents = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_schedule);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        adapter = new ScheduleRecyclerViewAdapter(titles, contents);


        firebaseDb = FirebaseDatabase.getInstance().getReference();
        firebaseDb.child(FirebaseModel.SPORTS_MEET.SPORTS_MEET_TABLE)
                .child(FirebaseModel.SPORTS_MEET.EVENTS)
                .child(FirebaseModel.SPORTS_MEET.SCHEDULE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot schedule) {
                String footballBoys = schedule.child("Football").child("Boys").getValue(String.class);
                String footballGirls = schedule.child("Football").child("Girls").getValue(String.class);
                titles.add("Football");
                contents.add(bg(footballBoys, footballGirls));

                String basketballBoys = schedule.child("Basketball").child("Boys").getValue(String.class);
                String basketballGirls = schedule.child("Basketball").child("Girls").getValue(String.class);
                titles.add("Basketball");
                contents.add(bg(basketballBoys, basketballGirls));


                String volleyballBoys = schedule.child("Volleyball").child("Boys").getValue(String.class);
                String volleyballGirls = schedule.child("Volleyball").child("Girls").getValue(String.class);
                titles.add("Volleyball");
                contents.add(bg(volleyballBoys, volleyballGirls));

                String carromBoysSingle = schedule.child("Carrom").child("Boys").child("Singles").getValue(String.class);
                String carromBoysDouble = schedule.child("Carrom").child("Boys").child("Doubles").getValue(String.class);
                String carromGirlsSingle = schedule.child("Carrom").child("Girls").child("Singles").getValue(String.class);
                String carromGirlsDouble = schedule.child("Carrom").child("Girls").child("Doubles").getValue(String.class);
                titles.add("Carrom");
                contents.add(bgsd(carromBoysSingle, carromBoysDouble, carromGirlsSingle, carromGirlsDouble));


                String chessBoys = schedule.child("Chess").child("Boys").getValue(String.class);
                String chessGirls = schedule.child("Chess").child("Girls").getValue(String.class);
                titles.add("Chess");
                contents.add(bg(chessBoys, chessGirls));

                String shotPutBoys = schedule.child("Shot put").child("Boys").getValue(String.class);
                String shotPutGirls = schedule.child("Shot put").child("Girls").getValue(String.class);
                titles.add("Shot put");
                contents.add(bg(shotPutBoys, shotPutGirls));

                String throwBallBoys = schedule.child("Throw ball").child("Boys").getValue(String.class);
                String throwBallGirls = schedule.child("Throw ball").child("Girls").getValue(String.class);
                titles.add("Throw ball");
                contents.add(bg(throwBallBoys, throwBallGirls));

                String discussThrowBoys = schedule.child("Discuss throw").child("Boys").getValue(String.class);
                String discussThrowGirls = schedule.child("Discuss throw").child("Girls").getValue(String.class);
                titles.add("Discuss throw");
                contents.add(bg(discussThrowBoys, discussThrowGirls));

                String longJumpBoys = schedule.child("Long jump").child("Boys").getValue(String.class);
                String longJumpGirls = schedule.child("Long jump").child("Girls").getValue(String.class);
                titles.add("Long jump");
                contents.add(bg(longJumpBoys, longJumpGirls));

                String cricket = schedule.child("Cricket").child("Boys").getValue(String.class);
                titles.add("Cricket");
                contents.add(cricketStr(cricket));

                String badBoysSing = schedule.child("Badminton").child("Boys").child("Singles").getValue(String.class);
                String badBoysDoubles = schedule.child("Badminton").child("Boys").child("Doubles").getValue(String.class);
                String badGirlsSing = schedule.child("Badminton").child("Girls").child("Singles").getValue(String.class);
                String badGirlsDoubles = schedule.child("Badminton").child("Girls").child("Doubles").getValue(String.class);
                titles.add("Badminton");
                contents.add(bgsd(badBoysSing, badBoysDoubles, badGirlsSing, badGirlsDoubles));

                String ttBoysSing = schedule.child("Table tennis").child("Boys").child("Singles").getValue(String.class);
                String ttBoysDoubles = schedule.child("Table tennis").child("Boys").child("Doubles").getValue(String.class);
                String ttGirlsSing = schedule.child("Table tennis").child("Girls").child("Singles").getValue(String.class);
                String ttGirlsDub = schedule.child("Table tennis").child("Girls").child("Doubles").getValue(String.class);
                titles.add("Table tennis");
                contents.add(bgsd(ttBoysSing, ttBoysDoubles, ttGirlsSing, ttGirlsDub));

                String armBoy55 = schedule.child("Arm wrestling").child("Boys").child("55-65KG").getValue(String.class);
                String armBoy65 = schedule.child("Arm wrestling").child("Boys").child("65-75KG").getValue(String.class);
                String armBoy75 = schedule.child("Arm wrestling").child("Boys").child("75+KG").getValue(String.class);
                String armGirl60m = schedule.child("Arm wrestling").child("Girls").child("60-KG").getValue(String.class);
                String armGirl60p = schedule.child("Arm wrestling").child("Girls").child("60+KG").getValue(String.class);
                titles.add("Arm wrestling");
                contents.add(wtLifting(armBoy55, armBoy65, armBoy75, armGirl60m, armGirl60p));
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public String bg(String boys, String girls){
        return "\tBoys: " + boys + "\n\tGirls: " + girls;
    }
    public String cricketStr(String boys){
        return "\tBoys: " + boys;
    }
    public String bgsd(String bs, String bd, String gs, String gd) {
       return "\tBoys:\n\t\tSingles: " + bs + "\n\t\tDoubles: " + bd + "\n\tGirls:\n\t\tSingles: " + gs + "\n\t\tDoubles: " + gd;
    }
    public String wtLifting(String bs, String bd, String bf, String gs, String gd) {
        return "\tBoys:\n\t\t55-65KG: " + bs + "\n\t\t65-75KG: " + bd + "\n\t\t75+KG: " + bf + "\n\tGirls:\n\t\t60-KG: " + gs + "\n\t\t60+KG: " + gd;
    }

}
