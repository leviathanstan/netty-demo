package com.rdc.zrj.nettydemo.example;

import com.rdc.zrj.nettydemo.websocket.handler.HttpRequestHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.junit.Test;
import com.rdc.zrj.nettydemo.example.handler.TestInHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertTrue;

/**
 * @author asce
 * @date 2019/7/11
 */
public class SquareSolve {

    public static void main(String[] args) {
        int target = 899;
        System.out.println(HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation());
    }


    @Test
    public void testFuckingEmbedded(){
        EmbeddedChannel channel = new EmbeddedChannel(new TestInHandler());
        channel.writeInbound(Unpooled.copiedBuffer("hello fucking netty", CharsetUtil.UTF_8));
        assertTrue(channel.finish());
        String buf = channel.readOutbound();
        System.out.println(buf);
    }

    @Test
    public void testA() throws InterruptedException{
        long l = System.currentTimeMillis();
        EventExecutorGroup group = new DefaultEventExecutorGroup(4);
        Future<Integer> f = group.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("执行耗时操作...");
                Thread.sleep(3000);
                return 100;
            }
        });
        f.addListener(new FutureListener<Object>() {
            @Override
            public void operationComplete(Future<Object> objectFuture) throws Exception {
                System.out.println("计算结果:：" + objectFuture.get());
            }
        });
        System.out.println("主线程运算耗时:" + (System.currentTimeMillis() - l)+ "ms");
        new CountDownLatch(1).await();
    }

}
