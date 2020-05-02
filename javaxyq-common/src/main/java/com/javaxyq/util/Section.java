package com.javaxyq.util;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private int start;

    private int end;

    private List<ColorationScheme> schemes;

    public Section(int start, int end) {
        this.start = start;
        this.end = end;
        schemes = new ArrayList<ColorationScheme>();
    }

    public void addScheme(ColorationScheme scheme) {
        this.schemes.add(scheme);
    }

    public void setScheme(int index, ColorationScheme scheme) {
        this.schemes.set(index, scheme);
    }

    public ColorationScheme getScheme(int index) {
        return this.schemes.get(index);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSchemeCount() {
        return this.schemes.size();
    }

}
