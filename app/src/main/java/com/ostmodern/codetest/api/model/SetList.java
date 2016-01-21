package com.ostmodern.codetest.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SetList {
    @SerializedName("objects")
    public final List<Set> sets;

    public SetList(List<Set> sets) {
        this.sets = sets;
    }
}
