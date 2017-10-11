package com.flyersoft.staticlayout;

import android.support.v4.media.TransportMediator;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ByteBuffer {
    private long size;
    private long startOffset;
    private byte[] tempBuffer = new byte[2048];

    public abstract long getAbsoluteOffset();

    public abstract long getFullSize();

    public abstract byte readByte();

    public abstract int readBytes(byte[] bArr, int i, int i2);

    public abstract void seekAbsolute(long j);

    public void close() {
    }

    @Deprecated
    public byte[] readBytes(int length) {
        byte[] out = new byte[length];
        readBytes(out, 0, out.length);
        return out;
    }

    public int readBytes(byte[] out) {
        return readBytes(out, 0, out.length);
    }

    public void skip(int length) {
        seek(getOffset() + ((long) length));
    }

    public void resetSize() {
        this.startOffset = getAbsoluteOffset();
        this.size = getFullSize() - this.startOffset;
    }

    public void resetSize(long _size) {
        this.startOffset = getAbsoluteOffset();
        this.size = _size;
    }

    public void resetSize(long _startOffset, long _size) {
        seekAbsolute(_startOffset);
        resetSize(_size);
    }

    public long getOffset() {
        return getAbsoluteOffset() - this.startOffset;
    }

    public void seek(long pos) {
        seekAbsolute(this.startOffset + pos);
    }

    public long getSize() {
        return this.size;
    }

    public long getStartOffset() {
        return this.startOffset;
    }

    public long getRemainSize() {
        return this.size - getOffset();
    }

    public boolean hasMore() {
        return getRemainSize() > 0;
    }

    public void checkAvailableSize(int size) {
        if (((long) size) > getRemainSize()) {
            throw new ChmParseException("EOF exception");
        }
    }

    public String readString(int length) {
        readBytes(this.tempBuffer, 0, length);
        try {
            return new String(this.tempBuffer, 0, length, "UTF-8");
        } catch (Exception e) {
            throw new ChmParseException(e);
        }
    }

    public int readUntil(byte[] out, int terminate) {
        int i = 0;
        while (i < out.length) {
            int b = readByte();
            if (b == terminate) {
                break;
            }
            out[i] = (byte) b;
            i++;
        }
        return i;
    }

    public String readStringNT() {
        try {
            return new String(this.tempBuffer, 0, readUntil(this.tempBuffer, 0), "UTF-8");
        } catch (Exception e) {
            throw new ChmParseException(e);
        }
    }

    public int readWORD() {
        readBytes(this.tempBuffer, 0, 2);
        int value = 0;
        for (int i = 2 - 1; i >= 0; i--) {
            value = (value << 8) + (this.tempBuffer[i] & 255);
        }
        return value;
    }

    public int readDWORD() {
        readBytes(this.tempBuffer, 0, 4);
        int value = 0;
        for (int i = 4 - 1; i >= 0; i--) {
            value = (value << 8) + (this.tempBuffer[i] & 255);
        }
        return value;
    }

    public long readQWORD() {
        readBytes(this.tempBuffer, 0, 8);
        long value = 0;
        for (int i = 8 - 1; i >= 0; i--) {
            value = (value << 8) + ((long) (this.tempBuffer[i] & 255));
        }
        return value;
    }

    public int readENCINT() {
        int result = 0;
        byte b;
        do {
            result <<= 7;
            b = readByte();
            result += b & TransportMediator.KEYCODE_MEDIA_PAUSE;
        } while ((b & 128) != 0);
        return result;
    }

    public int copyTo(OutputStream out) {
        try {
            int totalLength = (int) getRemainSize();
            while (true) {
                int length = readBytes(this.tempBuffer);
                if (length <= 0) {
                    return totalLength;
                }
                out.write(this.tempBuffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        while (readBytes(this.tempBuffer) > 0) {
            try {
                sb.append(String.valueOf(this.tempBuffer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}