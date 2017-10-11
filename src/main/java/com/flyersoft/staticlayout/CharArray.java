package com.flyersoft.staticlayout;

public class CharArray {
    public char[] buffer;
    public int len;

    public CharArray(int size) {
        this.buffer = new char[size];
    }

    public CharArray() {
        this(1024);
    }

    public CharArray clear() {
        this.len = 0;
        return this;
    }

    public void setLength(int _len) {
        this.len = _len;
    }

    public void append(int c) {
        char[] cArr = this.buffer;
        int i = this.len;
        this.len = i + 1;
        cArr[i] = (char) c;
    }

    public String toString() {
        return new String(this.buffer, 0, this.len);
    }

    public void toLowerCase() {
        for (int i = 0; i < this.len; i++) {
            int c = this.buffer[i];
            if (c >= 65 && c <= 90) {
                this.buffer[i] = (char) ((c + 97) - 65);
            }
        }
    }

    public boolean equals(String str) {
        if (this.len != str.length()) {
            return false;
        }
        for (int i = 0; i < this.len; i++) {
            if (this.buffer[i] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public void copyFrom(CharArray ca) {
        this.len = ca.len;
        for (int i = 0; i < this.len; i++) {
            this.buffer[i] = ca.buffer[i];
        }
    }

    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < this.len; i++) {
            hash = ((hash << 5) - hash) + this.buffer[i];
        }
        return hash;
    }

    public CharArray append(String str) {
        return a(str);
    }

    public CharArray a(String str) {
        for (int i = 0; i < str.length(); i++) {
            char[] cArr = this.buffer;
            int i2 = this.len;
            this.len = i2 + 1;
            cArr[i2] = str.charAt(i);
        }
        return this;
    }

    public CharArray append(CharArray ca) {
        return a(ca);
    }

    public CharArray a(CharArray ca) {
        for (int i = 0; i < ca.len; i++) {
            char[] cArr = this.buffer;
            int i2 = this.len;
            this.len = i2 + 1;
            cArr[i2] = ca.buffer[i];
        }
        return this;
    }

    public CharArray a(char c) {
        char[] cArr = this.buffer;
        int i = this.len;
        this.len = i + 1;
        cArr[i] = c;
        return this;
    }

    public void convertFrom(int i) {
    }

    public int indexOf(char c) {
        for (int i = 0; i < this.len; i++) {
            if (this.buffer[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(String str) {
        int i = 0;
        while (i < this.len - str.length()) {
            int j = 0;
            while (j < str.length() && this.buffer[i + j] == str.charAt(j)) {
                j++;
            }
            if (j == str.length()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public CharArray replace(String strFrom, String strTo) {
        return indexOf(strFrom) < 0 ? this : new CharArray().a(toString().replace(strFrom, strTo));
    }
}