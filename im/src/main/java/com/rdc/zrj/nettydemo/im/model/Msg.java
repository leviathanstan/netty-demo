package com.rdc.zrj.nettydemo.im.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Msg implements Serializable {

    private static final long serialVersionUID = 5058408032778571392L;

    public Msg() {}

    public Msg(MsgType type, String body) {
        this.type = type;
        this.body = body;
    }

    private MsgType type;
    private String body;
    private int resendCount;
}
