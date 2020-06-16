package com.rdc.zrj.nettydemo.im.client.handle;

import com.rdc.zrj.nettydemo.im.client.ClientService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new ObjectEncoder());
    }

    public void removeHandler(Channel channel) {
        if (channel.pipeline().get(IdleStateHandler.class.getSimpleName()) != null) {
            channel.pipeline().remove(IdleStateHandler.class.getSimpleName());
        }
        if (channel.pipeline().get(ClientHeartbeatHandle.class.getSimpleName()) != null) {
            channel.pipeline().remove(ClientHeartbeatHandle.class.getSimpleName());
        }
    }

    public void addHandler(Channel channel, ClientService service) {
        channel.pipeline()
                .addLast(new IdleStateHandler(10, 0, 0))
                .addLast(new ClientHeartbeatHandle(service));
    }
}
