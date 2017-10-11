package com.flyersoft.staticlayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MyBufferedReader extends BufferedReader {
    public static final String spaceChars = " \n\r\t";
    public static final String spaceOrTagEndChars = " \n\r\t>";
    public CharArray charArray = new CharArray();

    public MyBufferedReader(Reader reader) {
        super(reader);
    }

    public MyBufferedReader(Reader reader, int size) {
        super(reader, size);
    }

    public int seekAfter(char c) throws IOException {
        int i;
        do {
            i = read();
            if (i < '\u0000') {
                break;
            }
        } while (i != c);
        return i;
    }

    public int readUntil(char c, CharArray ca) throws IOException {
        ca.clear();
        while (true) {
            mark(1);
            int i = read();
            if (i < 0 || c == i) {
                break;
            } else {
                ca.append(i);
            }
        }
        reset();
        return ca.len;
    }

    public String readUntil(char c) throws IOException {
        readUntil(c, this.charArray);
        return this.charArray.toString();
    }

    public int readUntil(String chars, CharArray ca) throws IOException {
        ca.clear();
        while (true) {
            mark(1);
            int i = read();
            if (i < 0 || chars.indexOf(i) >= 0) {
                break;
            } else {
                ca.append(i);
            }
        }
        reset();
        return ca.len;
    }

    public String readUntil(String chars) throws IOException {
        readUntil(chars, this.charArray);
        return this.charArray.toString();
    }

    public String readUntilSpaces() throws IOException {
        return readUntil(spaceChars);
    }

    public void skipSpace() throws IOException {
        int i;
        do {
            mark(1);
            i = read();
            if (i < 0) {
                break;
            }
        } while (spaceChars.indexOf(i) >= 0);
        reset();
    }

    public int peek() throws IOException {
        mark(1);
        int i = read();
        reset();
        return i;
    }

    public boolean eof() throws IOException {
        return peek() == -1;
    }

    public int readNextXMLTag(CharArray ca) throws IOException {
        if (seekAfter('<') < 0) {
            return 0;
        }
        return readUntil(spaceOrTagEndChars, ca);
    }

    public String readNextXMLTag() throws IOException {
        if (seekAfter('<') < 0) {
            return null;
        }
        return readUntil(spaceOrTagEndChars);
    }
}