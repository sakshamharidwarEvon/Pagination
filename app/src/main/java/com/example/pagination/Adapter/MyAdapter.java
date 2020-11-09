package com.example.pagination.Adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pagination.Interface.LoadMore;
import com.example.pagination.Model.Item;
import com.example.pagination.R;

import java.util.List;

import javax.xml.namespace.QName;

class LoadingViewHolder extends RecyclerView.ViewHolder
{
    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView)
    {
        super(itemView);
        progressBar=(ProgressBar) itemView.findViewById(R.id.progressBar);

    }
}

class ItemViewHolder extends RecyclerView.ViewHolder{
    public TextView name,length;

    public ItemViewHolder(View itemView){
        super(itemView);
        name=(TextView)itemView.findViewById(R.id.txtName);
        length=(TextView)itemView.findViewById(R.id.txtLength);

    }
}

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;
    LoadMore loadmore;
    boolean isLoading;
    Activity activity;
    List<Item> items;
    int visibleThreshold=5;
    int lastVisibleItem,totalItemCount;

    public MyAdapter(RecyclerView recyclerView,Activity activity, List<Item> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount=linearLayoutManager.getItemCount();
                lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
                Log.e("in scroll",""+totalItemCount);
                Log.e("in scroll",""+lastVisibleItem);
                if (!isLoading && totalItemCount < (lastVisibleItem+visibleThreshold)) {
                    if (loadmore != null)
                        loadmore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position)==null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadmore(LoadMore loadmore) {
        this.loadmore = loadmore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_layout,parent,false);
            return new ItemViewHolder(view);
        }
        else if (viewType == VIEW_TYPE_LOADING){
            View view=LayoutInflater.from(activity)
                    .inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemViewHolder)
        {
            Item item=items.get(position);
            ItemViewHolder viewholder =(ItemViewHolder) holder;
            viewholder.name.setText(items.get(position).getName());
            viewholder.length.setText(String.valueOf(items.get(position).getLength()));

        }
        else
            if(holder instanceof LoadingViewHolder)
            {
                LoadingViewHolder loadingViewHolder=(LoadingViewHolder)holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void setLoaded() {
        isLoading = false;
    }
}
