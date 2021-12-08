package com.amrit.practice.filesbygooglereplica.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PdfLoader extends AsyncTaskLoader<ArrayList<Bitmap>> {

    private final String uri;

    // constructor for pdfloader
    public PdfLoader(@NonNull @NotNull Context context, String uri) {
        super(context);
        this.uri = uri;
    }

    // starting the background task
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    // background process
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public ArrayList<Bitmap> loadInBackground() {

        ArrayList<Bitmap> arrayList = new ArrayList<>();

        // getting file
        File file = new File(uri);
        ParcelFileDescriptor pdf;
        PdfRenderer pdfRenderer;
        try {
            pdf = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(pdf);

            final int pageCount = pdfRenderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = pdfRenderer.openPage(i);

                Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                // we render for showing on the screen
                page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                // adding bitmap in arraylist
                arrayList.add(mBitmap);

                // close the page
                page.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            arrayList = null;
        }

        // return arraylist
        return arrayList;
    }
}
