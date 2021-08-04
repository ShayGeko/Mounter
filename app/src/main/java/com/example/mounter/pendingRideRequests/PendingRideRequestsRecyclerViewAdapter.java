package com.example.mounter.pendingRideRequests;

import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mounter.R;
import com.example.mounter.data.realmModels.RidePostingModel;
import com.example.mounter.data.realmModels.RideRequestModel;
import com.example.mounter.data.realmModels.UserInfoModel;
import com.example.mounter.databinding.RideRequestHolderBinding;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RideRequestModel}.
 */
public class PendingRideRequestsRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<RideRequestModel, PendingRideRequestsRecyclerViewAdapter.RideRequestViewHolder>{
    private final PendingRideRequestsViewModel viewModel;
    private OrderedRealmCollection<RideRequestModel> rideRequests;

    public PendingRideRequestsRecyclerViewAdapter(OrderedRealmCollection<RideRequestModel> rideRequests, PendingRideRequestsViewModel viewModel) {
        super(rideRequests, true);
        this.viewModel = viewModel;
    }

    @Override
    public RideRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RideRequestHolderBinding binding = RideRequestHolderBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new RideRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final RideRequestViewHolder holder, int position) {
        holder.mItem = getItem(position);
        if(holder.mItem.isADriverRequest()){
            holder.mView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.golden));
        }
        RealmResults<UserInfoModel> users = holder.mItem.getPassenger();
        if(users.size() > 0) {
            UserInfoModel user = users.first();
            holder.mUserName.setText(user.getName() + " " + user.getSurname());
            holder.mRating.setText(""+user.getRating());
            // holder.mOriginAddress.setText(user.getPfpId());
        }

        RealmResults<RidePostingModel> ridePostings = holder.mItem.getRidePosting();
        if(ridePostings.size() > 0) {
            RidePostingModel ridePosting = ridePostings.first();
            holder.mDestinationAddress.setText(ridePosting.getDestinationAddress());
            // holder.mOriginAddress.setText(user.getPfpId());
        }


        holder.mAcceptBtn.setOnClickListener(t -> {
            viewModel.rideRequestAccepted(holder.mItem.getId());
        });
        holder.mDeclineBtn.setOnClickListener(t -> {
            viewModel.rideRequestDeclined(holder.mItem.getId());
        });
    }

    public class RideRequestViewHolder extends RecyclerView.ViewHolder {
        public final TextView mUserName;
        public final TextView mDestinationAddress;
        public final TextView mRating;
        public final ImageButton mAcceptBtn;
        public final ImageButton mDeclineBtn;
        public final View mView;

        public RideRequestModel mItem;

        public RideRequestViewHolder(RideRequestHolderBinding binding) {
            super(binding.getRoot());

            mUserName = binding.userName;
            mDestinationAddress = binding.destinationAddress;
            mRating = binding.rating;
            mAcceptBtn = binding.acceptBtn;
            mDeclineBtn = binding.declineBtn;
            mView = binding.getRoot();
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mDestinationAddress.getText() + "'";
        }
    }
}