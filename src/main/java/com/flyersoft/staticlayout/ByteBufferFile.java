package com.flyersoft.staticlayout;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ByteBufferFile extends ByteBuffer {
    private RandomAccessFile file;

    public ByteBufferFile(String fileName) throws IOException {
        this(new RandomAccessFile(fileName, "r"));
    }

    public ByteBufferFile(RandomAccessFile _file) {
        this.file = _file;
        resetSize();
    }

    public long getFullSize() {
        try {
            return this.file.length();
        } catch (Exception e) {
            throw new ChmParseException(e);
        }
    }

    public long getAbsoluteOffset() {
        try {
            return this.file.getFilePointer();
        } catch (Exception e) {
            throw new ChmParseException(e);
        }
    }

    public void seekAbsolute(long pos) {
        try {
            this.file.seek(pos);
        } catch (Exception e) {
            throw new ChmParseException(e);
        }
    }

    public byte readByte() {
        try {
            return (byte) this.file.read();
        } catch (Exception e) {
            throw new ChmParseException(e);
        }
    }

    public int readBytes(byte[] out, int offset, int length) {
        try {
            return this.file.read(out, offset, length);
        } catch (Exception e) {
            throw new ChmParseException(e);
        }
    }

    public void close() {
        try {
            this.file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}