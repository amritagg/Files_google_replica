package com.amrit.practice.filesbygooglereplica.utils;

public class DownloadUtils {

    //  Required Params for the Download Files
    private String uri;
    private int size;
    private String name;
    private long date;
    private final String location;

    // Constructor for the same
    public DownloadUtils(String uri, int size, String name, long date, String location) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.date = date;
        this.location = location;
    }

    // Getters to get the values
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

}
