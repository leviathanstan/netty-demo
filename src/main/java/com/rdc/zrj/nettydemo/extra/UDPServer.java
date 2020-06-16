package com.rdc.zrj.nettydemo.extra;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

/**
 * @author asce
 * 代码清单9.6
 * @since 2020/2/21
 */
public class UDPServer {

    public static void main(String[] args) {
        Bootstrap b = new Bootstrap();
        b.group(new NioEventLoopGroup()).channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
                            throws Exception {
                        // do something with the packet
                    }
                });
        ChannelFuture f = b.bind(new InetSocketAddress(0));
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("Channel bound");
                } else {
                    System.err.println("Bound attempt failed");
                    future.cause().printStackTrace();
                }
            }
        });
    }
}
