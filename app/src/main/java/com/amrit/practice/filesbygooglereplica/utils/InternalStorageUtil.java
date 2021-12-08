package com.amrit.practice.filesbygooglereplica.utils;

public class InternalStorageUtil {

    //  Required Params for the InternalStorage Files
    private final boolean isFolder;
    private final String name;
    private final String uri;
    private final long size;

    // Constructor the same
    public InternalStorageUtil(boolean isFolder, String name, String uri, long size) {
        this.isFolder = isFolder;
        this.name = name;
        this.uri = uri;
        this.size = size;
    }

    // Getters to get the values
    public boolean isFolder() {
        return isFolder;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public long getSize() {
        return size;
    }
}
