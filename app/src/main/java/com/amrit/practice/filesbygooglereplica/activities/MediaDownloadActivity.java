package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.amrit.practice.filesbygooglereplica.utils.DownloadUtils;
import com.amrit.practice.filesbygooglereplica.adapters.MediaDownloadAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaDownloadLoader;
import com.amrit.practice.filesbygooglereplica.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaDownloadActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<DownloadUtils>> {

    private final String LOG_TAG = MediaDownloadActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private GridView gridView;
    private ListView listView;
    private static final int LoaderManger_ID = 30;
    private static final boolean isList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        progressBar = findViewById(R.id.media_progress_bar);
        gridView = findViewById(R.id.media_grid_view);
        listView = findViewById(R.id.media_list_view);

        showDownloads();

    }

    private void showDownloads(){
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LoaderManger_ID, null, this);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<DownloadUtils>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        gridView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG, "started onLoadFinished");
        return new MediaDownloadLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<DownloadUtils>> loader, ArrayList<DownloadUtils> data) {

        progressBar.setVisibility(View.GONE);
        Log.e(LOG_TAG, "Done onLoadFinished");
        MediaDownloadAdapter downloadAdapter = new MediaDownloadAdapter(getApplicationContext(), data, isList);

        if(isList){
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(downloadAdapter);
        }else {
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(downloadAdapter);
        }
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<DownloadUtils>> loader) {

    }
}