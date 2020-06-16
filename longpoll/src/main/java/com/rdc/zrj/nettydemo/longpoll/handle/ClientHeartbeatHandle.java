package com.rdc.zrj.nettydemo.longpoll.handle;

import com.rdc.zrj.nettydemo.longpoll.ClientInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.UUID;

public class ClientHeartbeatHandle extends SimpleChannelInboundHandler<ByteBuf> {

    private static ClientInfo info = new ClientInfo();
    static {
        info.setClientId(UUID.randomUUID().toString());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println(new Date() + " receive:" + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) throws Exception{
        if (event instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) event;
            //一段时间没发送数据，主动发送心跳
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                context.writeAndFlush(info);
            }
        }
        super.userEventTriggered(context, event);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("inactive");
    }
}
