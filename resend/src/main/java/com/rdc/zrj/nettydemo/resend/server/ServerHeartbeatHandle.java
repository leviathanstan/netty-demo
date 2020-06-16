package com.rdc.zrj.nettydemo.resend.server;

import com.rdc.zrj.nettydemo.resend.model.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHeartbeatHandle extends SimpleChannelInboundHandler<Msg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) {
        System.out.println(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("channel inactive");
        ctx.fireChannelInactive();
    }
}
