package com.amrit.practice.filesbygooglereplica.utils;

import android.graphics.Bitmap;

public class DocumentsUtil {

    //  Required Params for the Document Files
    private String uri;
    private long size;
    private String name;
    private final Bitmap bitmap;

    // Constructor for the same
    public DocumentsUtil(String uri, long size, String name, Bitmap bitmap) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.bitmap = bitmap;
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

    public long getSize() {
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

}
