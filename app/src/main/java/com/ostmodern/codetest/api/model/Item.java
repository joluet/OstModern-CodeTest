package com.ostmodern.codetest.api.model;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("content_type")
    public final String type;

    @SerializedName("content_url")
    public final String url;

    @SerializedName("heading")
    public final String heading;

    public Item(String type, String url, String heading) {
        this.type = type;
        this.url = url;
        this.heading = heading;
    }
}
