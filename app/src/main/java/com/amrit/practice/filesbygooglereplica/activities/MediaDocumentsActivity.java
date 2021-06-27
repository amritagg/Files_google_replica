package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private ArrayList<DocumentsUtil> documentsUtils;
    private static boolean isList = false;
    private MediaDocAdapter mediaDocAdapter;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        progressBar = findViewById(R.id.media_progress_bar);
        gridView = findViewById(R.id.media_grid_view);
        listView = findViewById(R.id.media_list_view);
        showDocuments();

        gridView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemClickListener(onItemClickListener);
    }

    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Intent intent = new Intent(MediaDocumentsActivity.this, ShowPdfActivity.class);
            intent.putExtra("pdf_uri", documentsUtils.get(position).getUri());
            startActivity(intent);
        }
    };

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

        progressBar.setVisibility(View.GONE);
        Log.e(LOG_TAG, "Done onLoadFinished");
        mediaDocAdapter = new MediaDocAdapter(getApplicationContext(), data, isList);
        documentsUtils = data;

        if(isList){
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(mediaDocAdapter);
        }else {
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(mediaDocAdapter);
        }

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<DocumentsUtil>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.sort_by);
        menuItem.setVisible(false);
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.list_grid){
            isList = !isList;
            mediaDocAdapter = new MediaDocAdapter(getApplicationContext(), documentsUtils, isList);
            if(isList) {
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_grid_24));
                listView.setAdapter(mediaDocAdapter);
                listView.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            } else {
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_list_24));
                gridView.setAdapter(mediaDocAdapter);
                listView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            }
            return true;
        }else return super.onOptionsItemSelected(item);
    }
}