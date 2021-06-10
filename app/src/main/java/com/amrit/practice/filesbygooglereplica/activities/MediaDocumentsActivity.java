package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.amrit.practice.filesbygooglereplica.utils.DocumentsUtil;
import com.amrit.practice.filesbygooglereplica.adapters.MediaDocAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaDocLoader;
import com.amrit.practice.filesbygooglereplica.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MediaDocumentsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<DocumentsUtil>> {

    private final String LOG_TAG = MediaDocumentsActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private GridView gridView;
    private ListView listView;
    private static final int LoaderManger_ID = 25;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_documents);

        gridView = findViewById(R.id.grid_view_doc);
        listView = findViewById(R.id.list_view_doc);
        progressBar = findViewById(R.id.media_doc_progress_bar);
        showDocuments();

//        gridView.setOnItemClickListener(onItemClickListener);
//        listView.setOnItemClickListener(onItemClickListener);
    }

//    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//            openFile(Uri.parse(documentsUtils.get(position).getUri()));
//        }
//    };

//    private void openFile(Uri pickerInitialUri) {
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(pickerInitialUri, "application/pdf");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);
//
//        Intent objIntent = new Intent(Intent.ACTION_VIEW);
//        objIntent.setDataAndType(pickerInitialUri, "application/pdf");
//        objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(objIntent);
//
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_VIEW);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, pickerInitialUri);
//        shareIntent.setType("application/pdf");
//        startActivity(Intent.createChooser(shareIntent, "send to"));
//
//        Intent target = new Intent(Intent.ACTION_VIEW);
//        target.setDataAndType(pickerInitialUri,"application/pdf");
//        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//        Intent intent = Intent.createChooser(target, "Open File");
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "No app found", Toast.LENGTH_SHORT).show();
//        }
//
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/pdf");
//
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
//
//        documentOpenerActivity.launch(intent);
//        startActivityForResult(intent, PICK_PDF_FILE);
//  }

//    ActivityResultLauncher<Intent> documentOpenerActivity = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() != RESULT_OK) {
//                    Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show();
//                }
//            }
//    );


    private void showDocuments(){
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LoaderManger_ID, null, this);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<DocumentsUtil>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        gridView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG, "started onLoadFinished");
        return new MediaDocLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<DocumentsUtil>> loader, ArrayList<DocumentsUtil> data) {
//        gridView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        Log.e(LOG_TAG, "Done onLoadFinished");
        MediaDocAdapter mediaDocAdapter = new MediaDocAdapter(getApplicationContext(), data);
//        gridView.setAdapter(mediaDocAdapter);
        listView.setAdapter(mediaDocAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<DocumentsUtil>> loader) {

    }
}