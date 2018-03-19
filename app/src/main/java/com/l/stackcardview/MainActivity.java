package com.l.stackcardview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<User> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new User(R.drawable.pic1, "名字1", "其他1"));
        mData.add(new User(R.drawable.pic2, "名字2", "其他2"));
        mData.add(new User(R.drawable.pic3, "名字3", "其他3"));
        mData.add(new User(R.drawable.pic4, "名字4", "其他4"));
        mData.add(new User(R.drawable.pic5, "名字5", "其他5"));
        mData.add(new User(R.drawable.pic6, "名字6", "其他6"));
        mData.add(new User(R.drawable.pic7, "名字7", "其他7"));
        mData.add(new User(R.drawable.pic8, "名字8", "其他8"));
    }

    private void initView() {
        RecyclerView rv = findViewById(R.id.rv);

        StackCardConfig.initConfig(this);
        rv.setLayoutManager(new StackCardLayoutManager());
        CardAdapter mAdapter = new CardAdapter();
        rv.setAdapter(mAdapter);

        //三步
        //1.创建ItemTuchCallBack
        StackCardTouchHelper callBack = new StackCardTouchHelper(rv, mAdapter, mData);
        //2.创建ItemTouchHelper并把callBack传进去
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBack);
        //3.与RecyclerView关联起来
        itemTouchHelper.attachToRecyclerView(rv);
    }

    class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_card, parent, false);
            return new CardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
            //用Glide来加载图片
            Glide.with(getApplicationContext())
                    .load(mData.get(position).getPhotoResId())
                    .apply(new RequestOptions().transform(new CenterCrop()))
                    .into(holder.ivCard);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

//        //右滑的时候调用
//        public void addLoveCount() {
//            loveCount++;
//            tv_love_count.setText("喜欢:" + loveCount);
//        }
//
//        //左滑的时候调用
//        public void addDelCount(){
//            delCount++;
//            tv_del_count.setText("删除:" + delCount);
//        }

        class CardViewHolder extends RecyclerView.ViewHolder {

            ImageView ivCard;

            CardViewHolder(View itemView) {
                super(itemView);
                ivCard = itemView.findViewById(R.id.iv_card);
            }
        }
    }
}
