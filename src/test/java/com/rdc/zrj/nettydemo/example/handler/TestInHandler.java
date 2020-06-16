package com.rdc.zrj.nettydemo.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author asce
 * @date 2019/7/13
 */
public class TestInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException{
        ByteBuf in = (ByteBuf) msg;
        System.out.println(in.toString(CharsetUtil.UTF_8));
////        ctx.write(in);
////        in.retain();
//        ctx.write(Unpooled.copiedBuffer("give you", CharsetUtil.UTF_8));
//        ctx.fireChannelRead(msg);
        ChannelFuture future = ctx.channel().writeAndFlush("hello");
        future.sync();
//        future.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if (future.isSuccess()) {
//                    Thread.sleep(1000);
//                    System.out.println("success");
//                }
//            }
//        });
//        System.out.println("异步");

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);//.addListener(ChannelFutureListener.CLOSE);
    }
}
