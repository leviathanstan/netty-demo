package com.rdc.zrj.nettydemo.im.client;

import com.rdc.zrj.nettydemo.im.client.executor.ExecutorFactory;
import com.rdc.zrj.nettydemo.im.client.executor.ResendExecutor;
import com.rdc.zrj.nettydemo.im.client.handle.ChannelHandlerInitializer;
import com.rdc.zrj.nettydemo.im.model.Msg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ClientServiceImpl implements ClientService {

    private String host = "127.0.0.1";
    private int port = 9091;
    private Channel channel;
    private EventLoopGroup group = new NioEventLoopGroup();
    private Logger logger = Logger.getAnonymousLogger();
    private ResendExecutor executor;
    private ChannelHandlerInitializer handlerInitializer = new ChannelHandlerInitializer();

    public ClientServiceImpl() {
    }

    public ClientServiceImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean connect() throws InterruptedException{
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(handlerInitializer);
            resetHandler();
            ChannelFuture future = bootstrap.connect().sync();
            this.channel = future.channel();
            this.executor = ExecutorFactory.getClientInstance(channel);
            //启动一个http服务，辅助进行消息发送测试
//            new AidServer(ExecutorFactory.getClientInstance(future.channel())).start();
        } catch (InterruptedException e) {
            logger.warning("interrupt while connecting!");
            throw e;
        }
        return true;
    }

    public void resetHandler() {
        handlerInitializer.removeHandler(channel);
        handlerInitializer.addHandler(channel, this);
    }

    @Override
    public void reconnect() {
        group.schedule(this::connect, 5L, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        executor.close();
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    public boolean shutdown() {
        if (channel != null) {
            channel.close();
        }
        try {
            group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            //ignore
        }
        return true;
    }

    @Override
    public boolean isAlive() {
        return channel.isActive();
    }

    @Override
    public ChannelFuture send(Msg msg) {
        return executor.send(msg);
    }

    public static void main(String[] args) {

    }
}
