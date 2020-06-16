package com.rdc.zrj.nettydemo.example;

import com.rdc.zrj.nettydemo.example.handle.EchoServerHandler;
import com.rdc.zrj.nettydemo.example.handle.FixedLengthDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author asce
 * @date 2019/7/8
 */
public class EmbeddedChannelTest {

    @Test
    public void testHandle() throws Exception{
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addFirst(new FixedLengthDecoder());
        channelPipeline.addLast(new EchoServerHandler());
        channel.writeInbound(Unpooled.copiedBuffer("hello fucking netty", CharsetUtil.UTF_8));
        assertTrue(channel.finish());
        ByteBuf buf = channel.readInbound();
        if (buf != null) {
            System.out.println("test:" + buf.toString(CharsetUtil.UTF_8));
        }
    }
}
