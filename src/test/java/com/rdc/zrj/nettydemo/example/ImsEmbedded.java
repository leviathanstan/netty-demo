package com.rdc.zrj.nettydemo.example;

import com.rdc.zrj.nettydemo.im.handler.MessageDecoder;
import com.rdc.zrj.nettydemo.im.handler.ServerHandler;
import com.rdc.zrj.nettydemo.im.model.Message;
import com.rdc.zrj.nettydemo.im.model.MessageHead;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import com.rdc.zrj.nettydemo.example.handler.MessageEncoder;
import com.rdc.zrj.nettydemo.example.handler.TestOutHandler;
import com.rdc.zrj.nettydemo.example.handler.TestSimpleInHandler;

import static org.junit.Assert.assertTrue;

/**
 * @author asce
 * @date 2019/7/13
 */
public class ImsEmbedded {

    Message message;
    {
        message = new Message(new MessageHead(200, 2, 3, 4, 5, 6, 7,8), "i am a body");
    }

    @Test
    public void testServerChannel(){
        ByteBuf buf;
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
        pipeline.addLast(new TestSimpleInHandler());
        pipeline.addFirst(new TestOutHandler());
        pipeline.addLast(new MessageEncoder());

        channel.writeOutbound(message);
        ByteBuf lenBuf = channel.readOutbound();
        ByteBuf bodyBuf = channel.readOutbound();
        channel.writeInbound(lenBuf, bodyBuf);
//        assertTrue(channel.finish());
//        buf = channel.readInbound();
//        System.out.println(buf);
    }

    @Test
    public void testFuckingEmbedded(){
        EmbeddedChannel channel = new EmbeddedChannel(new TestSimpleInHandler());
        channel.writeInbound(Unpooled.copiedBuffer("hello fucking netty", CharsetUtil.UTF_8));
        assertTrue(channel.finish());
        ByteBuf buf = channel.readInbound();
        System.out.println(buf.toString(CharsetUtil.UTF_8));
    }

    @Test
    public void testServerHandler(){
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,
                0, 2, 0, 2));
        //处理类
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new ServerHandler());
        pipeline.addLast(new MessageEncoder());

        channel.writeOutbound(message);
        ByteBuf lenBuf = channel.readOutbound();
        ByteBuf bodyBuf = channel.readOutbound();

        channel.writeInbound(lenBuf, bodyBuf);

        assertTrue(channel.finish());
        ByteBuf buf = channel.readInbound();
        System.out.println(buf);
    }
}
