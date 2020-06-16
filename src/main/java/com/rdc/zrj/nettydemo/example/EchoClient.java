package com.rdc.zrj.nettydemo.example;

import com.rdc.zrj.nettydemo.example.handle.EchoClientHandler;
import com.rdc.zrj.nettydemo.example.handle.EchoClientOutHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author asce
 * @date 2019/7/5
 */
public class EchoClient {
    private final int port;
    private final String host;

    public EchoClient(String host, int port){
        this.port  = port;
        this.host = host;
    }

    public static void main(String[] args) {
        new EchoClient("127.0.0.1", 8089).start();
    }

    private void start(){
        final EchoClientHandler echoHandler = new EchoClientHandler();
        final EchoClientOutHandler outHandler = new EchoClientOutHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch){
                            ch.pipeline().addLast(echoHandler);
//                            ch.pipeline().addFirst(outHandler);
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
