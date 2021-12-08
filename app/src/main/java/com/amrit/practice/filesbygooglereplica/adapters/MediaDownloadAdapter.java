package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.DownloadUtils;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaDownloadAdapter extends RecyclerView.Adapter<MediaDownloadAdapter.MediaDownloadViewHolder> {

    private final ArrayList<DownloadUtils> downloadUtils;
    private final Context context;

    public MediaDownloadAdapter(Context context, ArrayList<DownloadUtils> downloadUtils) {
        this.context = context;
        this.downloadUtils = downloadUtils;
    }

    @NonNull
    @Override
    public MediaDownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_media, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new MediaDownloadViewHolder(layoutView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MediaDownloadViewHolder holder, int position) {
        holder.textView.setText(downloadUtils.get(position).getName());
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Uri contentUris = Uri.parse(downloadUtils.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
                .error(context.getDrawable(R.drawable.ic_baseline_document_24))
                .into(holder.imageView);
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
        TextView textView;
        LinearLayout layout;
        ImageView listMore;

        public MediaDownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_image_view);
            textView = itemView.findViewById(R.id.media_name_list);
            layout = itemView.findViewById(R.id.list_linearLayout);
            listMore = itemView.findViewById(R.id.list_more);
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
