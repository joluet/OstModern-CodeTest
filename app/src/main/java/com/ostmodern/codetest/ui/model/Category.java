package com.ostmodern.codetest.ui.model;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

    public final String id;
    public final String title;
    public final String summary;
    public final String imageUrl;
    public final List<String> episodeUrls;
    public final List<Divider> dividers;

    public Category(String id, String title, String summary, String imageUrl, List<String> episodeUrls, List<Divider> dividers) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.episodeUrls = episodeUrls;
        this.dividers = dividers;
    }

    public Category(Category category, String imageUrl) {
        this.id = category.id;
        this.title = category.title;
        this.summary = category.summary;
        this.episodeUrls = category.episodeUrls;
        this.dividers = category.dividers;
        this.imageUrl = imageUrl;
    }

}

