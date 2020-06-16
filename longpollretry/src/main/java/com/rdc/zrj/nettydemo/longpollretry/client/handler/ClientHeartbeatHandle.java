package com.rdc.zrj.nettydemo.longpollretry.client.handler;

import com.rdc.zrj.nettydemo.longpollretry.client.Client;
import com.rdc.zrj.nettydemo.longpollretry.model.Msg;
import com.rdc.zrj.nettydemo.longpollretry.model.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ClientHeartbeatHandle extends SimpleChannelInboundHandler<Msg> {

    private Msg msg = new Msg(MsgType.HEARTBEAT, null);
    private Client client;
    private Logger log = Logger.getAnonymousLogger();

    public ClientHeartbeatHandle(Client client) {
        this.client = client;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Msg in) {
        System.out.println(new Date() + " receive:" + msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) throws Exception{
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
                        context.channel().disconnect();
                        reconnect(context);
                    }
                });
            }
        }
        super.userEventTriggered(context, event);
    }

    /**
     * 在channel被关闭时，执行重连【较为鸡肋】
     * channelInactive的触发场景为：对端主动断开连接（发送FIN，如ctrl+c关闭服务），在断电的情况下是无法被触发的
     * 有用的使用场景为：只有客户端能够关闭连接，不允许服务度主动断开（因为如果服务端是工作完成后主动关闭的连接，此时客户端不应该再重连）
     * 这种情况下，手动关闭服务端后马上重启，此时客户端会继续重建连接。
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel inactive, try to reconnect");
        reconnect(ctx);
        super.channelInactive(ctx);
    }

    private void reconnect(ChannelHandlerContext ctx) {
        final EventLoop eventLoop = ctx.channel().eventLoop();
        /*
        延迟一段时间后重新连接（等待服务端重启）
        这里仍然使用了原来的EventLoopGroup来作为新bootstrap的线程池，
        要注意的是：这里重连的任务是交给当前channel所属线程来执行的，
        如果连接过程中有阻塞操作(sync)，有可能会引发死锁异常：
        如EventLoopGroup中只有一个线程，即当前channel的线程，那么调用sync时，当前线程被阻塞等待将要被自己执行的任务，导致死锁
         */
        eventLoop.schedule(() -> client.connect(client.group), 5L, TimeUnit.SECONDS);
    }
}
