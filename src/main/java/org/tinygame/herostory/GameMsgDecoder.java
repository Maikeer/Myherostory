package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
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
        byteBuf.readShort();//读取消息长度
        int msgCode = byteBuf.readShort();//读取消息编号

        GeneratedMessageV3 cmd = null;

        // 拿到真实的字节数组并打印
        byte[] byteArray = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(byteArray);
        switch (msgCode) {
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                cmd = GameMsgProtocol.UserEntryCmd.parseFrom(byteArray);
                break;
            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                cmd = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(byteArray);
                break;
            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                cmd = GameMsgProtocol.UserMoveToCmd.parseFrom(byteArray);
                break;

        }
        if (null != cmd) {
//            ctx.pipeline().writeAndFlush(cmd);
            ctx.fireChannelRead(cmd);
        }

//        System.out.println("收到的字节 = ");
    }
}
