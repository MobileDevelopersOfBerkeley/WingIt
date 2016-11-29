package com.mdb.wingit.wingit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KJ on 11/11/16.
 */

public class AdventureAdapter extends RecyclerView.Adapter<AdventureAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Adventure> adventures = new ArrayList();

    public AdventureAdapter(Context context, ArrayList<Adventure> adventures) {
        this.context = context;
        this.adventures = adventures;
    }

    @Override
    public int getItemCount() {
        return adventures.size();
    }

    @Override
    public AdventureAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_adventure_log, parent, false);
        return new AdventureAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Adventure adventure = adventures.get(position);
        holder.adventureName.setText(adventure.getStartloc());
        holder.date.setText(adventure.getDate());
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView adventureName;
        TextView date;

        public CustomViewHolder (View view) {
            super(view);
            this.adventureName = (TextView) view.findViewById(R.id.adventureName);
            this.date = (TextView) view.findViewById(R.id.date);


        }
    }
}
