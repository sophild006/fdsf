package com.flyersoft.moonreader;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import com.flyersoft.components.ColorDialog;
import com.flyersoft.components.ColorDialog.OnSaveColor;

public class PrefSelectHighlight extends Dialog implements OnClickListener {
    CheckBox allCb;
    Button b1;
    Button b2;
    RadioButton cb1;
    RadioButton cb2;
    RadioButton cb3;
    RadioButton cb4;
    View color1;
    View color2;
    View color3;
    View color4;
    CheckBox directCb;
    View hView2;
    View hView3;
    View hView4;
    boolean highlightAllChecked;
    String highlightAllWord;
    int highlightMode = -1;
    boolean isPdf;
    CheckBox magnifierCb;
    OnSelectColor onSelectColor;
    Resources res;
    View root;
    PrefSelectHighlight selfPref;
    CheckBox templateCb;

    public interface OnSelectColor {
        void selectColor(int i, boolean z);
    }

    public PrefSelectHighlight(Context context, OnSelectColor onAfterEditNote, boolean isPdf, int highlightMode, String highlightAllWord) {
        super(context, R.style.dialog_fullscreen);
        this.onSelectColor = onAfterEditNote;
        this.isPdf = isPdf;
        this.highlightMode = highlightMode;
        this.highlightAllWord = highlightAllWord;
        this.res = context.getResources();
        this.root = LayoutInflater.from(context).inflate(R.layout.pdf_highlight, null);
        setContentView(this.root);
        initView();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        A.setAlertDialogWidth(getWindow(), 0.75f, true);
        initView();
        A.setDialogNightState(this.root);
    }

