package com.r21nomi.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.r21nomi.blobtransition.BlobLayout;

import java.util.List;

/**
 * Created by r21nomi on 2016/12/11.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> dataSet;
    private Listener listener;

    public ItemAdapter(List<Item> dataSet, Listener listener) {
        this.dataSet = dataSet;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = dataSet.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(item.getThumb())
                .into(holder.thumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(holder.blobLayout, item);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        BlobLayout blobLayout;
        ImageView thumb;
        TextView title;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            blobLayout = (BlobLayout) itemView.findViewById(R.id.maskLayout);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }

    public interface Listener {
        void onClick(View view, Item item);
    }
}

