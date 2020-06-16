package com.rdc.zrj.nettydemo.im.client.executor;

import com.rdc.zrj.nettydemo.im.model.Msg;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ResendExecutorImpl implements ResendExecutor {

    private Logger logger = Logger.getAnonymousLogger();

    private ScheduledExecutorService executor;
    private AtomicInteger threadCount = new AtomicInteger(0);
    private ResendConfig config = new ResendConfig();
    /**
     * 正在等待重发的消息数
     */
    private AtomicInteger waitingCount = new AtomicInteger(0);

    protected ResendExecutorImpl() {
        this.executor = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("netty-resend-thread-" + threadCount.addAndGet(1));
            return thread;
        });
    }

    @Override
    public ChannelFuture send(Channel channel, Msg msg) {
        return null;
    }

    @Override
    public ChannelFuture send(Msg msg) {
        return null;
    }

    @Override
    public void close() {
        executor.shutdown();
    }

    private ChannelFuture resend(Channel channel, Msg msg) {
        //重发任务数过多，抛弃当前重发任务
        if (waitingCount.get() > config.getMaxWait()) {
            logger.warning("the size of waiting msg arrive the max");
            return null;
        }
        //达到最大重发次数
        if (msg.getResendCount() > config.getMaxCount()) {
            logger.warning(() -> "msg:" + msg + " complete fail");
            waitingCount.decrementAndGet();
            return null;
        }
        msg.setResendCount(msg.getResendCount() + 1);
        return doSend(channel, msg);
    }

    private ChannelFuture doSend(Channel channel, Msg msg) {
        ChannelPromise promise = channel.newPromise();
        ChannelFuture future = channel.writeAndFlush(msg, promise);
        //模拟失败
        randomFail(promise);
        addToResend(future, msg);
        return future;
    }

    private void randomFail(ChannelPromise promise) {
        if ((new Random().nextInt(10) % 3) == 0) {
            promise.setFailure(new RuntimeException("i am a error"));
        }
    }

    private void addToResend(ChannelFuture future, Msg msg) {
        future.addListener(f -> {
            if (!f.isSuccess()) {
                logger.info("msg:" + msg + " send fail, join to the retry queue");
                executor.schedule(
                        () -> resend(future.channel(), msg),
                        config.getResendInterval(),
                        TimeUnit.MILLISECONDS
                );
                waitingCount.incrementAndGet();
            } else {
                logger.info("msg:" + msg + " complete success");
                waitingCount.decrementAndGet();
            }
        });
    }
}
