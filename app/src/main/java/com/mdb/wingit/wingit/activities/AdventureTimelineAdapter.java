package com.mdb.wingit.wingit.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.vipulasri.timelineview.TimelineView;
import com.mdb.wingit.wingit.R;
import com.mdb.wingit.wingit.modelClasses.Pin;

import java.util.ArrayList;

/**
 * Recycler View of pins that populate Adventure Timeline Activity
 */

class AdventureTimelineAdapter extends RecyclerView.Adapter<AdventureTimelineAdapter.TimeLineViewHolder> {

    private Context context;
    private ArrayList<Pin> pins;

    AdventureTimelineAdapter(Context context, ArrayList<Pin> pins) {
        this.context = context;
        this.pins = pins;
    }

    @Override
    public int getItemCount() {
        //FIXME lol just personal preference, but ternary operators yo
        if (pins == null) {
            return 0;
        }
        return pins.size();
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.row_view_timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        Pin pin = pins.get(position);
        holder.pinName.setText(pin.getName());
        holder.pinTime.setText(pin.getStartTime());
        //FIXME pls put in a Utils class as a final variable or at least at top of this class
        String pinImageURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + pin.getImageURL() + "&key=" + CarouselActivity.API_KEY_UNRESTRICTED;
        Glide.with(context).load(pinImageURL).into(holder.pinImage);
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimeLineView;
        ImageView pinImage;
        TextView pinName;
        TextView pinTime;

        TimeLineViewHolder(View view, int viewType) {
            super(view);
            mTimeLineView = (TimelineView) view.findViewById(R.id.time_marker);
            mTimeLineView.initLine(viewType);

            pinImage = (ImageView) view.findViewById(R.id.pinImage);
            pinName = (TextView) view.findViewById(R.id.pinName);
            pinTime = (TextView) view.findViewById(R.id.time);
        }
    }
}
