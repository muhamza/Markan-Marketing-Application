package com.example.hamza.markan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StoreListView extends ArrayAdapter<Store> {

    private Activity context;
    private ArrayList<Store> storeList;

    public StoreListView(Activity context, ArrayList<Store> storeList){
        super(context, R.layout.listview_layout_store, storeList);
        this.context = context;
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_layout_store, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Picasso.get().load(storeList.get(position).getLogo()).into(viewHolder.imageViewStoreLogo);
        viewHolder.textViewStoreName.setText(storeList.get(position).getStoreName());
        viewHolder.textViewTagline.setText(storeList.get(position).getTagline());
        return view;
    }

    class ViewHolder{
        TextView textViewStoreName;
        TextView textViewTagline;
        ImageView imageViewStoreLogo;
        ViewHolder(View view){
            textViewStoreName = view.findViewById(R.id.textViewStoreName);
            textViewTagline = view.findViewById(R.id.textViewTagline);
            imageViewStoreLogo = view.findViewById(R.id.imageViewStoreLogo);
        }
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = context.getLayoutInflater();
//
//        View listViewItem = inflater.inflate(R.layout.listview_layout_store, null, true);
//
//        TextView textViewStoreName = listViewItem.findViewById(R.id.textViewStoreName);
//        TextView textViewDescription = listViewItem.findViewById(R.id.textViewDescription);
//        Store store = storeList.get(position);
//        textViewStoreName.setText(store.getStoreName());
//        textViewDescription.setText(store.getDetails());
//
//        return listViewItem;
//    }
}
