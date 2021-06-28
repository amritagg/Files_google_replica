package com.amrit.practice.filesbygooglereplica.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.adapters.ShowImageViewPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class ShowImageActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowImageActivity.class.getSimpleName();
    ViewPager2 viewPager;
    ArrayList<String> image_uris, image_names, image_location;
    ArrayList<Integer> image_size;
    long[] image_date;
    Button share, delete, info;
    Intent intent;
    private int current;
    private ShowImageViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        share = findViewById(R.id.image_share);
        delete = findViewById(R.id.image_delete);
        info = findViewById(R.id.image_info);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        intent = getIntent();
        viewPager = findViewById(R.id.show_image_view_pager);

        if(intent.hasExtra("bundle")){
            Bundle bundle = intent.getBundleExtra("bundle");

            assert bundle != null;
            current = bundle.getInt("current_position");
            image_uris = bundle.getStringArrayList("image_uris");
            image_date = bundle.getLongArray("image_date");
            image_names = bundle.getStringArrayList("image_name");
            image_location = bundle.getStringArrayList("image_location");
            image_size = bundle.getIntegerArrayList("image_size");

            share.setOnClickListener(view -> shareImage());
            delete.setOnClickListener(view -> deleteImage());
            info.setOnClickListener(view -> infoImage());

            adapter = new ShowImageViewPagerAdapter(this, image_uris, true);

        }else if(intent.hasExtra("from_internal")){
            Bundle bundle = intent.getBundleExtra("from_internal");
            image_uris = bundle.getStringArrayList("uris");
            current = bundle.getInt("current_position");
            adapter = new ShowImageViewPagerAdapter(this, image_uris, false);
        }

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(100));
        viewPager.setPageTransformer(transformer);

        viewPager.setCurrentItem(current);

    }

    private void infoImage() {
        int n = viewPager.getCurrentItem();
        Intent intent = new Intent(this, InfoActivity.class);
        Bundle bundle = new Bundle();

        Date date = new Date(image_date[n]);
        bundle.putString("uri", image_uris.get(n));
        bundle.putString("name", image_names.get(n));
        bundle.putString("location", image_location.get(n));
        bundle.putString("time", date.toString());
        bundle.putString("size", getSize(image_size.get(n)));
        intent.putExtra("INFO", bundle);
        startActivity(intent);
    }

    @NotNull
    private String getSize(int size){
        float sizeFloat = (float) size / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);

        if(sizeFloat < 1024) return sizeFloat + "KB";

        sizeFloat = sizeFloat / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);
        return sizeFloat + "MB";
    }

    private void deleteImage() {

        int currentID = viewPager.getCurrentItem();
        String url_string = image_uris.get(currentID);
        final Uri uri = Uri.parse(url_string);

        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Do You really want to delete the image")
                .setPositiveButton("YES!!",
                        (dialog, which) -> Toast.makeText(getApplicationContext(), "The file " + uri + " is deleted", Toast.LENGTH_SHORT).show())
                .setNegativeButton("NO!",
                        (dialog, which) -> Toast.makeText(getApplicationContext(), "The file " + uri + " will not be deleted", Toast.LENGTH_SHORT).show())
                .create()
                .show();

    }

    private void shareImage() {

        Uri uri = Uri.parse(image_uris.get(viewPager.getCurrentItem()));
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("image/*");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

}