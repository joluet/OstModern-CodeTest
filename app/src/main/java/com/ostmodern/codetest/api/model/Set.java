package com.ostmodern.codetest.api.model;

import com.google.gson.annotations.SerializedName;

public class Set {
    @SerializedName("uid")
    public final String uid;

    @SerializedName("title")
    public final String title;

    @SerializedName("summary")
    public final String summary;

    @SerializedName("image_urls")
    public final String[] imageUrls;

    @SerializedName("items")
    public final Item[] items;

    public Set(String uid, String title, String summary, String[] imageUrls, Item[] items) {
        this.uid = uid;
        this.title = title;
        this.summary = summary;
        this.imageUrls = imageUrls;
        this.items = items;
    }
}
