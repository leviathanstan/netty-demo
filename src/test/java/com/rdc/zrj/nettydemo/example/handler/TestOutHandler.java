package com.rdc.zrj.nettydemo.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author asce
 * @date 2019/7/13
 */
public class TestOutHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise){
        ByteBuf in = (ByteBuf) msg;
        ctx.write(in);
        promise.setSuccess();
    }
}
