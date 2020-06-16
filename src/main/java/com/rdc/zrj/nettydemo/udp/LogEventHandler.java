package com.rdc.zrj.nettydemo.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author asce
 * @date 2019/7/7
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception{
        StringBuilder string = new StringBuilder();
        string.append(event.getReceivedTimestamp());
        string.append("[");
        string.append(event.getSourceA().toString());
        string.append("[]");
        string.append(event.getLogfile());
        string.append(event.getMsg());
        System.out.println(string.toString());
    }
}
