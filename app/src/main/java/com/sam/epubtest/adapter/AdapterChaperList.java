package com.sam.epubtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.epubtest.Activity_epubReadView;
import com.sam.epubtest.EpubBean;
import com.sam.epubtest.R;
import com.sam.epubtest.databinding.ItemBinding;

import java.util.List;


/**
 * Created by zhangshexin on 2019/3/4.
 */

public class AdapterChaperList extends RecyclerView.Adapter<AdapterChaperList.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<EpubBean> mStrings;

    public AdapterChaperList(List<EpubBean> mStrings, Context context) {
        inflater = LayoutInflater.from(context);
        this.mStrings = mStrings;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.item, parent, false);
        ViewHolder mVH = new ViewHolder(binding);
        return mVH;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemBinding binding = holder.getBinding();
        EpubBean epubBean = mStrings.get(position);
        binding.bookChapter.setText(epubBean.getTilte());
        binding.bookChapter.setTag(epubBean.getHref());
        binding.bookChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String href = (String) v.getTag();
                Intent mintent = new Intent(mContext, Activity_epubReadView.class);
                mintent.putExtra("href", href);
                mContext.startActivity(mintent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemBinding binding;


        public ViewHolder(ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemBinding getBinding() {
            return binding;
        }
    }
}
