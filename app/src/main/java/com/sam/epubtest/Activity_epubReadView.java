package com.sam.epubtest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sam.epubtest.databinding.LayoutEpubDetailBinding;

import java.io.InputStream;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by zhangshexin on 2019/3/4.
 *
 * epub阅读阅
 */

public class Activity_epubReadView extends AppCompatActivity {

    private String TAG=getClass().getName();
    private LayoutEpubDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.layout_epub_detail);
        String href = getIntent().getStringExtra("href");
        Log.i(TAG, "onCreate: href=" + href);
        try {
            EpubReader reader = new EpubReader();
            InputStream in = getAssets().open("santi.epub");
            Book book = reader.readEpub(in);
            Resource byHref = book.getResources().getByHref(href);
//            byte[] bytes=new byte[byHref.getInputStream().available()];
//            byHref.getInputStream().read(bytes);
//            String s=new String(bytes,"Utf-8");
            byte[] data = byHref.getData();   //和 resource.getInputStream() 返回的都是html格式的文章内容，只不过读取方式不一样
            String strHtml1 = StringUtils.bytes2Hex(data);
            //Log.i(TAG, "initView: strHtml1111= " + strHtml1);


            binding.webview.getSettings().setJavaScriptEnabled(true);
            binding.webview.loadDataWithBaseURL(null, strHtml1, "text/html", "utf-8", null);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
