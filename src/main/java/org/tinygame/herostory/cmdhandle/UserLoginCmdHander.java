package org.tinygame.herostory.cmdhandle;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class UserLoginCmdHander implements ICmdHander<GameMsgProtocol.UserLoginCmd> {


    @Override
    public void hander(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd msg) {
        //进行数据封装
        String userName = msg.getUserName();

    }
}
