package com.mydarkappfactory.bpitextracurriculars;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> titles,dates, content;

    public EventRecyclerViewAdapter(ArrayList<String> titles, ArrayList<String> content, ArrayList<String> dates) {
        this.titles = titles;
        this.content = content;
        this.dates = dates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.evetns_list_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView name = cardView.findViewById(R.id.headingEvent);
        TextView date = cardView.findViewById(R.id.dateEvent);
        TextView desc = cardView.findViewById(R.id.bodyEvent);
        name.setText(titles.get(position));
        desc.setText(content.get(position));
        date.setText(dates.get(position));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

}
