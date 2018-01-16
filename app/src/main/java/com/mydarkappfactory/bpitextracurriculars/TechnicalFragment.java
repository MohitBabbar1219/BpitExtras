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

import java.util.ArrayList;


public class TechnicalFragment extends Fragment {

    View view;
    RecyclerViewAdapter adapter;
    SQLiteDatabase db;

    public TechnicalFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_frag_technical, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_technical);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        final ArrayList<Society> x = new ArrayList<>();
        x.add(new Society("Societies", Society.LOREM_IPSUM, R.drawable.ic_launcher_background, Society.NO_CATEGORY));
        adapter = new RecyclerViewAdapter(x);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        SQLiteOpenHelper dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();

        adapter.setListener(new RecyclerViewAdapter.Listener() {
            @Override
            public void onClick(int position) {
                SQLiteOpenHelper dbHelper = new DBHelper(getContext());
                db = dbHelper.getWritableDatabase();

                Cursor cursor = db.query("IS_LOGGED_IN", new String[]{"ANSWER"},
                        "_id = 1", null, null, null, null);
                cursor.moveToFirst();

                int answer = cursor.getInt(0);

                Log.d("Bpit", "" + answer);

                cursor.close();
                Intent intent;
                if (answer == -1) {
                    intent = new Intent(getContext(), LoginActivity.class);
                } else if (answer == 0) {
                    intent = new Intent(getContext(), FirstLoginActivity.class);
                } else {
                    intent = new Intent(getContext(), SocietyListActivity.class);
                    intent.putExtra("CategoryIndex", 1);
                }
                startActivity(intent);


            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
