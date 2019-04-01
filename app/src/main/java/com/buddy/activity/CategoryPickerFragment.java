package com.buddy.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buddy.entity.Category;
import com.buddy.main.R;
import com.buddy.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategorySelectorFragment extends DialogFragment {
    private Category selectCategory = null;
    private List<Category> allCategories;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        CategoryViewModel categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        allCategories = categoryViewModel.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        // Get names of categories
        for (Category cat : allCategories) {
            categoryNames.add(cat.getName());
        }

        // Build a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Category");
        builder.setItems(categoryNames.toArray(new String[0]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setSelectCategory(allCategories.get(which));
                TextView txtCategory = getActivity().findViewById(R.id.txt_category);
                txtCategory.setText(selectCategory.getName());
            }
        });
        builder.setNegativeButton("New Category", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getActivity(), CategoryNewEditActivity.class);
                startActivity(intent);
            }
        });

        return builder.create();
    }

    public Category getSelectCategory() {
        return selectCategory;
    }

    public void setSelectCategory(Category selectCategory) {
        this.selectCategory = selectCategory;
    }
}
