package com.ostmodern.codetest.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ostmodern.codetest.R;
import com.ostmodern.codetest.ui.model.Divider;
import com.ostmodern.codetest.ui.model.Episode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EpisodeAdapter extends RecyclerView.Adapter {

    public static final int TYPE_DIVIDER = 0;
    public static final int TYPE_EPISODE = 1;
    private final Context appContext;
    private List<Episode> episodeList = new LinkedList<>();
    private OnEpisodeClickListener onEpisodeClickListener;
    private Map<Integer, Divider> dividerMap = new HashMap<>();

    public EpisodeAdapter(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public int getItemViewType(int position) {
        if (dividerMap.containsKey(position)) {
            return TYPE_DIVIDER;
        } else {
            return TYPE_EPISODE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_DIVIDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_divider, parent, false);
                return new DividerViewHolder(view);
            case TYPE_EPISODE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_episode, parent, false);
                return new EpisodeViewHolder(view);
            default:
                throw new RuntimeException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (dividerMap.containsKey(position)) {
            final Divider divider = dividerMap.get(position);
            DividerViewHolder dividerViewHolder = (DividerViewHolder) holder;
            dividerViewHolder.tvlabel.setText(divider.label);
        } else {
            final Episode episode = episodeList.get(position);
            EpisodeViewHolder episodeViewHolder = (EpisodeViewHolder) holder;
            episodeViewHolder.tvTitle.setText(episode.title);
            episodeViewHolder.itemView.setOnClickListener(v -> {
                if (onEpisodeClickListener != null) {
                    onEpisodeClickListener.onClick(episode);
                }
            });
        }
    }

    public void setEpisodes(List<Episode> categories) {
        episodeList.addAll(categories);
        notifyItemRangeInserted(0, episodeList.size());
    }

    public void setDividers(List<Divider> dividers) {
        for (Divider divider : dividers) {
            episodeList.add(divider.position, null);
            dividerMap.put(divider.position, divider);
            notifyItemInserted(divider.position);
        }
    }

    public void setOnEpisodeClickListener(OnEpisodeClickListener onEpisodeClickListener) {
        this.onEpisodeClickListener = onEpisodeClickListener;
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public interface OnEpisodeClickListener {
        void onClick(Episode episode);
    }

    private class EpisodeViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvTitle;

        public EpisodeViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    private class DividerViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvlabel;

        public DividerViewHolder(View itemView) {
            super(itemView);
            tvlabel = (TextView) itemView.findViewById(R.id.tv_label);
        }
    }
}
