package com.rdc.zrj.nettydemo.longpollretry.server.handler;

import com.rdc.zrj.nettydemo.longpollretry.model.Msg;
import com.rdc.zrj.nettydemo.longpollretry.model.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Random;
import java.util.logging.Logger;

public class ServerHeartbeatHandle extends SimpleChannelInboundHandler<Msg> {

    private Msg msg = new Msg(MsgType.HEARTBEAT, null);
    private Logger log = Logger.getAnonymousLogger();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            //一段时间没收到和发送数据，server主动心跳
            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                System.out.println("heartbeat...");
                ctx.writeAndFlush(msg).addListener(future -> {
                    //心跳失败，直接断开连接（可改为再尝试几次）
                    if (!future.isSuccess()
                            //模拟宕机
                            || (new Random().nextInt(10) % 3 == 0)) {
                        log.info("heartbeat fail, disconnect.");
                        if (future.cause() != null) {
                            log.info("cause:" + future.cause().getMessage());
                        }
                        ctx.channel().disconnect();
                    }
                });
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) {
        System.out.println(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("channel inactive");
        ctx.fireChannelInactive();
    }
}
