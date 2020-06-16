package com.rdc.zrj.nettydemo.resend.client.executor;

import io.netty.channel.Channel;

public class ExecutorFactory {

    private ExecutorFactory() {}

    public static ResendExecutor getInstance() {
        return new ResendExecutorImpl();
    }

    public static ResendExecutor getClientInstance(Channel channel) {
        return new ResendClientExecutor(channel);
    }
}
