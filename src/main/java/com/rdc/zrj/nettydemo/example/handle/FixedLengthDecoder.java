package com.rdc.zrj.nettydemo.example.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author asce
 * @date 2019/7/8
 */
public class FixedLengthDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        ByteBuf buf = in.readBytes(in.readableBytes());
        System.out.println("decoder:" + buf.toString(CharsetUtil.UTF_8));
        out.add(Unpooled.copiedBuffer("replace buf", CharsetUtil.UTF_8));
    }
}
