package com.flyersoft.staticlayout;

public class Logger {
    public static Logger logProtoType = new Logger();
    public String name;
    private long previousTime;
    private long startTime;

    public static Logger getLogger(String name) {
        return logProtoType.makeCopy(name);
    }

    protected Logger() {
    }

    protected Logger(String _name) {
        this.name = _name;
    }

    public void log(String message) {
        System.out.println("[" + this.name + "]: " + message);
    }

    public Logger makeCopy(String _name) {
        return new Logger(_name);
    }

    public void logTimeStart(String message) {
        log(message);
        this.previousTime = System.currentTimeMillis();
        this.startTime = this.previousTime;
    }

    public void logTime(String message) {
        long curTime = System.currentTimeMillis();
        log(message + "; timeDelta=" + (curTime - this.previousTime) + "; time=" + (curTime - this.startTime));
        this.previousTime = curTime;
    }
}