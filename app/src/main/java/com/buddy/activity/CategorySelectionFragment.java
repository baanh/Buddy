package com.buddy.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.buddy.adapters.CategoryListAdapter;
import com.buddy.entity.Category;
import com.buddy.main.R;
import com.buddy.viewmodel.CategoryViewModel;

import java.util.List;

public class CategorySelectionFragment extends DialogFragment {
    private RecyclerView recycleView;
    private CategoryViewModel categoryViewModel;
    private Button btnNewCategory;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Get view
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_category_selection, null);

        recycleView = (RecyclerView) view.findViewById(R.id.category_list);
        final CategoryListAdapter mAdapter = new CategoryListAdapter(getActivity());
        recycleView.setAdapter(mAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                mAdapter.setCategoryList(categories);
            }
        });

        btnNewCategory = (Button) view.findViewById(R.id.btn_new_category);
        btnNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // return a dialog object
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Select Category")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }
}
