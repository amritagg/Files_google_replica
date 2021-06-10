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
import com.amrit.practice.filesbygooglereplica.utils.DownloadUtils;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaDownloadAdapter extends BaseAdapter {

    boolean isList = false;
    private final ArrayList<DownloadUtils> downloadUtils;
    private final Context context;

    public MediaDownloadAdapter(Context context, ArrayList<DownloadUtils> downloadUtils) {
        this.context = context;
        this.downloadUtils = downloadUtils;
    }

    @Override
    public int getCount() {
        return downloadUtils.size();
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_download, null);
            else convertView = layoutInflater.inflate(R.layout.list_download, null);
        }

        ImageView imageView;

        if(isList) {
            imageView = convertView.findViewById(R.id.download_list_image_view);
            TextView name = convertView.findViewById(R.id.file_name);
            name.setText(downloadUtils.get(position).getName());
        }else{
            imageView = convertView.findViewById(R.id.download_grid_image_view);
            TextView sizeText = convertView.findViewById(R.id.download_size);
            TextView name = convertView.findViewById(R.id.download_name_grid);
            sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = (int) downloadUtils.get(position).getSize();
            name.setShadowLayer(2, 1, 1, Color.BLACK);
            name.setText(downloadUtils.get(position).getName());
            String sizeString = getSize(sizeInt);
            sizeText.setText(sizeString);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri contentUris = Uri.parse(downloadUtils.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
//                .error(context.getDrawable(R.drawable.ic_baseline_document_24))
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
