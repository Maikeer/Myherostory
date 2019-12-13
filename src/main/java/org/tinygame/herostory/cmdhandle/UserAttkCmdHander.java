package org.tinygame.herostory.cmdhandle;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.BoradCaster;
import org.tinygame.herostory.GameMsgRecognizer;
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
public class UserAttkCmdHander implements ICmdHander<GameMsgProtocol.UserAttkCmd> {




    @Override
    public void hander(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd msg) {
        Integer subhp = (Integer) ctx.channel().attr(AttributeKey.valueOf("subhp")).get();
        int targetUserId = msg.getTargetUserId();
        GameMsgProtocol.UserSubtractHpResult.Builder builder=GameMsgProtocol.UserSubtractHpResult.newBuilder();

        builder.setTargetUserId(targetUserId);
        builder.setSubtractHp(10);
        GameMsgProtocol.UserSubtractHpResult build = builder.build();
        BoradCaster.boroadCast(build);
    }
}
