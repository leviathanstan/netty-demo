package com.rdc.zrj.nettydemo.longpoll.handle;

import com.rdc.zrj.nettydemo.longpoll.ClientInfo;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class ServerHeartbeatHandle extends SimpleChannelInboundHandler<ClientInfo> {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            //一段时间没收到和发送数据，server主动心跳
            if (idleStateEvent.state() == IdleState.ALL_IDLE){
                ctx.writeAndFlush(Unpooled.copiedBuffer("heartbeat", CharsetUtil.UTF_8));
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientInfo clientInfo) {
        System.out.println("receive:" + clientInfo);
    }
}
