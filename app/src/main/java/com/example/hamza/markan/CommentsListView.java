package com.example.hamza.markan;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsListView extends ArrayAdapter<Comment> {
    private Activity context;
    private ArrayList<Comment> commentList;

    public CommentsListView(Activity context, ArrayList<Comment> commentList){
        super(context, R.layout.listview_layout_comment, commentList);
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_layout_comment, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textViewTitle.setText(commentList.get(position).getTitle());
        viewHolder.textViewComment.setText(commentList.get(position).getComment());
        viewHolder.ratingBar.setRating((float)commentList.get(position).getRating());
        return view;
    }

    class ViewHolder{
        TextView textViewTitle;
        TextView textViewComment;
        RatingBar ratingBar;
        ViewHolder(View view){
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewComment = view.findViewById(R.id.textViewComment);
            ratingBar = view.findViewById(R.id.ratingBarComment);
        }
    }
}
