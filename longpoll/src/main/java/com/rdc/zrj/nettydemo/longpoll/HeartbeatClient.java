package com.rdc.zrj.nettydemo.longpoll;

import com.rdc.zrj.nettydemo.longpoll.handle.ClientHeartbeatHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;

public class HeartbeatClient {
    private final int port;
    private final String host;

    public HeartbeatClient(String host, int port){
        this.port  = port;
        this.host = host;
    }

    public static void main(String[] args) {
        new HeartbeatClient("127.0.0.1", 8089).start();
    }

    private void start(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(0, 5, 0))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ClientHeartbeatHandle());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
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
