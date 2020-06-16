package com.rdc.zrj.nettydemo.websocket;

import com.rdc.zrj.nettydemo.websocket.initializer.SecureChatServerInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * @author asce
 * @date 2019/7/7
 */
public class SecureChatServer extends ChatServer {

    private final SslContext context;

    public SecureChatServer(SslContext context){
        this.context = context;
    }

    @Override
    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group){
        return new SecureChatServerInitializer(group, context);
    }

    public static void main(String[] args) throws Exception{
        int port = 8089;
        SelfSignedCertificate certificate = new SelfSignedCertificate();
        SslContext context = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey()).build();
        final SecureChatServer endpoint = new SecureChatServer(context);
        ChannelFuture future = endpoint.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                endpoint.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }
}
