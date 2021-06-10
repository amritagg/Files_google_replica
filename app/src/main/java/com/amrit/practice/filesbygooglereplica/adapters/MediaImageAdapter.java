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
import com.amrit.practice.filesbygooglereplica.utils.ImageUtil;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaImageAdapter extends BaseAdapter {

    boolean isList = false;
    private final ArrayList<ImageUtil> imageUtil;
    private final Context context;

    public MediaImageAdapter(Context context, ArrayList<ImageUtil> imageUtil) {
        this.context = context;
        this.imageUtil = imageUtil;
    }

    @Override
    public int getCount() {
        return imageUtil.size();
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_image, null);
            else convertView = layoutInflater.inflate(R.layout.list_image, null);
        }

        ImageView imageView;

        if(isList) {
            imageView = convertView.findViewById(R.id.image_list_image_view);
            TextView name = convertView.findViewById(R.id.file_name);
            name.setText(imageUtil.get(position).getName());
        }else{
            imageView = convertView.findViewById(R.id.image_grid_image_view);
            TextView sizeText = convertView.findViewById(R.id.image_size);
            sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = imageUtil.get(position).getSize();
            String sizeString = getSize(sizeInt);
            sizeText.setText(sizeString);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri contentUris = Uri.parse(imageUtil.get(position).getUri());
        Glide.with(context).load(contentUris).into(imageView);

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
