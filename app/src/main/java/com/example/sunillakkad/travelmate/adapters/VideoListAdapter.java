package com.example.sunillakkad.travelmate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunillakkad.travelmate.R;
import com.example.sunillakkad.travelmate.model.VideoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private Context mContext;
    private List<VideoItem> mVideoItems;
    private OnItemListener mOnItemListener;

    public interface OnItemListener {
        void onItemClicked(VideoItem videoItem);
    }

    public void setOnItemClickListener(final OnItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    public VideoListAdapter(Context context, List<VideoItem> videoItems) {
        this.mContext = context;
        this.mVideoItems = videoItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;
        TextView description;

        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.video_thumbnail);
            title = (TextView) itemView.findViewById(R.id.video_title);
            description = (TextView) itemView.findViewById(R.id.video_description);

            itemView.setOnClickListener(v -> {
                if (mOnItemListener != null)
                    mOnItemListener.onItemClicked(mVideoItems.get(getAdapterPosition()));
            });
        }
    }

    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
        return new ViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(VideoListAdapter.ViewHolder holder, int position) {
        VideoItem video = mVideoItems.get(position);

        Picasso.with(mContext).load(video.getThumbnailURL()).into(holder.thumbnail);
        holder.title.setText(video.getTitle());
        holder.description.setText(video.getPublishDate());
    }

    @Override
    public int getItemCount() {
        return mVideoItems.size();
    }
}
