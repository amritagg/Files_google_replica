package com.amrit.practice.filesbygooglereplica.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.amrit.practice.filesbygooglereplica.utils.DocumentsUtil;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MediaDocLoader extends AsyncTaskLoader<ArrayList<DocumentsUtil>> {

    private final String LOG_TAG = MediaDocLoader.class.getSimpleName();
    private final Context context;

    // constructor for docloader
    public MediaDocLoader(@NonNull @NotNull Context context) {
        super(context);
        this.context = context;
    }

    // starting the background task
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    // during the background task
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public ArrayList<DocumentsUtil> loadInBackground() {

        ArrayList<DocumentsUtil> list = new ArrayList<>();

        // getting home file from current file
        File file = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(
                context.getExternalFilesDir(null) // /storage/emulated/0/Android/data/com.amrit.practice.filesByGoogleReplica/files
                .getParentFile()) // /storage/emulated/0/Android/data/com.amrit.practice.filesByGoogleReplica
                .getParentFile()) // /storage/emulated/0/Android/data
                .getParentFile()) // /storage/emulated/0/Android
                .getParentFile(); // /storage/emulated/0

        Log.e(LOG_TAG, "Process started");

        assert file != null;
        ArrayList<File> getAllFiles = new ArrayList<>();
        for(File f: Objects.requireNonNull(file.listFiles())){

            // ignroing docs from android folder
            if(!f.getName().equals("Android")){

                // if it is folder then check for all files
                if(f.isDirectory()){
                    getAllFiles.addAll(getDocumentFiles(Objects.requireNonNull(f.listFiles())));
                }else{
                    String name = file.getName();

                    for(String s: docType()){
                        if(name.endsWith(s)) {
                            getAllFiles.add(file);
                            break;
                        }
                    }
                }
            }
        }

        // loading thumbnail for pdf files
        for(File f: getAllFiles){
            if(!f.getAbsolutePath().equals("/storage/emulated/0")){
                String name = f.getName();
                long size = f.length();
                String uri = Uri.parse(f.getAbsolutePath()).toString();

                // creating the file
                File temp = new File(uri);
                ParcelFileDescriptor pdf;
                PdfRenderer pdfRenderer;
                Bitmap bitmap = null;
                try {
                    pdf = ParcelFileDescriptor.open(temp, ParcelFileDescriptor.MODE_READ_ONLY);
                    pdfRenderer = new PdfRenderer(pdf);

                    PdfRenderer.Page page = pdfRenderer.openPage(0);

                    // getting bitmap
                    bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);

                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    page.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // adding details in list
                DocumentsUtil documentsUtil = new DocumentsUtil(uri, size, name, bitmap);
                list.add(documentsUtil);
            }
        }

        Log.e(LOG_TAG, list.size() + "");

        return list;
    }

    private ArrayList<File> getDocumentFiles(File[] listFiles) {
        ArrayList<File> result = new ArrayList<>();

        for(File file: listFiles){
            if(file.isDirectory() && !file.isHidden()){
//                if(!file.getAbsolutePath().contains("/storage/emulated/0/WhatsApp/Media") && !file.getAbsolutePath().contains("Sent")){
                    getDocumentFiles(Objects.requireNonNull(file.listFiles()));
                    result.addAll(getDocumentFiles(Objects.requireNonNull(file.listFiles())));
//                }

            }else {
                String name = file.getName();

                for(String s: docType()){
                    if(name.endsWith(s)) {
                        result.add(file);
                        break;
                    }
                }
            }
        }

        return result;
    }

    private static String[] docType(){
        return new String[]{
                ".pdf"
                /*, ".docx", ".odt", ".rtf", ".txt", ".html", ".epub",
                ".xlsx", ".ods", ".tsv", ".csv",
                ".pptx", ".odp"*/
        };
    }

}
