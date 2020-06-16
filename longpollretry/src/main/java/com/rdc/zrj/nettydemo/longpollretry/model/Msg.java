package com.rdc.zrj.nettydemo.longpollretry.model;

import java.io.Serializable;

public class Msg implements Serializable {

    private static final long serialVersionUID = 5058408032778571392L;

    public Msg() {}

    public Msg(MsgType type, String body) {
        this.type = type;
        this.body = body;
    }

    private MsgType type;
    private String body;

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "type=" + type +
                ", body='" + body + '\'' +
                '}';
    }
}
