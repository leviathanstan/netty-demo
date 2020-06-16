package com.rdc.zrj.nettydemo.longpoll;

import com.rdc.zrj.nettydemo.longpoll.handle.ServerHeartbeatHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;

public class HeartbeatServer {

    private final int port;

    public HeartbeatServer(int port){
        this.port  = port;
    }

    public static void main(String[] args) {
        new HeartbeatServer(8089).start();
    }

    private void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // tcp套接字参数，一定时间内没有收到数据(default:2 hours)，TCP会自动发送一个活动探测数据包
                    // 所以在netty层实现长连接的意义在于：可以对长连接有更多的控制,如断线重连、连接维持时间控制等
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(0, 0, 10))
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new ServerHeartbeatHandle());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
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
