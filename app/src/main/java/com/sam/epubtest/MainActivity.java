package com.sam.epubtest;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sam.epubtest.adapter.AdapterChaperList;
import com.sam.epubtest.databinding.ActivityMainBinding;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Resources;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class MainActivity extends AppCompatActivity {
private String TAG=getClass().getName();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        try {
            InputStream is = getAssets().open("santi.epub");
            Book book = new EpubReader().readEpub(is);


            Metadata metadata = book.getMetadata();

            String bookInfo = "作者："+metadata.getAuthors()+
                    "\n出版社："+metadata.getPublishers()+
                    "\n出版时间：" +metadata.getDates()+
                    "\n书名："+metadata.getTitles()+
                    "\n简介："+metadata.getDescriptions()+
                    "\n语言："+metadata.getLanguage()+
                    "\n\n封面图：";
            binding.bookDescript.setText(bookInfo);
           Resource coverRes= book.getCoverImage();
//Resources resources=book.getResources();
//Resource coverRes = resources.getById("cover");
            byte[] data = coverRes.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            binding.bookCover.setImageBitmap(bitmap);


           Resource ncxRes= book.getNcxResource();
           String ncx=StringUtils.bytes2Hex(ncxRes.getData());

           Resource opfRes=book.getOpfResource();
            String opf=StringUtils.bytes2Hex(opfRes.getData());

            Resource coverPageRes=book.getCoverPage();
            String coverPage=StringUtils.bytes2Hex(coverPageRes.getData());

            //章节
            logTableOfContents(book.getTableOfContents().getTocReferences(), 0);
            AdapterChaperList adapterChaperList=new AdapterChaperList(beans,this);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
            binding.bookChapterList.setLayoutManager(layoutManager);
            binding.bookChapterList.setAdapter(adapterChaperList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<EpubBean> beans=new ArrayList<>();
    private void logTableOfContents(List<TOCReference> tocReferences, int depth) {

        if (tocReferences == null) {
            return;
        }
        for (TOCReference tocReference : tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            EpubBean bean=new EpubBean();
            bean.setTilte(tocReference.getTitle());
            bean.setHref(tocReference.getCompleteHref());
            beans.add(bean);
            tocString.append(tocReference.getTitle());
            Log.e("epublib", tocString.toString());

            logTableOfContents(tocReference.getChildren(), depth + 1);
        }
    }

}
