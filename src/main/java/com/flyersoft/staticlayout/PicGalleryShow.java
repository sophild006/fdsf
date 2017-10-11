package com.flyersoft.staticlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flyersoft.books.A;

public class PicGalleryShow extends AppCompatActivity implements View.OnClickListener {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (A.book_cache == null) {
            finish();
            return;
        }
        requestWindowFeature(1);
        getWindow().addFlags(1024);
        try {
            Bundle extras = getIntent().getExtras();

        } catch (Exception e) {
            A.error(e);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

    }
}