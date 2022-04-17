package com.iamcheng5.pet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SpeciesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> speciesList = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SpeciesAdapter(List<String> speciesList) {
        this.speciesList.addAll(speciesList);
    }

    public void updateData(final List<String> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BookDiffCallback(speciesList, newList), true);
        diffResult.dispatchUpdatesTo(this);
        speciesList.clear();
        speciesList.addAll(newList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BookViewHolder) holder).btn.setText(speciesList.get(position).split("\\|")[0]);
    }

    @Override
    public int getItemCount() {
        return speciesList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button btn;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.bookRec_btn);
            btn.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public class BookDiffCallback extends DiffUtil.Callback {
        List<String> oldData, newData;

        public BookDiffCallback(List<String> oldData, List<String> newData) {
            this.oldData = oldData;
            this.newData = newData;
        }

        @Override
        public int getOldListSize() {
            return oldData == null ? 0 : oldData.size();
        }

        @Override
        public int getNewListSize() {
            return newData == null ? 0 : newData.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            if (null == oldData.get(oldItemPosition) || null == newData.get(newItemPosition))
                return false;
            return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }
    }
}
