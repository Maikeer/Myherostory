package org.tinygame.herostory.cmdhandle;

import com.google.protobuf.GeneratedMessageV3;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

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
 * 指令处理器工厂
 */
public final class CmdHanderFactory {
    static public Map<Class<?>,ICmdHander<? extends GeneratedMessageV3>> _handerMap=new HashMap<>();
    static public void init(){
        _handerMap.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHander());
        _handerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHander());
        _handerMap.put(GameMsgProtocol.UserMoveToCmd.class,new UserMoveToCmdHander());
    }
    /**
     * 私有化
     */
    private CmdHanderFactory(){}
    static public ICmdHander<? extends GeneratedMessageV3> createHander(Class<?> msg){
        if(null==msg){
            return null;
        }
        return _handerMap.get(msg);
    }
}
