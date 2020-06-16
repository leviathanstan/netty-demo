package com.rdc.zrj.nettydemo.longpollretry.client;

import com.rdc.zrj.nettydemo.longpollretry.client.handler.ClientHeartbeatHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class Client {
    private final int port;
    private final String host;
    public EventLoopGroup group;

    public Client(String host, int port){
        this.port  = port;
        this.host = host;
    }

    public static void main(String[] args) {
        new Client("127.0.0.1", 8089).start();
    }

    private void start(){
        group = new NioEventLoopGroup();
        connect(group);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                //ignore
            }
        }));
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect(EventLoopGroup eventLoopGroup) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(0, 10, 0))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new ClientHeartbeatHandle(Client.this));
                        }
                    });
            //调用sync来保证connect失败时获得异常
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
