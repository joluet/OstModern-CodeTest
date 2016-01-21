package com.ostmodern.codetest.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ostmodern.codetest.R;
import com.ostmodern.codetest.ui.model.Category;

import java.util.LinkedList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter {

    private final Context appContext;
    private List<Category> categoryList = new LinkedList<>();
    private OnCategoryClickListener onCategoryClickListener;

    public CategoryAdapter(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Category category = categoryList.get(position);
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        categoryViewHolder.tvTitle.setText(category.title);
        Glide.with(appContext)
                .load(category.imageUrl)
                .centerCrop()
                .error(ContextCompat.getDrawable(appContext, R.drawable.placeholder))
                .into(categoryViewHolder.ivImage);
        categoryViewHolder.itemView.setOnClickListener(v -> {
            if (onCategoryClickListener != null) {
                onCategoryClickListener.onClick(category);
            }
        });
    }

    public void setCategories(List<Category> categories) {
        categoryList.addAll(categories);
        notifyItemRangeInserted(0, categoryList.size());
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnCategoryClickListener {
        void onClick(Category category);
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder {
        public final ImageView ivImage;
        public final TextView tvTitle;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
