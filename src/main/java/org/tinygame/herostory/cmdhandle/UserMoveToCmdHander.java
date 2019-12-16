package org.tinygame.herostory.cmdhandle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.BoradCaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Author: jin.tang
 * <p>
 * 常用快捷键
 * ctrl+shift+space 代码提示
 * Ctrl+Alt+L	代码格式化
 * Ctrl+O	重写父类方法
 * Ctrl-P	方法参数提示
 * Ctrl+Alt+O	优化导入的类和包
 * Ctrl+Alt+L	代码格式化
 * Alt+J	提示所有智能补全代码
 * CTRL + ALT + M 代码抽取成方法
 * Ctrl + F12 展示一个类的所有方法
 * Ctrl+H：显示当前类的继承层次
 * Alt+Insert：产生构造方法
 */
public class UserMoveToCmdHander implements ICmdHander<GameMsgProtocol.UserMoveToCmd> {
    @Override
    public void hander(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd msg) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        GameMsgProtocol.UserMoveToCmd cmd = msg;
        User user = UserManager.getUserById(userId);
        long currentTimeMillis = System.currentTimeMillis();
        user.info.fromX=cmd.getMoveFromPosX();
        user.info.fromY=cmd.getMoveFromPosY();
        user.info.toX=cmd.getMoveToPosX();
        user.info.toY=cmd.getMoveToPosY();
        user.info.startTime=currentTimeMillis;
        UserManager.addUser(user);
        GameMsgProtocol.UserMoveToResult.Builder moveBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        moveBuilder.setMoveUserId(userId);
        moveBuilder.setMoveFromPosX(cmd.getMoveFromPosX());
        moveBuilder.setMoveFromPosY(cmd.getMoveFromPosY());
        moveBuilder.setMoveToPosX(cmd.getMoveToPosX());
        moveBuilder.setMoveToPosY(cmd.getMoveToPosY());
        moveBuilder.setMoveStartTime(currentTimeMillis);
        GameMsgProtocol.UserMoveToResult build = moveBuilder.build();
        BoradCaster.boroadCast(build);
    }
}
