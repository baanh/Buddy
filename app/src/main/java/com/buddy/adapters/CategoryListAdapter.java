package com.buddy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buddy.entity.Category;
import com.buddy.main.R;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    private final LayoutInflater mInflater;
    private List<Category> categoryList;
    private Category selectedItem;

    public CategoryListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public Category getSelectedItem() {
        if (selectedItem == null) {
            selectedItem = categoryList.get(0);
        }
        return selectedItem;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide all access to all the views for a data item in a view holder
    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
        }
    }

    @NonNull
    @Override
    public CategoryListAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.category_list_row, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder viewHolder, final int position) {
        if (categoryList != null) {
            final Category category = categoryList.get(position);
            viewHolder.categoryName.setText(category.getName());
            viewHolder.categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItem = category;
                    viewHolder.categoryName.setBackgroundColor(Color.parseColor("#C0C0C0"));
                }
            });
        } else {
            viewHolder.categoryName.setText("No Category found");
        }
    }

    @Override
    public int getItemCount() {
        if (categoryList == null) {
            return 0;
        }
        return categoryList.size();
    }
}
