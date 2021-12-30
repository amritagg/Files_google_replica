package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.Models.DownloadUtils;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MediaDownloadAdapter extends RecyclerView.Adapter<MediaDownloadAdapter.MediaDownloadViewHolder> {

    private final ArrayList<DownloadUtils> downloadUtils;
    private final Context context;
    private final boolean isList;

    public MediaDownloadAdapter(Context context, ArrayList<DownloadUtils> downloadUtils, boolean isList) {
        this.context = context;
        this.downloadUtils = downloadUtils;
        this.isList = isList;
    }

    @NonNull
    @Override
    public MediaDownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View layoutView;
        if(isList){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_media, null, false);
        }else{
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_media, null, false);
        }
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new MediaDownloadViewHolder(layoutView, isList);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MediaDownloadViewHolder holder, int position) {
        if(isList){
            Date date = new Date(downloadUtils.get(position).getDate()*1000);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
            String dateText = df2.format(date);
            String size = getSize(downloadUtils.get(position).getSize());
//            setUpPopUp(holder, position);
//            holder.linear.setOnClickListener(view -> startImageActivity(position));
            holder.mediaDate.setText(dateText + ", " + size);
        }else{
            holder.mediaName.setShadowLayer(2, 1, 1, Color.BLACK);
            holder.mediaSize.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = downloadUtils.get(position).getSize();
            String sizeString = getSize(sizeInt);
            holder.mediaSize.setText(sizeString);
//            holder.relative.setOnClickListener(view -> startImageActivity(position));
        }

        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri contentUris = Uri.parse(downloadUtils.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
                .placeholder(context.getDrawable(R.drawable.ic_baseline_document_24))
                .error(context.getDrawable(R.drawable.ic_baseline_document_24))
                .into(holder.imageView);
        holder.mediaName.setText(downloadUtils.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return downloadUtils.size();
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

    public static class MediaDownloadViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView listMore;
        TextView mediaName;
        TextView mediaSize;
        TextView mediaDate;
        LinearLayout linear;
        RelativeLayout relative;

        public MediaDownloadViewHolder(@NonNull View itemView, boolean isList) {
            super(itemView);
            if(isList){
                imageView = itemView.findViewById(R.id.list_image_view);
                mediaName = itemView.findViewById(R.id.media_name_list);
                linear = itemView.findViewById(R.id.list_linearLayout);
                listMore = itemView.findViewById(R.id.list_more);
                mediaDate = itemView.findViewById(R.id.media_size_date_list);
            }else{
                imageView = itemView.findViewById(R.id.grid_image_view);
                mediaName = itemView.findViewById(R.id.media_name_grid);
                mediaSize = itemView.findViewById(R.id.media_size);
                relative = itemView.findViewById(R.id.grid_layout);
            }
        }
    }

}

//    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables"})
//    @Override
//    public View getView(int position, View convertView, ViewGroup viewGroup) {
//
//        if(!isList) {
//            imageView = convertView.findViewById(R.id.grid_image_view);
//
//            TextView sizeText = convertView.findViewById(R.id.media_size);
//            TextView name = convertView.findViewById(R.id.media_name_grid);
//            sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
//
//            int sizeInt = downloadUtils.get(position).getSize();
//            name.setShadowLayer(2, 1, 1, Color.BLACK);
//            name.setText(downloadUtils.get(position).getName());
//            String sizeString = getSize(sizeInt);
//            sizeText.setText(sizeString);
//        }
//
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        Uri contentUris = Uri.parse(downloadUtils.get(position).getUri());
//        Glide.with(context)
//                .load(contentUris)
//                .error(context.getDrawable(R.drawable.ic_baseline_document_24))
//                .into(imageView);
//
//        return convertView;
//    }
