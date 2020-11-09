package com.example.pagination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.pagination.Adapter.MyAdapter;
import com.example.pagination.Interface.LoadMore;
import com.example.pagination.Model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    List<Item> items=new ArrayList<>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random10Data();

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MyAdapter(recyclerView,this,items);
        recyclerView.setAdapter(adapter);

        //set load more event
        adapter.setLoadmore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if(items.size() <= 20){
                    items.add(null);
                    adapter.notifyItemInserted(items.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items.remove(items.size()-1);
                            adapter.notifyItemRemoved(items.size());

                            //random more data
                            int index=items.size();
                            int end=index+10;
                            for(int i=index;i<end;i++)
                            {
                                String name=UUID.randomUUID().toString();
                                Item item=new Item(name,name.length());
                                items.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
                    },2000);
                }else{
                    Toast.makeText(MainActivity.this,"Load data Completed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void random10Data() {
        for(int i=0;i<20;i++){
            String name= UUID.randomUUID().toString();
            Item item=new Item(name,name.length());
            items.add(item);
        }
    }
}