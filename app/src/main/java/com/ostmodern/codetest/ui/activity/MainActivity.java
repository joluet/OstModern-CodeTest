package com.ostmodern.codetest.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.ostmodern.codetest.R;
import com.ostmodern.codetest.api.ApiAdapter;
import com.ostmodern.codetest.api.ApiService;
import com.ostmodern.codetest.ui.adapter.CategoryAdapter;
import com.ostmodern.codetest.ui.model.Category;
import com.ostmodern.codetest.ui.model.CategoryMapper;
import com.ostmodern.codetest.ui.utils.RxUtils;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import rx.Observable;

public class MainActivity extends RxAppCompatActivity {

    private ApiService apiAdapter;
    private CategoryAdapter categoryAdapter;
    private ProgressBar progressBar;
    private RecyclerView rvCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create REST adapter
        apiAdapter = ApiAdapter.build();

        // Get view references
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        rvCategories = (RecyclerView) findViewById(R.id.rv_categories);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Setup RecyclerView
        rvCategories.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(getApplicationContext());
        rvCategories.setAdapter(categoryAdapter);
        categoryAdapter.setOnCategoryClickListener(category -> EpisodesActivity.start(this, category));

        loadCategories();
    }

    private void loadCategories() {
        apiAdapter.getSets()
                .compose(CategoryMapper.mapSetToCategory())
                .compose(RxLifecycle.bindUntilActivityEvent(lifecycle(), ActivityEvent.PAUSE))
                .flatMap(Observable::from)
                .flatMap(category -> {
                    if (!category.imageUrl.isEmpty()) {
                        return apiAdapter.getImageUrl(category.imageUrl)
                                .map(image -> new Category(category, image.url));
                    } else {
                        return Observable.just(category);
                    }
                })
                .toList()
                .compose(RxUtils.<List<Category>>applySchedulers())
                .doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
                .doOnCompleted(() -> progressBar.setVisibility(View.GONE))
                .doOnError(throwable -> progressBar.setVisibility(View.GONE))
                .subscribe(categoryAdapter::setCategories,
                        throwable -> {
                            Snackbar.make(rvCategories, R.string.error_loading_content, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.action_retry, v -> loadCategories())
                                    .show();
                        });
    }
}
