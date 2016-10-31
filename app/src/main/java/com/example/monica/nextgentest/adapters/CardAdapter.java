package com.example.monica.nextgentest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.monica.nextgentest.R;
import com.example.monica.nextgentest.models.Github;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monica on 10/26/16.
 */

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
        List<Github> mItems;

        public CardAdapter() {
            super();
            mItems = new ArrayList<Github>();
        }

        public void addData(Github github) {
            mItems.add(github);
            notifyDataSetChanged();
        }

        public void clear() {
            mItems.clear();
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_view, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Github github = mItems.get(i);
            viewHolder.login.setText(github.getLogin());
            viewHolder.repos.setText("repos: " + github.getPublicRepos());
            viewHolder.blog.setText("blog: " + github.getBlog());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView login;
            public TextView repos;
            public TextView blog;

            public ViewHolder(View itemView) {
                super(itemView);
                login = (TextView) itemView.findViewById(R.id.login);
                repos = (TextView) itemView.findViewById(R.id.repos);
                blog = (TextView) itemView.findViewById(R.id.blog);
            }
        }
    }

