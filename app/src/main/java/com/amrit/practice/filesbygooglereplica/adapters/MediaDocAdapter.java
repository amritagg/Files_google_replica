package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.DocumentsUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaDocAdapter extends BaseAdapter {

    private final static boolean isList = true;
    private final Context context;
    private final ArrayList<DocumentsUtil> data;

    public MediaDocAdapter(Context context, ArrayList<DocumentsUtil> data) {
        this.context = context;
        this.data = data;
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
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_doc, null);
            else convertView = layoutInflater.inflate(R.layout.list_doc, null);
        }

        ImageView imageView;

        if(isList) {
            imageView = convertView.findViewById(R.id.doc_list_image_view);
            TextView name = convertView.findViewById(R.id.file_name);
            name.setText(data.get(position).getName());
        }else{
            imageView = convertView.findViewById(R.id.doc_grid_image_view);
            TextView sizeText = convertView.findViewById(R.id.doc_size);
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
        size /= 8;
        float sizeFloat = (float) size / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);

        if(sizeFloat < 1024) return sizeFloat + "KB";

        sizeFloat = sizeFloat / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);
        return sizeFloat + "MB";
    }
}
