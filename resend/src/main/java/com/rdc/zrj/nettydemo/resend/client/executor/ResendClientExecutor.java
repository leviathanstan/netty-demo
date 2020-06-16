package com.rdc.zrj.nettydemo.resend.client.executor;

import com.rdc.zrj.nettydemo.resend.model.Msg;
import com.rdc.zrj.nettydemo.resend.client.exception.MethodNotSupportedException;
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
