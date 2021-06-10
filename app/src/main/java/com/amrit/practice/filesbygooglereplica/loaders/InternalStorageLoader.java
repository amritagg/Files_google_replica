package com.amrit.practice.filesbygooglereplica.loaders;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import com.amrit.practice.filesbygooglereplica.utils.InternalStorageUtil;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
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

        for(File f: Objects.requireNonNull(file.listFiles())){
            boolean isFolder = true;
            if(!f.isDirectory()) isFolder = false;

            String name = f.getName();
            long size = f.length();
            String uri = f.getAbsolutePath();

            InternalStorageUtil util = new InternalStorageUtil(isFolder, name, uri, size);
            list.add(util);

        }

        list.sort(new Sorting());
        return list;
    }
}


class Sorting implements Comparator<InternalStorageUtil> {

    public int compare(InternalStorageUtil util1, InternalStorageUtil util2){
        if(util1.isFolder() && util2.isFolder()) {
            return util1.getName().compareTo(util2.getName());
        }else if(util1.isFolder() && !util2.isFolder()){
            return -1;
        }else if(!util1.isFolder() && util2.isFolder()){
            return 1;
        }else {
            return util1.getName().compareTo(util2.getName());
        }
    }
}
