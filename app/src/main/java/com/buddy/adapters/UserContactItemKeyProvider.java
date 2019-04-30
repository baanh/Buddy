package com.buddy.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.buddy.entity.UserContact;

import java.util.List;

import androidx.recyclerview.selection.ItemKeyProvider;

public class UserContactItemKeyProvider extends ItemKeyProvider<UserContact> {
    private List<UserContact> items;

    public UserContactItemKeyProvider(int scope, List<UserContact> items) {
        super(scope);
        this.items = items;
    }

    @Nullable
    @Override
    public UserContact getKey(int position) {
        return items.get(position);
    }

    @Override
    public int getPosition(@NonNull UserContact key) {
        return items.indexOf(key);
    }
}
