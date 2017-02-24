package com.androiditgroup.loclook.v2.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sostrovschi on 2/23/17.
 */

public class Group {

    public Badge badge;
//    public String string;
    // public final List<String> children = new ArrayList<String>();
    public final List<Badge> children = new ArrayList<Badge>();

    // public Group(String string) {
//        this.string = string;
//    }

    public Group(Badge value) {
        this.badge = value;
    }
}
