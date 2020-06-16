package com.rdc.zrj.nettydemo.resend.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Msg implements Serializable {

    private static final long serialVersionUID = 5058408032778571392L;

    private MsgType type;
    private String body;
    private int resendCount;
}
