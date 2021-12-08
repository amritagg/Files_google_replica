package com.amrit.practice.filesbygooglereplica.utils;

public class DownloadUtils {

    //  Required Params for the Download Files
    private String uri;
    private long size;
    private String name;
    private String date;
    private final String location;

    // Constructor for the same
    public DownloadUtils(String uri, long size, String name, String date, String location) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
