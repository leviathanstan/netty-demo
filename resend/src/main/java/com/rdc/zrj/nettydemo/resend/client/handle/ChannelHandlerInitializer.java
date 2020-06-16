package com.rdc.zrj.nettydemo.resend.client.handle;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new ObjectEncoder());
    }
}
