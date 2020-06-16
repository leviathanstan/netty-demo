package com.rdc.zrj.nettydemo.im.client.executor;

import com.rdc.zrj.nettydemo.im.model.Msg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public interface ResendExecutor {

    /**
     * 发送消息，服务端使用
     * @param channel   连接的channel
     * @param msg       要发送的实际消息
     * @return          Future
     */
    ChannelFuture send(Channel channel, Msg msg);

    /**
     * 发送消息，客户端使用
     * @param msg
     * @return
     */
    ChannelFuture send(Msg msg);

    public void close();
}
