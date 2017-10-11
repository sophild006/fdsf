package com.flyersoft.staticlayout;

import android.support.v4.view.InputDeviceCompat;

public class LZXCoder {
    public static final int ALIGNED_OFFSET_TREE_BITS = 3;
    public static int[] EXTRA_BITS = new int[]{0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17};
    public static final int MIN_MATCH = 2;
    public static final int NUM_ALIGNED_OFFSET = 8;
    public static final int NUM_CHARS = 256;
    public static final int NUM_LENGTH_FOOT_VALUE = 249;
    public static final int NUM_PRIMARY_LENGTHS = 7;
    public static int[] POSITION_BASE = new int[]{0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 128, 192, 256, 384, 512, 768, 1024, 1536, 2048, 3072, 4096, 6144, 8192, 12288, 16384, 24576, 32768, 49152, 65536, 98304, 131072, 196608, 262144, 393216, 524288, 655360, 786432, 917504, 1048576, 1179648, 1310720, 1441792, 1572864, 1703936, 1835008, 1966080, 2097152};
    public static final int PRE_TREE_BITS = 4;
    public static final int PRE_TREE_ENTRIES = 20;
    public int blockSize;
    private int curpos;
    private int endpos;
    public long fileTranslationSize;
    public boolean headerBit;
    public int[] lengthTreeLen;
    public int mainTreeElements;
    public int[] mainTreeLen;
    public int numOfUncompressedBytes;
    public int numPositionSlots;
    private int offset;
    int r0;
    int r1;
    int r2;
    public int winSize;


}