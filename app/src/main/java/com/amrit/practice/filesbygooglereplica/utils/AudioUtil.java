package com.amrit.practice.filesbygooglereplica.utils;

import android.graphics.Bitmap;

public class AudioUtil {

    private final String uri;
    private final int size;
    private final String name;
    private final Bitmap bitmap;
    private final long date;
    private final String location;

    public AudioUtil(String uri, int size, String name, Bitmap bitmap, String location, long date) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.bitmap = bitmap;
        this.location = location;
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public Bitmap getBitmap() {
        return bitmap;
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

    public String getLocation() {
        return location;
    }

    public static boolean isAudio(String name){

        String[] ext = {".mp3", ".wav", ".amr", ".flac", ".aac", ".ogg", ".opus"};
        for (String s : ext) if (name.endsWith(s)) return true;
        return false;
    }

}
