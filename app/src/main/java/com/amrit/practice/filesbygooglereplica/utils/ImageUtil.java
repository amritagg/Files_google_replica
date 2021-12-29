package com.amrit.practice.filesbygooglereplica.utils;

public class ImageUtil {

    //  Required Params for the Image Files
    private String uri;
    private int size;
    private String name;
    private long date;
    private final String path;

    // Constructor for the same
    public ImageUtil(String uri, int size, String name, long date, String location) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.date = date;
        this.path = location;
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

    public String getPath() {
        return path;
    }

    public static boolean isImage(String name){
        String[] ext = {".jpg", ".png", ".gif", ".bmp", ".webp", ".heic", ".heif"};
        for (String s : ext) if (name.endsWith(s)) return true;
        return false;
    }

}
