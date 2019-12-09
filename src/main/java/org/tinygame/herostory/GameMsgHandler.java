package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.tinygame.herostory.cmdhandle.*;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏消息处理器
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        BoradCaster.addChannel(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        BoradCaster.deleteChannel(ctx.channel());
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        UserManager.removeUser(userId);
        GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(userId);
        GameMsgProtocol.UserQuitResult userQuitResult = resultBuilder.build();
        BoradCaster.boroadCast(userQuitResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息, msgclass=" + msg.getClass().getName() + " msg = " + msg);
        ICmdHander<? extends GeneratedMessageV3 > cmdHander=null;
        cmdHander= CmdHanderFactory.createHander(msg.getClass());

        if(null != cmdHander){
            cmdHander.hander(ctx,cast(msg));
        }
    }

    /**
     * 转型消息对象
     * @param msg
     * @param <Tcmd>
     * @return
     */
    private <Tcmd extends GeneratedMessageV3 >Tcmd cast(Object msg){
        if(null ==msg){
            return null;
        }else{
            return (Tcmd)msg;
        }
    }



}
