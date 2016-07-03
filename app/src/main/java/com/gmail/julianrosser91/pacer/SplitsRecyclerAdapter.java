package com.gmail.julianrosser91.pacer;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.julianrosser91.pacer.model.TrackedRoute;

import java.util.ArrayList;
import java.util.List;

public class SplitsRecyclerAdapter extends RecyclerView.Adapter<SplitsRecyclerAdapter.SplitViewHolder> implements View.OnClickListener {

    private List<TrackedRoute> splits = new ArrayList<>();

    private IntentCallback mCallback;

    public SplitsRecyclerAdapter(IntentCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public SplitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_split_row, parent, false);
        SplitViewHolder vh = new SplitViewHolder(v, this);
        v.setTag(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(SplitViewHolder holder, int position) {
        TrackedRoute split = splits.get(position);
        holder.setTag(split);
        holder.setSplitPace(split.getTotalDistance());
        holder.setSplitNumber(position);
    }

    @Override
    public int getItemCount() {
        return splits.size();
    }

    public void setData(ArrayList<TrackedRoute> splits) {
        this.splits = splits;
        notifyDataSetChanged();
    }
    public void addSplit(TrackedRoute split) {
        this.splits.add(split);
    }

    @Override
    public void onClick(View v) {
        TrackedRoute split = (TrackedRoute) v.getTag();
        Intent intent = new Intent(Pacer.getmInstance(), MainActivity.class);
//        intent.putExtra(Constants.KEY_SPLIT_ID, split.getId());
        mCallback.onIntentReceived(intent);
    }

    public class SplitViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView mTextSplitNumber;
        private TextView mTextSplitPace;

        public SplitViewHolder(View view, View.OnClickListener clickListener) {
            super(view);
            this.view = view;
            mTextSplitNumber = (TextView) view.findViewById(R.id.text_split_number);
            mTextSplitPace = (TextView) view.findViewById(R.id.text_split_pace);
            view.setOnClickListener(clickListener);
        }

        public void setTag(TrackedRoute split) {
            view.setTag(split);
        }

        public void setSplitNumber(int position) {
            mTextSplitNumber.setText(String.valueOf(position));
        }

        public void setSplitPace(float pace) {
            mTextSplitPace.setText(String.valueOf(pace));
        }

    }

}
