package com.buddy.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.buddy.entity.Category;
import com.buddy.main.R;
import com.buddy.viewmodel.CategoryViewModel;

public class CategoryNewEditActivity extends AppCompatActivity {
    private CategoryViewModel categoryViewModel;
    private EditText categoryName;
    private Button btnSaveCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_new_edit);

        categoryName = (EditText) findViewById(R.id.edit_category_name);
        btnSaveCategory = (Button) findViewById(R.id.btn_save_category);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        btnSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category(categoryName.getText().toString());
                categoryViewModel.insert(category);
                finish();
            }
        });
    }
}
