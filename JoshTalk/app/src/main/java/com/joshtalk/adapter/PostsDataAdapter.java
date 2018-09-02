package com.joshtalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.joshtalk.R;
import com.joshtalk.model.PostModel;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class PostsDataAdapter extends RecyclerView.Adapter<PostsDataAdapter.MyViewHolder> {
    private Context context;
    private List<PostModel> contactListFiltered;
    private PostsDataAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date,views,likes,shares;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            thumbnail = view.findViewById(R.id.thumbnail);

            views = view.findViewById(R.id.view_val);
            likes = view.findViewById(R.id.likes_val);
            shares = view.findViewById(R.id.share_val);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onPostDataSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public PostsDataAdapter(Context context, List<PostModel> contactList, PostsDataAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PostModel contact = contactListFiltered.get(position);
        holder.name.setText(contact.getEvent_name());


        holder.date.setText(formatDate(contact.getEvent_date()));
       holder.views.setText(String.valueOf(contact.getViews()));
       holder.likes.setText(String.valueOf(contact.getLikes()));
       holder.shares.setText(String.valueOf(contact.getShares()));

        Glide.with(context)
                .load(contact.getThumbnail_image())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public interface PostsDataAdapterListener {
        void onPostDataSelected(PostModel contact);
    }

    private String formatDate(long millis)
    {
        Date date=new Date(millis);
        return DateFormat.getDateInstance().format(date);
    }
}
