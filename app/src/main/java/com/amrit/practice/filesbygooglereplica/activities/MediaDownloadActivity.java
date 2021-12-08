package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class MediaDownloadActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<DownloadUtils>> {

    private final String LOG_TAG = MediaDownloadActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private static final int LoaderManger_ID = 30;
    private MediaDownloadAdapter downloadAdapter;
    private ArrayList<DownloadUtils> downloadUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        initialiseRecyclerView();
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
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG, "started onLoadFinished");
        return new MediaDownloadLoader(getApplicationContext());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<DownloadUtils>> loader, ArrayList<DownloadUtils> data) {

        Log.e(LOG_TAG, "Done onLoadFinished");
        progressBar.setVisibility(View.GONE);
        downloadUtils.addAll(data);
        recyclerView.setVisibility(View.VISIBLE);
        downloadAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<DownloadUtils>> loader) { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.sort_by);
        menuItem.setVisible(false);
        return true;
    }

//    @SuppressLint("UseCompatLoadingForDrawables")
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.list_grid){
//            isList = !isList;
//            downloadAdapter = new MediaDownloadAdapter(getApplicationContext(), downloadUtils, isList);
//            if(isList) {
//                item.setIcon(getDrawable(R.drawable.ic_baseline_view_grid_24));
//                listView.setAdapter(downloadAdapter);
//                listView.setVisibility(View.VISIBLE);
//                gridView.setVisibility(View.GONE);
//            } else {
//                item.setIcon(getDrawable(R.drawable.ic_baseline_view_list_24));
//                gridView.setAdapter(downloadAdapter);
//                listView.setVisibility(View.GONE);
//                gridView.setVisibility(View.VISIBLE);
//            }
//            return true;
//        }else return super.onOptionsItemSelected(item);
//    }

    private void initialiseRecyclerView(){
        progressBar = findViewById(R.id.media_progress_bar);
        recyclerView = findViewById(R.id.media_recycler_view);
        recyclerView.setVisibility(View.GONE);
        downloadUtils = new ArrayList<>();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        downloadAdapter = new MediaDownloadAdapter(this, downloadUtils);
        recyclerView.setAdapter(downloadAdapter);
    }

}