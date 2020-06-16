package com.rdc.zrj.nettydemo.example.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author asce
 * @date 2019/7/8
 */
public class ByteToStringDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception{
        System.out.println("byte decode");
        out.add(msg.toString(CharsetUtil.UTF_8));
    }
}
