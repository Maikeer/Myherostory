package org.tinygame.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.Collection;

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
 */
public final class BoradCaster {
    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 私有化构造方法
     */
    private BoradCaster (){

    }

    /**
     * 添加信道
     * @param channel
     */
    static public void addChannel(Channel channel){
        _channelGroup.add(channel);
    }
    /**
     * delete信道
     * @param channel
     */
    static public void deleteChannel(Channel channel){
        _channelGroup.remove(channel);
    }
    /**
     * 广播信道
     * @param msg
     */
    static public void boroadCast(Object msg){
        if(null ==msg){
            return;
        }
        Collection<User> users = UserManager.getUsers();

        _channelGroup.writeAndFlush(msg);
    }
}