    private void initView() {
        boolean z;
        boolean z2 = true;
        ((TextView) this.root.findViewById(R.id.titleB)).setText(R.string.highlight);
        this.root.findViewById(R.id.exitB).setVisibility(View.GONE);
        this.b1 = (Button) this.root.findViewById(R.id.okB);
        this.b2 = (Button) this.root.findViewById(R.id.cancelB);
        this.b1.setOnClickListener(this);
        this.b2.setOnClickListener(this);
        this.selfPref = this;
        this.color1 = this.root.findViewById(R.id.color1);
        this.color2 = this.root.findViewById(R.id.color2);
        this.color3 = this.root.findViewById(R.id.color3);
        this.color4 = this.root.findViewById(R.id.color4);
        this.hView2 = this.root.findViewById(R.id.View2);
        this.hView3 = this.root.findViewById(R.id.View3);
        this.hView4 = this.root.findViewById(R.id.View4);
        this.cb1 = (RadioButton) this.root.findViewById(R.id.radioButton1);
        this.cb2 = (RadioButton) this.root.findViewById(R.id.radioButton2);
        this.cb3 = (RadioButton) this.root.findViewById(R.id.radioButton3);
        this.cb4 = (RadioButton) this.root.findViewById(R.id.radioButton4);
        this.templateCb = (CheckBox) this.root.findViewById(R.id.checkBox1);
        this.directCb = (CheckBox) this.root.findViewById(R.id.CheckBox2);
        this.magnifierCb = (CheckBox) this.root.findViewById(R.id.CheckBox3);
        this.allCb = (CheckBox) this.root.findViewById(R.id.CheckBox4);
        if (T.isNull(this.highlightAllWord)) {
            this.allCb.setVisibility(View.GONE);
        } else {
            if (A.highlightAllItems.indexOf(this.highlightAllWord) != -1) {
                this.allCb.setChecked(true);
            }
            this.allCb.setText(Html.fromHtml(getContext().getString(R.string.highlight_all, new Object[]{"\"<b>" + this.highlightAllWord + "</b>\""})));
        }
        this.color1.setOnClickListener(this);
        this.color2.setOnClickListener(this);
        this.color3.setOnClickListener(this);
        this.color4.setOnClickListener(this);
        this.hView2.setOnClickListener(this);
        this.hView3.setOnClickListener(this);
        this.hView4.setOnClickListener(this);
        int mode = this.highlightMode == -1 ? A.highlightMode : this.highlightMode;
        RadioButton radioButton = this.cb1;
        if (mode == 0) {
            z = true;
        } else {
            z = false;
        }
        radioButton.setChecked(z);
        radioButton = this.cb2;
        if (mode == 1) {
            z = true;
        } else {
            z = false;
        }
        radioButton.setChecked(z);
        radioButton = this.cb3;
        if (mode == 2) {
            z = true;
        } else {
            z = false;
        }
        radioButton.setChecked(z);
        radioButton = this.cb4;
        if (mode == 3) {
            z = true;
        } else {
            z = false;
        }
        radioButton.setChecked(z);
        this.cb1.setOnClickListener(this);
        this.cb2.setOnClickListener(this);
        this.cb3.setOnClickListener(this);
        this.cb4.setOnClickListener(this);
        if (this.isPdf && this.highlightMode != -1) {
            radioButton = this.cb1;
            if (mode == 0) {
                z = true;
            } else {
                z = false;
            }
            radioButton.setEnabled(z);
            radioButton = this.cb2;
            if (mode == 1) {
                z = true;
            } else {
                z = false;
            }
            radioButton.setEnabled(z);
            radioButton = this.cb3;
            if (mode == 2) {
                z = true;
            } else {
                z = false;
            }
            radioButton.setEnabled(z);
            RadioButton radioButton2 = this.cb4;
            if (mode != 3) {
                z2 = false;
            }
            radioButton2.setEnabled(z2);
        }
        if (this.highlightMode != -1) {
            this.b2.setText(R.string.delete);
        }
        this.color1.setBackgroundColor(this.isPdf ? A.pdf_highlight_color : A.highlight_color1);
        this.color2.setBackgroundColor(A.underline_color);
        this.color3.setBackgroundColor(A.strikethrough_color);
        this.color4.setBackgroundColor(A.squiggly_color);
        this.hView2.setBackgroundColor(A.highlight_color2);
        this.hView3.setBackgroundColor(A.highlight_color3);
        this.hView4.setBackgroundColor(A.highlight_color4);
        this.directCb.setChecked(A.directHighlight);
        this.directCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                A.directHighlight = isChecked;
            }
        });
        this.magnifierCb.setChecked(A.showMagnifier);
        this.magnifierCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                A.showMagnifier = isChecked;
            }
        });
        this.templateCb.setChecked(A.showColorTemplate);
        this.templateCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (A.showColorTemplate != isChecked) {
                    A.showColorTemplate = isChecked;
                    ActivityTxt.selfPref.colorTemplate.setVisibility(A.showColorTemplate ? 0 : 8);
                    ActivityTxt.selfPref.updateHBarHeight(A.showColorTemplate ? -A.d(70.0f) : A.d(70.0f));
                    ActivityTxt.selfPref.initColorTemplateEvents();
                }
            }
        });
    }

    public void dismiss() {
        A.setSystemUiVisibility(true);
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v == this.b1) {
            //cb2
            int mode = this.cb2.isChecked() ? 1 : this.cb3.isChecked() ? 2 : this.cb4.isChecked() ? 3 : 0;
            if (!T.isNull(this.highlightAllWord)) {
                StringBuilder append = new StringBuilder().append(mode).append("|");
                int i = mode == 0 ? A.highlight_color1 : mode == 1 ? A.underline_color : mode == 2 ? A.strikethrough_color : A.squiggly_color;
                String value = append.append(i).toString();
                int i2 = A.highlightAllItems.indexOf(this.highlightAllWord);
                if (this.allCb.isChecked()) {
                    if (i2 == -1) {
                        A.highlightAllItems.add(this.highlightAllWord);
                        A.highlightAllProperties.add(value);
                        A.updateHighlightAllItem(this.highlightAllWord, value);
                    } else if (!((String) A.highlightAllProperties.get(i2)).equals(value)) {
                        A.highlightAllProperties.set(i2, value);
                        A.updateHighlightAllItem(this.highlightAllWord, value);
                    }
                } else if (i2 != -1) {
                    A.deleteHighlightAllItem(this.highlightAllWord);
                }
            }
            if (this.highlightMode == -1 || !this.isPdf) {
                A.highlightMode = mode;
            }
            this.onSelectColor.selectColor(mode, false);
            cancel();
        }
        if (v == this.b2) {
            if (this.highlightMode != -1) {
                this.onSelectColor.selectColor(0, true);
                cancel();
            } else {
                cancel();
            }
        }
        if (v == this.color1) {
            new ColorDialog(this.selfPref.getContext(), this.res.getString(R.string.highlight_color), true, this.isPdf ? A.pdf_highlight_color : A.highlight_color1, new OnSaveColor() {
                public void getColor(int color) {
                    if (PrefSelectHighlight.this.isPdf) {
                        A.pdf_highlight_color = color;
                    } else {
                        A.highlight_color1 = color;
                    }
                    PrefSelectHighlight.this.color1.setBackgroundColor(color);
                    PrefSelectHighlight.this.setChecked(PrefSelectHighlight.this.cb1);
                }
            }).show();
        }
        if (v == this.color2) {
            new ColorDialog(this.selfPref.getContext(), this.res.getString(R.string.underline), true, A.underline_color, new OnSaveColor() {
                public void getColor(int color) {
                    A.underline_color = color;
                    PrefSelectHighlight.this.color2.setBackgroundColor(color);
                    PrefSelectHighlight.this.setChecked(PrefSelectHighlight.this.cb2);
                }
            }).show();
        }
        if (v == this.color3) {
            new ColorDialog(this.selfPref.getContext(), this.res.getString(R.string.strikethrough), true, A.strikethrough_color, new OnSaveColor() {
                public void getColor(int color) {
                    A.strikethrough_color = color;
                    PrefSelectHighlight.this.color3.setBackgroundColor(color);
                    PrefSelectHighlight.this.setChecked(PrefSelectHighlight.this.cb3);
                }
            }).show();
        }
        if (v == this.color4) {
            new ColorDialog(this.selfPref.getContext(), this.res.getString(R.string.squiggly_underline), true, A.squiggly_color, new OnSaveColor() {
                public void getColor(int color) {
                    A.squiggly_color = color;
                    PrefSelectHighlight.this.color4.setBackgroundColor(color);
                    PrefSelectHighlight.this.setChecked(PrefSelectHighlight.this.cb4);
                }
            }).show();
        }
        if (v == this.hView2 || v == this.hView3 || v == this.hView4) {
            int oldColor = this.cb1.isChecked() ? this.isPdf ? A.pdf_highlight_color : A.highlight_color1 : this.cb2.isChecked() ? A.underline_color : this.cb3.isChecked() ? A.strikethrough_color : A.squiggly_color;
            int replaceColor = v == this.hView2 ? A.highlight_color2 : v == this.hView3 ? A.highlight_color3 : A.highlight_color4;
            v.setBackgroundColor(oldColor);
            if (v == this.hView2) {
                A.highlight_color2 = oldColor;
            } else if (v == this.hView3) {
                A.highlight_color3 = oldColor;
            } else {
                A.highlight_color4 = oldColor;
            }
            if (this.cb1.isChecked()) {
                if (this.isPdf) {
                    A.pdf_highlight_color = replaceColor;
                } else {
                    A.highlight_color1 = replaceColor;
                }
                this.color1.setBackgroundColor(replaceColor);
            } else if (this.cb2.isChecked()) {
                A.underline_color = replaceColor;
                this.color2.setBackgroundColor(replaceColor);
            } else if (this.cb3.isChecked()) {
                A.strikethrough_color = replaceColor;
                this.color3.setBackgroundColor(replaceColor);
            } else if (this.cb4.isChecked()) {
                A.squiggly_color = replaceColor;
                this.color4.setBackgroundColor(replaceColor);
            }
        }
        if (v == this.cb1) {
            setChecked(this.cb1);
        }
        if (v == this.cb2) {
            setChecked(this.cb2);
        }
        if (v == this.cb3) {
            setChecked(this.cb3);
        }
        if (v == this.cb4) {
            setChecked(this.cb4);
        }
    }

    protected void setChecked(RadioButton cb) {
        this.cb1.setChecked(false);
        this.cb2.setChecked(false);
        this.cb3.setChecked(false);
        this.cb4.setChecked(false);
        cb.setChecked(true);
    }
}