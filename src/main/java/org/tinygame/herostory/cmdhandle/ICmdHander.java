package org.tinygame.herostory.cmdhandle;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.msg.GameMsgProtocol;

public interface ICmdHander<Tcmd extends GeneratedMessageV3> {
    void hander(ChannelHandlerContext ctx, Tcmd msg);
}
