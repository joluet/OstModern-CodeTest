package com.ostmodern.codetest.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ostmodern.codetest.R;
import com.ostmodern.codetest.api.ApiAdapter;
import com.ostmodern.codetest.api.ApiService;
import com.ostmodern.codetest.ui.adapter.EpisodeAdapter;
import com.ostmodern.codetest.ui.model.Category;
import com.ostmodern.codetest.ui.model.Episode;
import com.ostmodern.codetest.ui.utils.RxUtils;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import rx.Observable;

public class EpisodesActivity extends RxAppCompatActivity {
    public static final String ARG_CATEGORY = "arg_category";

    private ApiService apiAdapter;
    private EpisodeAdapter episodeAdapter;
    private ProgressBar progressBar;
    private RecyclerView rvEpisodes;

    public static void start(Context context, Category category) {
        final Intent activityIntent = new Intent(context, EpisodesActivity.class);
        activityIntent.putExtra(ARG_CATEGORY, category);
        context.startActivity(activityIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        apiAdapter = ApiAdapter.build();

        final Category category = (Category) getIntent().getSerializableExtra(ARG_CATEGORY);

        initUI(category);
        loadAndShowEpisodes(category);
    }

    private void initUI(Category category) {
        // Get view references
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView ivCategory = (ImageView) findViewById(R.id.iv_category);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        final TextView tvSummary = (TextView) findViewById(R.id.tv_summary);

        // Set title
        toolbar.setTitle(category.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set summary if available
        if (category.summary.isEmpty()) {
            tvSummary.setVisibility(View.GONE);
        } else {
            tvSummary.setText(category.summary);
        }

        // Setup recyclerview
        rvEpisodes = (RecyclerView) findViewById(R.id.rv_episodes);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvEpisodes.setLayoutManager(layoutManager);
        episodeAdapter = new EpisodeAdapter(getApplicationContext());
        rvEpisodes.setAdapter(episodeAdapter);
        episodeAdapter.setOnEpisodeClickListener(v ->
                Snackbar.make(rvEpisodes, R.string.play_video, Snackbar.LENGTH_SHORT).show());

        // Load category image
        Glide.with(this)
                .load(category.imageUrl)
                .placeholder(ContextCompat.getDrawable(this, R.drawable.placeholder))
                .centerCrop()
                .into(ivCategory);
    }

    private void loadAndShowEpisodes(Category category) {
        Observable.from(category.episodeUrls)
                .flatMap(itemUrl -> apiAdapter.getEpisode(itemUrl))
                .map(episode -> new Episode(episode.title))
                .toList()
                .filter(episodes -> !episodes.isEmpty())
                .compose(RxUtils.<List<Episode>>applySchedulers())
                .compose(RxLifecycle.bindUntilActivityEvent(lifecycle(), ActivityEvent.PAUSE))
                .doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
                .doOnCompleted(() -> progressBar.setVisibility(View.GONE))
                .doOnError(throwable -> progressBar.setVisibility(View.GONE))
                .subscribe(episodeItems -> {
                    episodeAdapter.setEpisodes(episodeItems);
                    episodeAdapter.setDividers(category.dividers);
                }, throwable -> {
                    Snackbar.make(rvEpisodes, R.string.error_loading_episodes, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.action_retry, v -> loadAndShowEpisodes(category))
                            .show();
                });
    }
}
