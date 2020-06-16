package com.rdc.zrj.nettydemo.im.client.executor;

import com.rdc.zrj.nettydemo.im.client.exception.MethodNotSupportedException;
import com.rdc.zrj.nettydemo.im.model.Msg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class ResendClientExecutor extends ResendExecutorImpl {

    private Channel channel;

    protected ResendClientExecutor(Channel channel) {
        this.channel = channel;
    }

    @Override
    public ChannelFuture send(Msg msg) {
        return super.send(channel, msg);
    }

    @Override
    public ChannelFuture send(Channel channel, Msg msg) {
        throw new MethodNotSupportedException("Unsupported Operation");
    }
}
