package com.ostmodern.codetest.api;

import com.ostmodern.codetest.api.model.EpisodeItem;
import com.ostmodern.codetest.api.model.Image;
import com.ostmodern.codetest.api.model.SetList;

import retrofit.http.GET;
import retrofit.http.Url;
import rx.Observable;

public interface ApiService {

    @GET("/api/sets/")
    Observable<SetList> getSets();

    @GET
    Observable<Image> getImageUrl(@Url String imageUrl);

    @GET
    Observable<EpisodeItem> getEpisode(@Url String episodeUrl);


}