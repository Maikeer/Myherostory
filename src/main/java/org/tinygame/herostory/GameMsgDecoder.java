package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }
//        System.out.println("收到客户端消息, msg = " + msg);
        // WebSocket 二进制消息会通过 HttpServerCodec 解码成 BinaryWebSocketFrame 类对象
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        Message.Builder builder=null;
        byteBuf.readShort();//读取消息长度
        int msgCode = byteBuf.readShort();//读取消息编号

        builder = GameMsgRecognizer.getBuilder(msgCode);
        if(null ==builder){
            LOGGER.error("无法识别的消息：msgCode={}",msgCode);
           return;
        }
        // 拿到真实的字节数组并打印
        byte[] byteArray = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(byteArray);

        builder.clear();
        builder.mergeFrom(byteArray);

        Message newMsg = builder.build();
        if (null != newMsg) {
//            ctx.pipeline().writeAndFlush(cmd);
            ctx.fireChannelRead(newMsg);
        }

//        System.out.println("收到的字节 = ");
    }
}
