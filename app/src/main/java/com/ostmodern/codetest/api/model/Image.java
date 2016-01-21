package com.ostmodern.codetest.api.model;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("url")
    public final String url;

    public Image(String url) {
        this.url = url;
    }
}
