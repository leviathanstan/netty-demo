package com.rdc.zrj.nettydemo.im.client;

import com.rdc.zrj.nettydemo.im.model.Msg;
import io.netty.channel.ChannelFuture;

public interface ClientService {

    /**
     * blocking
     */
    boolean connect() throws InterruptedException;

    ChannelFuture send(Msg msg);

    boolean isAlive();

    /**
     * just close the channel
     */
    void close();

    /**
     * shutdown loopGroup
     */
    boolean shutdown();

    /**
     * no blocking
     */
    void reconnect();

}
