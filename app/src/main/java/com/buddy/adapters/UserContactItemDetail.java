package com.buddy.adapters;

import android.support.annotation.Nullable;

import com.buddy.entity.UserContact;

import androidx.recyclerview.selection.ItemDetailsLookup;

public class UserContactItemDetail extends ItemDetailsLookup.ItemDetails {
    private int position;
    private UserContact item;

    public UserContactItemDetail(int position, UserContact item) {
        this.position = position;
        this.item = item;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Nullable
    @Override
    public Object getSelectionKey() {
        return item;
    }
}
