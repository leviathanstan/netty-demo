package com.rdc.zrj.nettydemo.udp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.*;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * @author asce
 * @date 2019/7/7
 */
public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file){
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws Exception{
        Channel channel = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        while(true){
            long len = file.length();
            if (len < file.length()){
                pointer = len;
            } else if (len > pointer){
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null){
                    channel.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop(){
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception{
        int port = 8089;
        String path = "C:\\Users\\Asce\\Desktop\\Java\\netty-in-action\\chapter12\\src\\main\\resources\\index.html";
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", port), new File(path));
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }

}
