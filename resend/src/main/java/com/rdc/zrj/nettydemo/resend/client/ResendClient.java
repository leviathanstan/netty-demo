package com.rdc.zrj.nettydemo.resend.client;

import com.rdc.zrj.nettydemo.resend.aid.AidServer;
import com.rdc.zrj.nettydemo.resend.client.executor.ExecutorFactory;
import com.rdc.zrj.nettydemo.resend.client.handle.ChannelHandlerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class ResendClient {
    private final int port;
    private final String host;

    public ResendClient(String host, int port){
        this.port  = port;
        this.host = host;
    }

    public static void main(String[] args) {
        new ResendClient("127.0.0.1", 8089).start();
    }

    private void start(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelHandlerInitializer());
            ChannelFuture future = bootstrap.connect().sync();
            //启动一个http服务，辅助进行消息发送测试
            new AidServer(ExecutorFactory.getClientInstance(future.channel())).start();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
