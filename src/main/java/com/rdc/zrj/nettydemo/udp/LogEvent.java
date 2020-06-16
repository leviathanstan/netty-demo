package com.rdc.zrj.nettydemo.udp;

import sun.rmi.runtime.Log;

import java.net.InetSocketAddress;

/**
 * @author asce
 * @date 2019/7/7
 */
public class LogEvent {

    public static final byte SEPARATOR = (byte) ':';
    private final InetSocketAddress source;
    private final String logfile;
    private final String msg;
    private final long received;

    public LogEvent(String logfile, String msg){
        this(null, -1, logfile, msg);
    }

    public LogEvent(InetSocketAddress source, long received, String logfile, String msg){
        this.source = source;
        this.received = received;
        this.logfile = logfile;
        this.msg = msg;
    }

    public InetSocketAddress getSourceA(){
        return source;
    }

    public String getLogfile(){
        return logfile;
    }

    public String getMsg(){
        return msg;
    }

    public long getReceivedTimestamp(){
        return received;
    }
}
