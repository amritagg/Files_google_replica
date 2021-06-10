package com.amrit.practice.filesbygooglereplica.utils;

public class InternalStorageUtil {

    private final boolean isFolder;
    private final String name;
    private final String uri;
    private final long size;

    public InternalStorageUtil(boolean isFolder, String name, String uri, long size) {
        this.isFolder = isFolder;
        this.name = name;
        this.uri = uri;
        this.size = size;
    }

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
