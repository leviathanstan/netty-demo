package com.rdc.zrj.nettydemo.im.client.handle;

import com.rdc.zrj.nettydemo.im.client.ClientService;
import com.rdc.zrj.nettydemo.im.model.Msg;
import com.rdc.zrj.nettydemo.im.model.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class ClientHeartbeatHandle extends SimpleChannelInboundHandler<Msg> {

    private Msg msg = new Msg(MsgType.HEARTBEAT, null);
    private Logger log = Logger.getAnonymousLogger();
    private ClientService client;

    public ClientHeartbeatHandle(ClientService client) {
        this.client = client;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Msg in) {
        System.out.println(new Date() + " receive:" + msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) throws Exception {
        if (event instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) event;
            //一段时间没发送数据，主动发送心跳
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("heartbeat...");
                //心跳失败，尝试重连
                context.writeAndFlush(msg).addListener(future -> {
                    if (!future.isSuccess()
                            //模拟宕机
                            || (new Random().nextInt(10) % 3 == 0)) {
                        log.info("heartbeat fail, reconnect");
                        client.close();
                        client.reconnect();
                    }
                });
            }
        }
        super.userEventTriggered(context, event);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
