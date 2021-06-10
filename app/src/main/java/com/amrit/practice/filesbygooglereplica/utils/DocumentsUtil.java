package com.amrit.practice.filesbygooglereplica.utils;

public class DocumentsUtil {

    private String uri;
    private long size;
    private String name;

    public DocumentsUtil(String uri, long size, String name) {
        this.uri = uri;
        this.size = size;
        this.name = name;
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
