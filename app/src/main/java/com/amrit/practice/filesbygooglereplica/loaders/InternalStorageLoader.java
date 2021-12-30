package com.amrit.practice.filesbygooglereplica.loaders;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import com.amrit.practice.filesbygooglereplica.Models.InternalStorageUtil;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class InternalStorageLoader extends AsyncTaskLoader<ArrayList<InternalStorageUtil>> {

    private final File file;

    public InternalStorageLoader(@NonNull @NotNull Context context, File file) {
        super(context);
        this.file = file;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public ArrayList<InternalStorageUtil> loadInBackground() {
        ArrayList<InternalStorageUtil> list = new ArrayList<>();

        if(file.getAbsolutePath().equals("/storage/emulated/0/Android/data") || file.getAbsolutePath().equals("/storage/emulated/0/Android/obb")) return list;
        for(File f: Objects.requireNonNull(file.listFiles())){
            boolean isFolder = true;
            if(!f.isDirectory()) isFolder = false;

            String name = f.getName();
            String path = f.getAbsolutePath();
            long size = 0;
            try {
                size = Files.size(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            long date = 0;
            try {
                BasicFileAttributes attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
                date = attr.lastModifiedTime().toMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String uri = f.getAbsolutePath();

            InternalStorageUtil util = new InternalStorageUtil(isFolder, name, uri, size, date);
            list.add(util);

        }

        list.sort((util1, util2) -> {
            if(util1.isFolder() && util2.isFolder()) {
                return util1.getName().compareTo(util2.getName());
            }else if(util1.isFolder() && !util2.isFolder()){
                return -1;
            }else if(!util1.isFolder() && util2.isFolder()){
                return 1;
            }else {
                return util1.getName().compareTo(util2.getName());
            }
        });
        return list;
    }

}
