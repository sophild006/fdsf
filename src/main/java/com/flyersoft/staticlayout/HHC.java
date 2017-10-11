package com.flyersoft.staticlayout;

import com.flyersoft.books.A;
import com.flyersoft.books.T;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HHC {
    private static Logger logger = Logger.getLogger("HHC");
    public ArrayList<HChapter> hChapters = new ArrayList();
    private String hhcIndexHTML;
    private List<IndexEntry> indexes;

    public static class HChapter {
        public String filename = "";
        public boolean hasSubChapter = false;
        public int indent = 0;
        public String name = "";
        public long size = 0;
    }

    public static class IndexEntry {
        public int pathHash;
        public long position;

        public String toString() {
            return String.format("pathHash=%x;position=%x", new Object[]{Integer.valueOf(this.pathHash), Long.valueOf(this.position)});
        }
    }

    public HHC(MyBufferedReader br, String _hhcIndexHTML, String hhcIndexIdx) throws IOException {
        logger.logTimeStart("***************(1)HHC: start");
        this.hhcIndexHTML = _hhcIndexHTML;
        this.indexes = convert(br, hhcIndexIdx);
        logger.logTime("******************(2)HHC: After convert");
        writeIndex(hhcIndexIdx);
        logger.logTime("******************(3)HHC: After writeIndex");
    }

    private List<IndexEntry> convert(MyBufferedReader br, String hhcIndexIdx) {
        OutputStream fout = T.getFileOutputStream(this.hhcIndexHTML);
        List<IndexEntry> hhcIndexes = null;
        try {
            hhcIndexes = HHCConverter.convertToTree(br, fout, this.hChapters);
        } catch (Exception e) {
            A.error(e);
        }
        if (fout != null) {
            try {
                fout.close();
            } catch (Exception e2) {
                A.error(e2);
            }
        }
        return hhcIndexes;
    }

    public void readIndex(String hhcIndexIdx) throws IOException {
        logger.logTimeStart("readIndex: start");
        File f = new File(hhcIndexIdx);
        int num = ((int) f.length()) / 8;
        this.indexes = new ArrayList(num);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
        for (int i = 0; i < num; i++) {
            IndexEntry ie = new IndexEntry();
            ie.pathHash = dis.readInt();
            ie.position = (long) dis.readInt();
            this.indexes.add(ie);
        }
        logger.logTime("readIndex: end");
        fis.close();
    }

    public void writeIndex(String hhcIndexIdx) {
        OutputStream fout = T.getFileOutputStream(hhcIndexIdx);
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(fout));
            for (IndexEntry ie : this.indexes) {
                dos.writeInt(ie.pathHash);
                dos.writeInt((int) ie.position);
            }
            dos.flush();
        } catch (Exception e) {
            A.error(e);
        }
        if (fout != null) {
            try {
                fout.close();
            } catch (Exception e2) {
                A.error(e2);
            }
        }
        logger.logTime("writeIndex: after close");
    }

    public int getFileId(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        int hash = path.hashCode();
        for (int i = 0; i < this.indexes.size(); i++) {
            if (((IndexEntry) this.indexes.get(i)).pathHash == hash) {
                return i;
            }
        }
        return -1;
    }

    public String getNextFile(String path) {
        int hash = path.hashCode();
        logger.logTimeStart("getNextFile: Start: hash=" + hash + "/" + Integer.toHexString(hash) + ";path=" + path);
        int currentId = getFileId(path);
        if (currentId < 0) {
            currentId = 0;
        }
        logger.logTime("getNextFile: after getID: currentId=" + currentId);
        int id = currentId;
        while (id < this.indexes.size()) {
            int idHash = ((IndexEntry) this.indexes.get(id)).pathHash;
            if (idHash == 0 || idHash == hash || id == currentId) {
                id++;
            } else {
                logger.logTime("getNextFile: done, id=" + id);
                return getFileNameById(id);
            }
        }
        logger.logTime("getNextFile: done, return null");
        return null;
    }

    public String getPreviousFile(String path) {
        int hash = path.hashCode();
        int currentId = getFileId(path);
        if (currentId < 0) {
            currentId = 0;
        }
        int id = currentId;
        while (id >= 0) {
            int idHash = ((IndexEntry) this.indexes.get(id)).pathHash;
            if (idHash != 0 && idHash != hash && id != currentId) {
                return getFileNameById(id);
            }
            id--;
        }
        return null;
    }

    public String getFileNameById(int id) {
        try {
            long position = ((IndexEntry) this.indexes.get(id)).position;
            ByteBufferFile bf = new ByteBufferFile(this.hhcIndexHTML);
            bf.seek(position);
            byte[] bytes = new byte[1024];
            int length = bf.readUntil(bytes, 62) - 1;
            bf.close();
            return new String(bytes, 0, length, "UTF-8");
        } catch (Exception e) {
            throw new ChmParseException(e);
        }
    }
}