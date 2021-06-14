package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.amrit.practice.filesbygooglereplica.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

//public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> {
//
//    private final Context mContext;
//    private final ArrayList<Bitmap> bitmaps;
//
//    public PdfAdapter(Context mContext, ArrayList<Bitmap> bitmaps) {
//        this.mContext = mContext;
//        this.bitmaps = bitmaps;
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.pdf_card, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
//        holder.imageView.setImageBitmap(bitmaps.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return bitmaps.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView imageView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.pdf_image);
//        }
//    }
//
//}

public class PdfAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Bitmap> bitmaps;

    public PdfAdapter(Context mContext, ArrayList<Bitmap> bitmaps) {
        this.mContext = mContext;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int i) {
        return getItemId(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.pdf_card, null);
        }
        Bitmap bitmap = bitmaps.get(position);

        CardView cardView = convertView.findViewById(R.id.card_for_image);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));

        ImageView imageView = convertView.findViewById(R.id.pdf_image);
        imageView.setImageBitmap(bitmaps.get(position));
        return convertView;

    }

}

