package com.fourthmach.inkcompiler;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fourthmach.inkcompiler.SaveFileSystem.ShallowSaveFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final HashMap<String, ShallowSaveFile> itemMap;
    private final ArrayList<String> keyList; // For position-based access
    private final OnItemClickListener listener;

    public RecyclerViewAdapter(HashMap<String, ShallowSaveFile> itemMap, OnItemClickListener listener) {
        this.itemMap = itemMap;
        this.keyList = new ArrayList<>(itemMap.keySet()); // Maintain order
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
        String key = keyList.get(position);
        ShallowSaveFile item = itemMap.get(key);
        if (item == null) return;

        holder.title.setText(item.FileName);
        // holder.description.setText(item.getDescription());

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return keyList.size();
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
        void onItemClick(ShallowSaveFile item);
    }

    // Optional: if you need to update the map and refresh view
    public void updateData(HashMap<String, ShallowSaveFile> newMap) {
        // Only update items that actually changed
        for (Map.Entry<String, ShallowSaveFile> entry : newMap.entrySet()) {
            itemMap.put(entry.getKey(), entry.getValue());
        }

        // Refresh the keyList based on the updated itemMap
        keyList.clear();
        keyList.addAll(itemMap.keySet());

        // Notify RecyclerView that the data has changed
        notifyDataSetChanged();
    }

    // Optional: remove by key
    public void removeItem(String key) {
        if (itemMap.containsKey(key)) {
            int index = keyList.indexOf(key);
            itemMap.remove(key);
            keyList.remove(key);
            notifyItemRemoved(index);
        }
    }
}