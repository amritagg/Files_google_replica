package com.amrit.practice.filesbygooglereplica.utils;

public class VideoUtil {

    private final String uri;
    private final int size;
    private final String name;
    private final long date;
    private final String location;

    public VideoUtil(String uri, int size, String name, long date, String location) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.date = date;
        this.location = location;
    }

    public long getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getUri() {
        return uri;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public static boolean isVideo(String name){

        String[] ext = {".3gp", ".mp4", ".mkv", ".webm"};
        for (String s : ext) if (name.endsWith(s)) return true;
        return false;
    }

}
