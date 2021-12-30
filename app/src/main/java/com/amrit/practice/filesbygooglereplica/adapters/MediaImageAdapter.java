package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.activities.InfoActivity;
import com.amrit.practice.filesbygooglereplica.activities.ShowImageActivity;
import com.amrit.practice.filesbygooglereplica.Models.ImageUtil;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MediaImageAdapter extends RecyclerView.Adapter<MediaImageAdapter.MediaImageViewHolder>{

    private final ArrayList<ImageUtil> imageUtil;
    private final Context context;
    private final boolean isList;

    public MediaImageAdapter(Context context, ArrayList<ImageUtil> imageUtil, boolean isList) {
        this.context = context;
        this.imageUtil = imageUtil;
        this.isList = isList;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public MediaImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView;
        if(isList){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_media, null, false);
        }else{
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_media, null, false);
        }
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new MediaImageViewHolder(layoutView, isList);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MediaImageViewHolder holder, int position) {
        if(isList){
            Date date = new Date(imageUtil.get(position).getDate()*1000);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
            String dateText = df2.format(date);
            String size = getSize(imageUtil.get(position).getSize());
            setUpPopUp(holder, position);
            holder.linear.setOnClickListener(view -> startImageActivity(position));
            holder.mediaDate.setText(dateText + ", " + size);
        }else{
            holder.mediaName.setShadowLayer(2, 1, 1, Color.BLACK);
            holder.mediaSize.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = imageUtil.get(position).getSize();
            String sizeString = getSize(sizeInt);
            holder.mediaSize.setText(sizeString);
            holder.relative.setOnClickListener(view -> startImageActivity(position));
        }

        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri contentUris = Uri.parse(imageUtil.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
                .placeholder(context.getDrawable(R.drawable.ic_baseline_image_24))
                .into(holder.imageView);
        holder.mediaName.setText(imageUtil.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return imageUtil.size();
    }

    private void startImageActivity(int position){

        Intent intent = new Intent(context, ShowImageActivity.class);

        ArrayList<String> imageUris = new ArrayList<>();
        ArrayList<String> imageName = new ArrayList<>();
        ArrayList<Integer> imageSize = new ArrayList<>();
        ArrayList<Long> imageDate = new ArrayList<>();
        ArrayList<String> imageLocation = new ArrayList<>();

        for(ImageUtil util: imageUtil) {
            imageUris.add(util.getUri());
            imageName.add(util.getName());
            imageSize.add(util.getSize());
            imageDate.add(util.getDate());
            imageLocation.add(util.getPath());
        }

        long[] longList = new long[imageDate.size()];

        for(int j = 0; j < imageDate.size(); j++) longList[j] = imageDate.get(j);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("image_uris", imageUris);
        bundle.putStringArrayList("image_name", imageName);
        bundle.putIntegerArrayList("image_size", imageSize);
        bundle.putLongArray("image_date", longList);
        bundle.putStringArrayList("image_location", imageLocation);

        bundle.putInt("current_position", position);
        intent.putExtra("bundle", bundle);

        context.startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    private void setUpPopUp(MediaImageViewHolder holder, int position) {

        holder.listMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), holder.listMore);
            popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {

                switch (menuItem.getItemId()){
                    case R.id.share:
                        shareImage(imageUtil.get(position).getUri());
                        break;
                    case R.id.open_with:
                        imageOpenWith(position);
                        break;
                    case R.id.file_info:
                        infoImage(position);
                        break;
                    case R.id.delete_permanent:
                        deleteToast(imageUtil.get(position).getUri());
                        break;
                    default:
                        return false;
                }
                return true;
            });
            popupMenu.show();
        });

    }

    private void deleteToast(String uri){
        new AlertDialog.Builder(context)
                .setTitle("Delete Image")
                .setMessage("Do You really want to delete the image")
                .setPositiveButton("YES!!",
                        (dialog, which) -> Toast.makeText(context, "The file " + uri + " is deleted", Toast.LENGTH_SHORT).show())
                .setNegativeButton("NO!",
                        (dialog, which) -> Toast.makeText(context, "The file " + uri + " will not be deleted", Toast.LENGTH_SHORT).show())
                .create()
                .show();
    }

    private void imageOpenWith(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(imageUtil.get(position).getUri());
        intent.setDataAndType(uri, "image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void shareImage(String uri_string) {

        Uri uri = Uri.parse(uri_string);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("image/*");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(shareIntent);
    }

    private void infoImage(int position) {
        Intent intent = new Intent(context, InfoActivity.class);
        Bundle bundle = new Bundle();
        String size_string = getSize(imageUtil.get(position).getSize());

        Date date = new Date(imageUtil.get(position).getDate() * 1000);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy, hh:mm:aa");
        String dateText = df2.format(date);
        bundle.putString("uri", imageUtil.get(position).getUri());
        bundle.putString("name", imageUtil.get(position).getName());
        bundle.putString("location", imageUtil.get(position).getPath());
        bundle.putString("time", dateText);
        bundle.putString("size", size_string);
        bundle.putInt("isMedia", 0);
        intent.putExtra("INFO", bundle);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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

    public static class MediaImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView listMore;
        TextView mediaName;
        TextView mediaSize;
        TextView mediaDate;
        LinearLayout linear;
        RelativeLayout relative;

        public MediaImageViewHolder(@NonNull View itemView, boolean isList) {
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
