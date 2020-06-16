package com.rdc.zrj.nettydemo.example;

import com.rdc.zrj.nettydemo.websocket.handler.HttpRequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaders.Values.URL;
import static org.junit.Assert.*;

/**
 * @author asce
 * @date 2019/7/6
 */
public class EmbeddedTest {

    @Test
    public void testDecoded(){
        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpRequestHandler("/ws"));
//        pipeline.addLast(new HttpStaticFileServerHandler());
//        pipeline.addLast(new TestInHandler());

        FullHttpRequest request = new DefaultFullHttpRequest(HTTP_1_1, GET, URL);
        request.headers().add(HttpHeaderNames.HOST, "127.0.0.1");
        request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

//        channel.writeInbound(Unpooled.copiedBuffer("hello fucking netty", CharsetUtil.UTF_8));
//        ByteBuf buf = channel.readOutbound();
//        System.out.println(buf.toString(CharsetUtil.UTF_8));

        channel.writeInbound(request);

        assertTrue(channel.finish());

        int count = 0;
        while (true) {
            ByteBuf a = channel.readOutbound();
            if (a == null) break;
            System.out.println(a);
            System.out.println(a.toString(CharsetUtil.UTF_8));
        }
    }
}
