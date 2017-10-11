package com.flyersoft.staticlayout;

public class ChmParseException extends RuntimeException {
    public ChmParseException(String s) {
        super(s);
    }

    public ChmParseException(Exception e) {
        super(e);
    }

    public ChmParseException(String s, Exception e) {
        super(s, e);
    }
}