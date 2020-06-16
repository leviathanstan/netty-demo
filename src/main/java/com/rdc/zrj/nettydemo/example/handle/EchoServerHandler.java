package com.rdc.zrj.nettydemo.example.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * @author asce
 * @date 2019/7/5
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /*
    为什么不使用SimpleChannelInboundHandler？
    因为SimpleChannelInboundHandler在ChannelRead0后会释放资源
    但整个handler需要把客户端发送的信息再发送回客户端，而且写操作是异步的，所以不能让read操作自动释放资源。
    写操作结束后会自动释放资源
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf in = (ByteBuf) msg;
        System.out.println(in.toString(CharsetUtil.UTF_8));
        ctx.write(in);
        in.retain();
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }

}
