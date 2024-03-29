package org.tinygame.herostory.cmdhandle;

import com.google.protobuf.GeneratedMessageV3;
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
public class UserEntryCmdHander implements ICmdHander<GameMsgProtocol.UserEntryCmd>{
    public UserEntryCmdHander()  {
    }
    @Override
    public  void hander(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd msg) {
        GameMsgProtocol.UserEntryCmd userEntryCmd = msg;
        Integer useId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        User user= UserManager.getUserById(useId);
        int userId = user.userId;
        String heroAvatar = user.heroAvatar;
        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        //构建结构并发送
        GameMsgProtocol.UserEntryResult build = resultBuilder.build();
        BoradCaster.boroadCast(build);
    }


}
