package com.amrit.practice.filesbygooglereplica.utils;

import android.graphics.Bitmap;

public class AudioUtil {

    private final String uri;
    private final int size;
    private final String name;
    private final Bitmap bitmap;

    public AudioUtil(String uri, int size, String name, Bitmap bitmap) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.bitmap = bitmap;
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

    public static boolean isAudio(String name){

        String[] ext = {".mp3", ".wav", ".amr", ".flac", ".aac", ".ogg", ".opus"};
        for (String s : ext) if (name.endsWith(s)) return true;
        return false;
    }

}
