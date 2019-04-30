package com.buddy.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.buddy.entity.UserContact;
import com.buddy.main.R;

import java.util.List;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;

public class InviteeListAdapter extends RecyclerView.Adapter<InviteeListAdapter.InviteeViewHolder> {
    private List<UserContact> mDataset;
    private SelectionTracker mSelectionTracker;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class InviteeViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView txtName;
        private TextView txtPhone;
        private TextView txtEmail;

        InviteeViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txt_name);
            txtPhone = v.findViewById(R.id.txt_phone);
            txtEmail = v.findViewById(R.id.txt_email);
        }

        void bind(UserContact item, boolean isSelected) {
            txtName.setText(item.getName());
            txtEmail.setText(item.getEmail());
            txtPhone.setText(item.getPhone());
            // If the item is selected then we change its state to activated
            txtName.setActivated(isSelected);
            txtPhone.setActivated(isSelected);
            txtEmail.setActivated(isSelected);
        }

        ItemDetailsLookup.ItemDetails getItemDetails() {
            return new UserContactItemDetail(getAdapterPosition(), mDataset.get(getAdapterPosition()));
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InviteeListAdapter(List<UserContact> myDataset) {
        this.mDataset = myDataset;
    }

    public void setSelectionTracker(SelectionTracker mSelectionTracker) {
        this.mSelectionTracker = mSelectionTracker;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InviteeListAdapter.InviteeViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invitee_contact_list_row, parent, false);
        InviteeViewHolder vh = new InviteeViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(InviteeViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        UserContact item = mDataset.get(position);
        holder.bind(item, mSelectionTracker.isSelected(item));
        holder.txtName.setText(item.getName());
        holder.txtPhone.setText(item.getPhone());
        holder.txtEmail.setText(item.getEmail());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataset.get(position).hashCode();
    }
}
