package com.example.mounter.ridesearch;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mounter.data.model.RidePostingModel;
import com.example.mounter.databinding.FragmentItemBinding;
import com.example.mounter.directions.MyRideActivity;

import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RidePostingModel}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RidePostingRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<RidePostingModel, RidePostingRecyclerViewAdapter.ViewHolder>{

    private OrderedRealmCollection<RidePostingModel> ridePostings;

    public RidePostingRecyclerViewAdapter(OrderedRealmCollection<RidePostingModel> ridePostings) {
        super(ridePostings, true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getItem(position);
        holder.mIdView.setText(holder.mItem.getDestinationAddress());
        String departureDate = holder.mItem.getDepartureTime().toString();
        if(departureDate == null) holder.mContentView.setText("");
        else holder.mContentView.setText(departureDate.toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mIdView;
        public final TextView mContentView;
        public RidePostingModel mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            View view = binding.getRoot();
            view.setOnClickListener(this);
            mIdView = binding.itemNumber;

            mContentView = binding.content;
        }

        @Override
        public void onClick(View v){
            Log.d("ViewHolder", "onClick fired");
            Context context = mIdView.getContext();
            Intent rideDetailsIntent = new Intent(context, MyRideActivity.class);
            rideDetailsIntent.putExtra("ridePostingId", mItem.get_id());

            context.startActivity(rideDetailsIntent);

        }
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}