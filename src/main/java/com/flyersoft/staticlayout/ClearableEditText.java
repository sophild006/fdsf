package com.flyersoft.staticlayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.flyersoft.books.A;
import com.flyersoft.books.T;

public class ClearableEditText extends EditText implements OnTouchListener, OnFocusChangeListener {
    private Activity act;
    private OnFocusChangeListener f;
    private OnTouchListener l;
    private Drawable xD;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    public void setActivity(Activity act) {
        this.act = act;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getX() > ((float) ((getWidth() - getPaddingRight()) - this.xD.getIntrinsicWidth()))) {
                if (event.getAction() != 1) {
                    return true;
                }
                setText("");
                if (this.act == null) {
                    return true;
                }
                InputMethodManager imm = (InputMethodManager) this.act.getSystemService("input_method");
                if (imm == null) {
                    return true;
                }
                imm.showSoftInput(this, 0);
                return true;
            }
        }
        return this.l != null ? this.l.onTouch(v, event) : false;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        boolean z = false;
        if (hasFocus) {
            if (!T.isNull(getText())) {
                z = true;
            }
            setClearIconVisible(z);
        } else {
            setClearIconVisible(false);
        }
        if (this.f != null) {
            this.f.onFocusChange(v, hasFocus);
        }
    }

    private void init() {
        this.xD = getCompoundDrawables()[2];
        if (this.xD == null) {
            this.xD = getResources().getDrawable(17301610);
        }
        this.xD.setBounds(0, 0, this.xD.getIntrinsicWidth(), this.xD.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ClearableEditText.this.setClearIconVisible(!T.isNull(s));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                ClearableEditText.this.setClearIconVisible(!T.isNull((CharSequence) s));
            }
        });
    }

    protected void setClearIconVisible(boolean visible) {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], visible ? this.xD : null, getCompoundDrawables()[3]);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getAction() == 1) {
            A.setSystemUiVisibility(true);
        }
        return super.onKeyPreIme(keyCode, event);
    }
}