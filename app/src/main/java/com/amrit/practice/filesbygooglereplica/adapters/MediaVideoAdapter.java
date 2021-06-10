package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.VideoUtil;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MediaVideoAdapter extends BaseAdapter {

    boolean isList = false;
    private final ArrayList<VideoUtil> videoUtil;
    private final Context context;

    public MediaVideoAdapter(ArrayList<VideoUtil> videoUtil, Context context) {
        this.videoUtil = videoUtil;
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoUtil.size();
    }

    @Override
    public Object getItem(int i) {
        return getItemId(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_video, null);
            else convertView = layoutInflater.inflate(R.layout.list_video, null);
        }

        ImageView imageView;

        if(isList) {
            imageView = convertView.findViewById(R.id.video_list_image_view);
            TextView name = convertView.findViewById(R.id.file_name);
            name.setText(videoUtil.get(position).getName());
        }else{
            imageView = convertView.findViewById(R.id.video_grid_image_view);
            TextView sizeText = convertView.findViewById(R.id.video_size);
            sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = videoUtil.get(position).getSize();
            String sizeString = getSize(sizeInt);
            sizeText.setText(sizeString);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri contentUris = Uri.parse(videoUtil.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
                .placeholder(context.getDrawable(R.drawable.ic_baseline_videocam_24))
                .into(imageView);

        return convertView;
    }

    @NotNull
    private String getSize(int size){
        size /= 8;
        float sizeFloat = (float) size / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);

        if(sizeFloat < 1024) return sizeFloat + "KB";

        sizeFloat = sizeFloat / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);
        return sizeFloat + "MB";
    }

}
