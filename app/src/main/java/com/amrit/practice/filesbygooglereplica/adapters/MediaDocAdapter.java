package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.DocumentsUtil;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MediaDocAdapter extends BaseAdapter {

    private final boolean isList;
    private final Context context;
    private final ArrayList<DocumentsUtil> data;
    private Toast mToast;
    private static final String LOG_TAG = MediaDocAdapter.class.getSimpleName();

    public MediaDocAdapter(Context context, ArrayList<DocumentsUtil> data, boolean isList) {
        this.context = context;
        this.data = data;
        this.isList = isList;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return getItemId(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_media, null);
            else convertView = layoutInflater.inflate(R.layout.list_media, null);
        }

        ImageView imageView;

        if(isList) {
            imageView = convertView.findViewById(R.id.list_image_view);
            TextView name = convertView.findViewById(R.id.media_name_list);
            name.setText(data.get(position).getName());
            setupPopUp(convertView, position);
        }else{
            imageView = convertView.findViewById(R.id.grid_image_view);
            TextView sizeText = convertView.findViewById(R.id.media_size);
            sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
            String size_text = getSize((int) data.get(position).getSize());
            sizeText.setText(size_text);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_document_24));

        return convertView;
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

    @SuppressLint("NonConstantResourceId")
    private void setupPopUp(View convertView, int position) {

        ImageView imageView = convertView.findViewById(R.id.list_more);
        imageView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), imageView);
            popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.share:
                        shareDocument(data.get(position).getUri());
                        break;
                    case R.id.open_with:
                        docOpenWith(position);
                        break;
                    case R.id.file_info:
                        infoDocument(position);
                        break;
                    case R.id.delete_permanent:
                        deleteToast();
                        break;
                    case R.id.yes:
                        deleteYesToast(data.get(position).getUri());
                        break;
                    case R.id.no:
                        deleteNoToast(data.get(position).getUri());
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
                "Do you really want to delete doc", Toast.LENGTH_LONG);
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

    private void docOpenWith(int position) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(data.get(position).getUri());
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Can't find any app");
            if(mToast != null) mToast.cancel();
            mToast = Toast.makeText(context, "No App found to complete the action", Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    private void shareDocument(String uri_string) {

        Uri uri = Uri.parse(uri_string);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("application/pdf");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(shareIntent);

    }

    private void infoDocument(int position) {
//        Intent intent = new Intent(context, docInfoActivity.class);
//        Bundle bundle = new Bundle();
//
//        Date date = new Date(data.get(position).getDate() * 1000);
//        bundle.putString("uri", data.get(position).getUri());
//        bundle.putString("name", data.get(position).getName());
//        bundle.putString("location", data.get(position).getLocation());
//        bundle.putString("time", date.toString());
//        bundle.putString("size", getSize(data.get(position).getSize()));
//        intent.putExtra("INFO", bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

}
