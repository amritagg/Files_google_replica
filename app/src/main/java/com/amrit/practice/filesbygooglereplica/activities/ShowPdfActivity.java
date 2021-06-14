package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.adapters.PdfAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.PdfLoader;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class ShowPdfActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Bitmap>> {

    private static final int LoaderManger_ID = 40;
    private ProgressBar progressBar;
    private String uri;
    private ListView listView;
    private static final String LOG_TAG = ShowPdfActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);

        Intent intent = getIntent();
        uri = intent.getStringExtra("pdf_uri");

        progressBar = findViewById(R.id.progress_bar_pdf_activity);
        listView = findViewById(R.id.list_view_pdf);

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LoaderManger_ID, null, this);

    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<Bitmap>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        return new PdfLoader(this, uri);
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<Bitmap>> loader, ArrayList<Bitmap> data) {
        PdfAdapter pdfAdapter = new PdfAdapter(this, data);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        listView.setAdapter(pdfAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<Bitmap>> loader) { }

}