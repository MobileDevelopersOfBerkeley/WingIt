package com.mdb.wingit.wingit.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.vipulasri.timelineview.TimelineView;
import com.mdb.wingit.wingit.R;

class AdventureTimelineAdapter extends RecyclerView.Adapter<AdventureTimelineAdapter.TimeLineViewHolder> {

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.row_view_timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimeLineView;
        ImageView pinImage;
        TextView pinName;

        TimeLineViewHolder(View view, int viewType) {
            super(view);
            mTimeLineView = (TimelineView) view.findViewById(R.id.time_marker);
            mTimeLineView.initLine(viewType);

            pinImage = (ImageView) view.findViewById(R.id.pinImage);
            pinName = (TextView) view.findViewById(R.id.pinName);
        }
    }
}
