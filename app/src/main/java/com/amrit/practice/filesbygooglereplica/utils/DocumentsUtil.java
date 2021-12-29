package com.amrit.practice.filesbygooglereplica.utils;

import android.graphics.Bitmap;

public class DocumentsUtil {

    //  Required Params for the Document Files
    private String uri;
    private int size;
    private long date;
    private String name;
    private final Bitmap bitmap;
    private String location;

    // Constructor for the same
    public DocumentsUtil(String uri, int size, String name, Bitmap bitmap, long date, String location) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.bitmap = bitmap;
        this.date = date;
        this.location = location;
    }

    // Getters to get the values
    public Bitmap getBitmap(){
        return bitmap;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
