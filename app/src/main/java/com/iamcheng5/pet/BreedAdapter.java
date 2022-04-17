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

public class BreedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Breed> breedList = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BreedAdapter() {
    }

    public void updateData(final List<Breed> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BreedAdapter.BreedDiffCallback(breedList, newList), true);
        diffResult.dispatchUpdatesTo(this);
        breedList.clear();
        breedList.addAll(newList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_book_item, parent, false);
        return new BreedAdapter.BreedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BreedAdapter.BreedViewHolder) holder).btn.setText(breedList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return breedList.size();
    }
    public class BreedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button btn;

        public BreedViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.bookRec_btn);
            btn.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getAdapterPosition());
        }
    }
    public class BreedDiffCallback extends DiffUtil.Callback {
        List<Breed> oldData, newData;

        public BreedDiffCallback(List<Breed> oldData, List<Breed> newData) {
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
            return oldData.get(oldItemPosition).getId() == newData.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }
    }

}
