package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.nio.ByteBuffer;

public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(GameMsgEncoder.class);
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(null == msg || !(msg instanceof GeneratedMessageV3)){
                super.write(ctx, msg, promise);
                return;
        }
        int msgCode=-1;
        msgCode = GameMsgRecognizer.getMsgCodeByClazz(msg.getClass());
        if(msgCode<=-1){
            LOGGER.error("无法识别的消息：msgClazz={}",msg.getClass().getName());
            return;
        }
        GeneratedMessageV3 result= (GeneratedMessageV3) msg;
        byte[] bytes = result.toByteArray();
        ByteBuf byteBuffer =ctx.alloc().buffer();
        byteBuffer.writeShort((short)0);
        byteBuffer.writeShort((short)msgCode);
        byteBuffer.writeBytes(bytes);
        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuffer);
        super.write(ctx, frame, promise);
    }
}

