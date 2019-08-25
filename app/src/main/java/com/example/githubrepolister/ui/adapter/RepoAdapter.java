package com.example.githubrepolister.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubrepolister.R;
import com.example.githubrepolister.network.model.RepoResponse;
import com.example.githubrepolister.ui.activity.DetailActivity;
import com.example.githubrepolister.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    private ArrayList<RepoResponse> repoList;
    private ItemClickListener itemClickListener;
    private PreferencesUtil preferencesUtil;

    public RepoAdapter(ArrayList<RepoResponse> repoList, ItemClickListener itemClickListener, PreferencesUtil preferencesUtil) {
        this.repoList = repoList;
        this.itemClickListener = itemClickListener;
        this.preferencesUtil = preferencesUtil;
    }

    @Override
    public RepoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.clContainer.setOnClickListener(v -> itemClickListener.onRepoClicked(position, repoList));
        holder.tvRepoName.setText(repoList.get(position).getName());

        if(preferencesUtil.getRepoIdList(DetailActivity.KEY_SAVED_ID) != null && preferencesUtil.getRepoIdList(DetailActivity.KEY_SAVED_ID).contains(repoList.get(position).getId().toString()))
            holder.ivFav.setVisibility(View.VISIBLE);
        else
            holder.ivFav.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return repoList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRepoName;
        private ImageView ivFav;
        private ConstraintLayout clContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRepoName = itemView.findViewById(R.id.tv_repo_name);
            ivFav = itemView.findViewById(R.id.iv_fav);
            clContainer = itemView.findViewById(R.id.cl_container);
        }
    }

    public interface ItemClickListener {
        void onRepoClicked(int position, List<RepoResponse> repoList);
    }
}
