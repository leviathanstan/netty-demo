package com.rdc.zrj.nettydemo.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


/**
 * @author asce
 * @date 2019/7/5
 */
public class TestSimpleInHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("..");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object in){
        if (in instanceof ByteBuf){
            int msgId = ((ByteBuf) in).readInt();
            System.out.println(msgId);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello netty", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
