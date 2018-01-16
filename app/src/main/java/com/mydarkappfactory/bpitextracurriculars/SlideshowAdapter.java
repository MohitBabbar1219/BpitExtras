package com.mydarkappfactory.bpitextracurriculars;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

public class SlideshowAdapter extends PagerAdapter {

    private Context context;
    LayoutInflater inflater;
    SQLiteDatabase db;
    public int[] images = {
            R.drawable.sports,
            R.drawable.technical,
            R.drawable.cultural
    };

    public SlideshowAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.slideshow_layout, container, false);
        ImageView img = view.findViewById(R.id.slideshow_image);
//        img.setImageResource(images[position]);
        Glide.with(context).load(images[position]).into(img);
        container.addView(view);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (position != 0) {
                    intent = new Intent(context, CategoryListActivity.class);
                    intent.putExtra("ImageIndex", position);
                } else {
                    SQLiteOpenHelper dbHelper = new DBHelper(context);
                    db = dbHelper.getWritableDatabase();

                    Cursor cursor = db.query("IS_LOGGED_IN", new String[]{"ANSWER"},
                            "_id = 1", null, null, null, null);
                    cursor.moveToFirst();

                    int answer = cursor.getInt(0);

                    Log.d("Bpit", "" + answer);

                    cursor.close();
                    if (answer == -1) {
                        intent = new Intent(context, LoginActivity.class);
                    } else if (answer == 0) {
                        intent = new Intent(context, FirstLoginActivity.class);
                    } else {
                        intent = new Intent(context, SportsMeetActivity.class);
                        intent.putExtra("CategoryIndex", 2);
                    }
                }
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
