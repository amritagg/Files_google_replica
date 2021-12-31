package com.amrit.practice.filesbygooglereplica.loaders;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.amrit.practice.filesbygooglereplica.models.DocumentsUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MediaDocLoader extends AsyncTaskLoader<ArrayList<DocumentsUtil>> {

    private final String LOG_TAG = MediaDocLoader.class.getSimpleName();
    private final Context context;

    // constructor for docLoader
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

//        ArrayList<DocumentsUtil> list = new ArrayList<>();
//
//        // getting home file from current file
//        File file = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(
//                context.getExternalFilesDir(null) // /storage/emulated/0/Android/data/com.amrit.practice.filesByGoogleReplica/files
//                .getParentFile()) // /storage/emulated/0/Android/data/com.amrit.practice.filesByGoogleReplica
//                .getParentFile()) // /storage/emulated/0/Android/data
//                .getParentFile()) // /storage/emulated/0/Android
//                .getParentFile(); // /storage/emulated/0
//
//        Log.e(LOG_TAG, "Process started");
//
//        Queue<File> q = new LinkedList<>();
//        q.add(file);
//        while (!q.isEmpty()) {
//            File listFiles = q.poll();
//            assert listFiles != null;
//            if(listFiles.isHidden()) continue;
//            if(listFiles.getAbsolutePath().equals("/storage/emulated/0/Android/data")
//                    || listFiles.getAbsolutePath().equals("/storage/emulated/0/Android/obb")) continue;
//            for (File f : Objects.requireNonNull(listFiles.listFiles())) {
//                if (f.isDirectory()) {
//                    q.add(f);
//                    continue;
//                }
//                String name = f.getName();
//                for (String s : docType()) {
//                    if (name.endsWith(s)) {
//                        String path = f.getAbsolutePath();
//                        int size = 0;
//                        try {
//                            size = (int) Files.size(Paths.get(path));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        String contentUri = Uri.parse(new File(path).toString()).toString();
//                        String date = "";
//                        try {
//                            BasicFileAttributes attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
//                            date = attr.lastModifiedTime().toString();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        DocumentsUtil documentsUtil = new DocumentsUtil(contentUri, size, name, bitmap, date);
//                        list.add(documentsUtil);
//                        break;
//                    }
//                }
//
//            }
//        }
//

        ArrayList<DocumentsUtil> list = new ArrayList<>();

        final String[] projection = new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.RELATIVE_PATH
        };

        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";

        String mime = MediaStore.Files.FileColumns.MIME_TYPE;
        final String selection = mime + " = ? OR " + mime + " = ? OR " + mime + " = ? ";

        final String pdfType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        final String docType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
        final String xlsxType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
        final String[] selectionArgs = new String[]{pdfType, docType, xlsxType};

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                projection,
                selection,
                selectionArgs,
                sortOrder)) {
            assert cursor != null;
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
            int sizeColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
            int titleColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
            int locationColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH);
            int dateColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumnIndex);
                int size = cursor.getInt(sizeColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id);
                String location = cursor.getString(locationColumnIndex);
                long date = cursor.getLong(dateColumnIndex);

                ParcelFileDescriptor pdf;
                PdfRenderer pdfRenderer;
                Bitmap bitmap = null;
                try {
                    File file = new File( "/storage/emulated/0/" + location + "/" + title);
                    Log.e(LOG_TAG, file.getAbsolutePath());
                    pdf = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                    pdfRenderer = new PdfRenderer(pdf);

                    PdfRenderer.Page page = pdfRenderer.openPage(0);

                    // getting bitmap
                    bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);

                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    page.close();

                } catch (IOException e) {
                    Log.e(LOG_TAG, e.toString());
                    e.printStackTrace();
                }
                list.add(new DocumentsUtil(contentUri.toString(), size, title, bitmap, date, location));

            }
        }

        return list;
    }

}
