package com.rdc.zrj.nettydemo.extra;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * @author asce
 * 代码清单9.6
 * @since 2020/2/21
 */
public class ChannalOption {

    public static void main(String[] args) {
        //创建属性键对象
        final AttributeKey<Integer> id = AttributeKey.valueOf("ID");
        //客户端引导对象
        Bootstrap b = new Bootstrap();
        //设置EventLoop，设置通道类型
        b.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                //设置ChannelHandler
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
                            throws Exception {
                        System.out.println("Reveived data");
                        msg.clear();
                    }

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        //通道注册后执行，获取属性值
                        Integer idValue = ctx.channel().attr(id).get();
                        System.out.println(idValue);
                        //do something with the idValue
                    }
                });
        //设置通道选项，在通道注册后或被创建后设置
        b.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        //设置通道属性
        b.attr(id, 123456);
        ChannelFuture f = b.connect("www.manning.com",80);
        f.syncUninterruptibly();
    }
}
