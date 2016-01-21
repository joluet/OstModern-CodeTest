package com.ostmodern.codetest.api.model;


import com.google.gson.annotations.SerializedName;

public class EpisodeItem {
    @SerializedName("title")
    public final String title;

    public EpisodeItem(String title) {
        this.title = title;
    }
}
