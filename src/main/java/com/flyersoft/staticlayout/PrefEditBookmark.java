package com.flyersoft.staticlayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import com.flyersoft.books.A;
import com.flyersoft.components.ColorDialog;
import com.flyersoft.components.ColorDialog.OnSaveColor;
import com.flyersoft.moonreader.R;

public class PrefEditBookmark implements OnClickListener {
    CheckBox colorCb;
    View colorLay;
    View colorV;
    Integer editColor;
    EditText et;
    CheckBox noAskCb;
    OnAfterEdit onAfterEdit;
    Context res;
    View root;
    String text;

    public interface OnAfterEdit {
        void AfterEdit(String str, Integer num);
    }

    public PrefEditBookmark(Context context, String text1, OnAfterEdit onAfterEdit1, Integer editColor1) {
        this.res = context;
        this.text = text1;
        this.onAfterEdit = onAfterEdit1;
        this.editColor = editColor1;
        this.root = LayoutInflater.from(context).inflate(R.layout.pref_edit_bookmark2, null);
        initView();
        new AlertDialog.Builder(context).setTitle((int) R.string.add_bookmark).setView(this.root).setPositiveButton("17039370", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                boolean z = true;
                PrefEditBookmark.this.text = PrefEditBookmark.this.et.getText().toString();
                A.bookmarkManually = !PrefEditBookmark.this.noAskCb.isChecked();
                if (PrefEditBookmark.this.editColor == null) {
                    if (PrefEditBookmark.this.colorCb.isChecked()) {
                        z = false;
                    }
                    A.bookmarkNoColor = z;
                } else if (!PrefEditBookmark.this.colorCb.isChecked()) {
                    PrefEditBookmark.this.editColor = Integer.valueOf(0);
                }
                PrefEditBookmark.this.onAfterEdit.AfterEdit(PrefEditBookmark.this.text, PrefEditBookmark.this.editColor);
            }
        }).setNegativeButton("17039360", null).show();
    }

    private void initView() {
        this.et = (EditText) this.root.findViewById(R.id.noteEt);
        this.et.setTextSize(A.isTablet ? 18.0f : 16.0f);
        this.colorCb = (CheckBox) this.root.findViewById(R.id.checkBox2);
        this.noAskCb = (CheckBox) this.root.findViewById(R.id.checkBox);
        this.colorV = this.root.findViewById(R.id.colorV);
        this.colorLay = this.root.findViewById(R.id.colorLay);
        this.colorLay.setOnClickListener(this);
        this.et.setText(this.text);
        this.colorV.setBackgroundColor(A.bookmark_color );
        CheckBox checkBox = this.colorCb;
        boolean z = (this.editColor == null && !A.bookmarkNoColor) || !(this.editColor == null || this.editColor.intValue() == 0);
        checkBox.setChecked(z);
        if (!this.colorCb.isChecked()) {
            this.colorLay.setVisibility(View.GONE);
            this.colorCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PrefEditBookmark.this.colorCb.setOnCheckedChangeListener(null);
                    if (isChecked) {
                        PrefEditBookmark.this.onClick(PrefEditBookmark.this.colorLay);
                        PrefEditBookmark.this.colorLay.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    public void onClick(View v) {
        if (v == this.colorLay) {
            new ColorDialog(this.res, this.res.getString(R.string.bookmark), true, A.bookmark_color, new OnSaveColor() {
                public void getColor(int color) {
                    if (PrefEditBookmark.this.editColor != null) {
                        PrefEditBookmark.this.editColor = Integer.valueOf(color);
                    } else {
                        A.bookmark_color = color;
                    }
                    PrefEditBookmark.this.colorV.setBackgroundColor(color);
                    PrefEditBookmark.this.colorLay.setVisibility(0);
                }
            }).show();
        }
    }
}