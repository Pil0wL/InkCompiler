package com.fourthmach.inkcompiler;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<SaveFile>  itemList;
    private final OnItemClickListener listener;

    public RecyclerViewAdapter(List<SaveFile> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_file_frame, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SaveFile item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.savefile_displaytitel);
            description = itemView.findViewById(R.id.savefile_discription);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SaveFile item);
    }
}