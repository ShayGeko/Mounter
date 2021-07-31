package com.example.mounter.ridesearch;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mounter.data.model.RidePostingModel;
import com.example.mounter.databinding.RidePostingHolderBinding;
import com.example.mounter.directions.RideDetailsActivity;
import com.example.mounter.profile.UserProfileActivity;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RidePostingModel}.
 */
public class RidePostingRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<RidePostingModel, RidePostingRecyclerViewAdapter.RidePostingViewHolder>{

    private OrderedRealmCollection<RidePostingModel> ridePostings;

    public RidePostingRecyclerViewAdapter(OrderedRealmCollection<RidePostingModel> ridePostings) {
        super(ridePostings, true);
    }

    @Override
    public RidePostingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RidePostingHolderBinding binding = RidePostingHolderBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new RidePostingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final RidePostingViewHolder holder, int position) {
        holder.mItem = getItem(position);
        holder.mDestinationAddress.setText(holder.mItem.getDestinationAddress());
        holder.mOriginAddress.setText(holder.mItem.getOriginAddress());
        holder.mDepartureTime.setText(holder.mItem.getDepartureTime());

        holder.mProfileButton.setOnClickListener(t -> {
            Context context = holder.mDestinationAddress.getContext();
            Intent intent = new Intent(context, UserProfileActivity.class);
            context.startActivity(intent);
        });
    }

    public class RidePostingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mDestinationAddress;
        public final TextView mOriginAddress;
        public final TextView mDepartureTime;
        public final Button mProfileButton;

        public RidePostingModel mItem;

        public RidePostingViewHolder(RidePostingHolderBinding binding) {
            super(binding.getRoot());
            View view = binding.getRoot();
            view.setOnClickListener(this);

            mDestinationAddress = binding.destinationAddress;
            mOriginAddress = binding.originAddress;
            mDepartureTime = binding.departureTime;
            mProfileButton = binding.profileBtn;
        }

        @Override
        public void onClick(View v){
            Log.d("ViewHolder", "onClick fired");
            Context context = mDestinationAddress.getContext();
            Intent rideDetailsIntent = new Intent(v.getContext(), RideDetailsActivity.class);
            rideDetailsIntent.putExtra("ridePostingId", mItem.getId());

            context.startActivity(rideDetailsIntent);

        }
        @Override
        public String toString() {
            return super.toString() + " '" + mOriginAddress.getText() + "'";
        }
    }
}