package com.rdc.zrj.nettydemo.websocket.initializer;

import com.rdc.zrj.nettydemo.websocket.handler.HttpRequestHandler;
import com.rdc.zrj.nettydemo.websocket.handler.TextWebSocketFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author asce
 * @date 2019/7/7
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {

    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group){
        this.group = group;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception{
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        //聚合解码HttpRequest/HttpContent/LastHttpContent到FullHttpRequest
        //保证接收的Http请求的完整性
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new HttpRequestHandler("/ws"));
        //WebSocketServerProtcolHandler 不仅处理 Ping/Pong/CloseWebSocketFrame，还和它自己握手并帮助升级 WebSocket
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new TextWebSocketFrameHandler(group));
    }
}
