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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.activities.ImageInfoActivity;
import com.amrit.practice.filesbygooglereplica.utils.ImageUtil;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Date;

public class MediaImageAdapter extends BaseAdapter {

    private final boolean isList;
    private final ArrayList<ImageUtil> imageUtil;
    private final Context context;
    private Toast mToast;

    public MediaImageAdapter(Context context, ArrayList<ImageUtil> imageUtil, boolean isList) {
        this.context = context;
        this.imageUtil = imageUtil;
        this.isList = isList;
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
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_media, null);
            else convertView = layoutInflater.inflate(R.layout.list_view, null);
        }

        ImageView imageView;

        if(isList) {
            imageView = convertView.findViewById(R.id.list_image_view);
            TextView name = convertView.findViewById(R.id.media_name_list);
            name.setText(imageUtil.get(position).getName());
            setUpPopUp(convertView, position);
        }else{
            imageView = convertView.findViewById(R.id.grid_image_view);
            TextView sizeText = convertView.findViewById(R.id.media_size);
            sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = imageUtil.get(position).getSize();
            String sizeString = getSize(sizeInt);
            sizeText.setText(sizeString);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri contentUris = Uri.parse(imageUtil.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
                .placeholder(context.getDrawable(R.drawable.ic_baseline_image_24))
                .into(imageView);

        return convertView;
    }

    @SuppressLint("NonConstantResourceId")
    private void setUpPopUp(View convertView, int position) {

        ImageView imageMore = convertView.findViewById(R.id.list_more);
        imageMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), imageMore);
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
                        deleteToast();
                        break;
                    case R.id.yes:
                        deleteYesToast(imageUtil.get(position).getUri());
                        break;
                    case R.id.no:
                        deleteNoToast(imageUtil.get(position).getUri());
                        break;
                    default:
                        return false;
                }
                return true;
            });
            popupMenu.show();
        });

    }

    private void deleteToast(){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(context,
                "Do you really want to delete image", Toast.LENGTH_LONG);
        mToast.show();
    }

    private void deleteYesToast(String uri){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(context,
                "The file " + uri + " is deleted", Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void deleteNoToast(String uri){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(context,
                "The file " + uri + " will not be deleted", Toast.LENGTH_SHORT);
        mToast.show();
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
        Intent intent = new Intent(context, ImageInfoActivity.class);
        Bundle bundle = new Bundle();
        String size_string = getSize(imageUtil.get(position).getSize());

        Date date = new Date(imageUtil.get(position).getDate() * 1000);
        bundle.putString("uri", imageUtil.get(position).getUri());
        bundle.putString("name", imageUtil.get(position).getName());
        bundle.putString("location", imageUtil.get(position).getLocation());
        bundle.putString("time", date.toString());
        bundle.putString("size", size_string);
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

}
