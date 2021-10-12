package com.example.simplechatapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simplechatapplication.databinding.ActivityHomeBinding;
import com.example.simplechatapplication.databinding.ListItemChatBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeRecyclerviewAdapter extends RecyclerView.Adapter<HomeRecyclerviewAdapter.HomeRecyclerViewHolder> {

    ArrayList<String> messageList;

    public HomeRecyclerviewAdapter(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemChatBinding listItemChatBinding = ListItemChatBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new HomeRecyclerViewHolder(listItemChatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {
        holder.listItemChatBinding.recyclerViewTextview.setText(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder{

        ListItemChatBinding listItemChatBinding;

        public HomeRecyclerViewHolder(ListItemChatBinding listItemChatBinding) {
            super(listItemChatBinding.getRoot());
            this.listItemChatBinding = listItemChatBinding;
        }
    }

}
