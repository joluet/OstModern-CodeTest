package com.ostmodern.codetest.ui.model;

import java.io.Serializable;

public class Divider implements Serializable {
    public final int position;
    public final String label;

    public Divider(int position, String label) {
        this.position = position;
        this.label = label;
    }
}
