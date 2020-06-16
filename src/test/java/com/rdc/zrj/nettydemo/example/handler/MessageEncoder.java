package com.rdc.zrj.nettydemo.example.handler;

import com.rdc.zrj.nettydemo.im.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author asce
 * @date 2019/7/13
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out){
        byte[] body = message.getBody().getBytes();
        out.writeInt(message.getHead().getMsgId());
        out.writeInt(message.getHead().getMsgType());
        out.writeInt(message.getHead().getMsgContentType());
        out.writeInt(message.getHead().getFromId());
        out.writeInt(message.getHead().getToId());
        out.writeLong(message.getHead().getTimestamp());
        out.writeInt(message.getHead().getStatusReport());
        out.writeInt(message.getHead().getExtend());
        out.writeBytes(body);
    }
}
