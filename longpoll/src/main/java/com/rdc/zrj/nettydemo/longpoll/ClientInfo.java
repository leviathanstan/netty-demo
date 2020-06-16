package com.rdc.zrj.nettydemo.longpoll;

import java.io.Serializable;

public class ClientInfo implements Serializable {

    private static final long serialVersionUID = 2758664189824313365L;
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "clientId='" + clientId + '\'' +
                '}';
    }
}
