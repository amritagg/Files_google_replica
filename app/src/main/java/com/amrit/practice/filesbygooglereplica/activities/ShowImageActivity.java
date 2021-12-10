package com.amrit.practice.filesbygooglereplica.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.ViewPagerTransformer;
import com.amrit.practice.filesbygooglereplica.adapters.ShowImageViewPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class ShowImageActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    ShowImageViewPagerAdapter adapter;
    ArrayList<String> image_uris, image_names, image_location;
    ArrayList<Integer> image_size;
    long[] image_date;
    private int current;
    ConstraintLayout baseLayout;
    LinearLayout topLayout;
    Button back, share, info, delete;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Intent intent = getIntent();
        if(intent.hasExtra("bundle")){
            Bundle bundle = intent.getBundleExtra("bundle");

            assert bundle != null;
            current = bundle.getInt("current_position");
            image_uris = bundle.getStringArrayList("image_uris");
            image_date = bundle.getLongArray("image_date");
            image_names = bundle.getStringArrayList("image_name");
            image_location = bundle.getStringArrayList("image_location");
            image_size = bundle.getIntegerArrayList("image_size");

            adapter = new ShowImageViewPagerAdapter(this, image_uris, true);

        }

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(100));

        viewPager2 = findViewById(R.id.show_image_view_pager);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager2.setAdapter(adapter);
        viewPager2.setPageTransformer(new ViewPagerTransformer());
        viewPager2.setCurrentItem(current);
        baseLayout = findViewById(R.id.base_layout);
        topLayout = findViewById(R.id.top_layout);
        back = findViewById(R.id.back_button);
        share = findViewById(R.id.image_share);
        info = findViewById(R.id.image_info);
        delete = findViewById(R.id.image_delete);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("show_hide"));

        back.setOnClickListener(view -> onBackPressed());
        share.setOnClickListener(view -> shareImage());
        info.setOnClickListener(view -> infoImage());
        delete.setOnClickListener(view -> deleteImage());
        
    }

    private void infoImage() {
        int n = viewPager2.getCurrentItem();
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

        int currentID = viewPager2.getCurrentItem();
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

        Uri uri = Uri.parse(image_uris.get(viewPager2.getCurrentItem()));
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("image/*");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }
    
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int visible = baseLayout.getVisibility();
            if(visible == 0){
                baseLayout.setVisibility(View.GONE);
                topLayout.setVisibility(View.GONE);
            }else{
                baseLayout.setVisibility(View.VISIBLE);
                topLayout.setVisibility(View.VISIBLE);
            }
        }
    };

/*
    
*/

}