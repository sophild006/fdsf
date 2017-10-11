package com.flyersoft.staticlayout;

import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class XmlUtils {
    public static void skipCurrentTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
        }
    }

    public static final int convertValueToList(CharSequence value, String[] options, int defaultValue) {
        if (value != null) {
            for (int i = 0; i < options.length; i++) {
                if (value.equals(options[i])) {
                    return i;
                }
            }
        }
        return defaultValue;
    }

    public static final boolean convertValueToBoolean(CharSequence value, boolean defaultValue) {
        boolean result = false;
        if (value == null) {
            return defaultValue;
        }
        if (value.equals("1") || value.equals("true") || value.equals("TRUE")) {
            result = true;
        }
        return result;
    }

    public static final int convertValueToInt(CharSequence charSeq, int defaultValue) {
        if (charSeq == null) {
            return defaultValue;
        }
        String nm = charSeq.toString();
        int sign = 1;
        int index = 0;
        int len = nm.length();
        int base = 10;
        if ('-' == nm.charAt(0)) {
            sign = -1;
            index = 0 + 1;
        }
        if ('0' == nm.charAt(index)) {
            if (index == len - 1) {
                return 0;
            }
            char c = nm.charAt(index + 1);
            if ('x' == c || 'X' == c) {
                index += 2;
                base = 16;
            } else {
                index++;
                base = 8;
            }
        } else if ('#' == nm.charAt(index)) {
            index++;
            base = 16;
        }
        return Integer.parseInt(nm.substring(index), base) * sign;
    }

    public static final int convertValueToUnsignedInt(String value, int defaultValue) {
        return value == null ? defaultValue : parseUnsignedIntAttribute(value);
    }

    public static final int parseUnsignedIntAttribute(CharSequence charSeq) {
        String value = charSeq.toString();
        int index = 0;
        int len = value.length();
        int base = 10;
        if ('0' == value.charAt(0)) {
            if (0 == len - 1) {
                return 0;
            }
            char c = value.charAt(1);
            if ('x' == c || 'X' == c) {
                index = 0 + 2;
                base = 16;
            } else {
                index = 0 + 1;
                base = 8;
            }
        } else if ('#' == value.charAt(0)) {
            index = 0 + 1;
            base = 16;
        }
        return (int) Long.parseLong(value.substring(index), base);
    }

    public static final void writeMapXml(Map val, OutputStream out) throws XmlPullParserException, IOException {

    }

    public static final void writeListXml(List val, OutputStream out) throws XmlPullParserException, IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "utf-8");
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeListXml(val, null, serializer);
        serializer.endDocument();
    }

    public static final void writeMapXml(Map val, String name, XmlSerializer out) throws XmlPullParserException, IOException {

    }

    public static final void writeListXml(List val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "list");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.size();
        for (int i = 0; i < N; i++) {
            writeValueXml(val.get(i), null, out);
        }
        out.endTag(null, "list");
    }

    public static final void writeByteArrayXml(byte[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {

    }

    public static final void writeIntArrayXml(int[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {

    }

    public static final void writeValueXml(Object v, String name, XmlSerializer out) throws XmlPullParserException, IOException {
    }

    public static final HashMap readMapXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, null);
        return (HashMap) readValueXml(parser, new String[1]);
    }

    public static final ArrayList readListXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, null);
        return (ArrayList) readValueXml(parser, new String[1]);
    }

    public static final HashMap readThisMapXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        HashMap map = new HashMap();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                Object val = readThisValueXml(parser, name);
                if (name[0] != null) {
                    map.put(name[0], val);
                } else {
                    throw new XmlPullParserException("Map value without name attribute: " + parser.getName());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return map;
                }
                throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final ArrayList readThisListXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        ArrayList list = new ArrayList();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                list.add(readThisValueXml(parser, name));
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return list;
                }
                throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final int[] readThisIntArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        return  null;
    }

    public static final Object readValueXml(XmlPullParser parser, String[] name) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != 2) {
            if (eventType == 3) {
                throw new XmlPullParserException("Unexpected end tag at: " + parser.getName());
            } else if (eventType == 4) {
                throw new XmlPullParserException("Unexpected text: " + parser.getText());
            } else {
                eventType = parser.next();
                if (eventType == 1) {
                    throw new XmlPullParserException("Unexpected end of document");
                }
            }
        }
        return readThisValueXml(parser, name);
    }

    private static final Object readThisValueXml(XmlPullParser parser, String[] name) throws XmlPullParserException, IOException {
        return  null;
    }

    public static final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            throw new XmlPullParserException("No start tag found");
        } else if (!parser.getName().equals(firstElementName)) {
            throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() + ", expected " + firstElementName);
        }
    }

    public static final void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                return;
            }
        } while (type != 1);
    }
}