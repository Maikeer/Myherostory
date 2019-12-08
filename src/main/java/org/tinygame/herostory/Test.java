package org.tinygame.herostory;

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
 */
public class Test {
    public static void main(String[] args) {
        GameMsgProtocol.UserQuitResult.Builder builder = GameMsgProtocol.UserQuitResult.newBuilder();
        builder.setQuitUserId(1);
    }
}
