package org.tinygame.herostory.cmdhandle;

import io.netty.channel.ChannelHandlerContext;
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
public class WhoElseIsHereCmdHander implements ICmdHander<GameMsgProtocol.WhoElseIsHereCmd> {
    @Override
    public void hander(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg ) {
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
        for (User currentUser : UserManager.getUsers()) {
            if (null == currentUser) {
                continue;
            }
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(currentUser.userId);
            userInfoBuilder.setHeroAvatar(currentUser.heroAvatar);
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder moveStateOrBuilder=
                    GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
            moveStateOrBuilder.setFromPosX(currentUser.info.fromX);
            moveStateOrBuilder.setFromPosY(currentUser.info.fromY);
            moveStateOrBuilder.setToPosX(currentUser.info.toX);
            moveStateOrBuilder.setToPosY(currentUser.info.toY);
            moveStateOrBuilder.setStartTime(currentUser.info.startTime);
            userInfoBuilder.setMoveState(moveStateOrBuilder.build());

            resultBuilder.addUserInfo(userInfoBuilder);
        }
        GameMsgProtocol.WhoElseIsHereResult whoBuilder = resultBuilder.build();

        ctx.writeAndFlush(whoBuilder);
    }
}
