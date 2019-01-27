package com.example.hamza.markan;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoriesListView extends ArrayAdapter<String>{

    private String[] categories;
    private Integer[] imagesID;
    private Activity context;

    public CategoriesListView(Activity context, String[] categories, Integer[] imagesID) {
        super(context, R.layout.listview_layout_category, categories);
        this.context = context;
        this.categories = categories;
        this.imagesID = imagesID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_layout_category, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageView.setImageResource(imagesID[position]);
        viewHolder.textView.setText(categories[position]);

        return view;
    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;
        ViewHolder(View view){
            textView = view.findViewById(R.id.textViewCategory);
            imageView = view.findViewById(R.id.imageViewCategory);
        }
    }
}
