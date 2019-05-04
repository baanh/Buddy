package com.buddy.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.selection.ItemDetailsLookup;

public class UserContactItemDetailLookup extends ItemDetailsLookup {
    private final RecyclerView mRecyclerView;

    public UserContactItemDetailLookup(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            if (holder instanceof InviteeListAdapter.InviteeViewHolder) {
                return ((InviteeListAdapter.InviteeViewHolder) holder).getItemDetails();
            }
        }
        return null;
    }
}
