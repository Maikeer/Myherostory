package org.tinygame.herostory.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
 * Fn+Alt+Insert：产生构造方法
 */
public final class UserManager {
    private static final Map<Integer, User> _userMap = new HashMap<Integer, User>();
    private UserManager() {
    }

    /**
     * 添加用户
     * @param user
     */
    static public void addUser(User user){
        if(null==user)return;
        _userMap.put(user.userId,user);
    }
    /**
     * 删除用户
     * @param userId
     */
    static public void removeUser(Integer userId){
        if(null==userId)return;
        _userMap.remove(userId);
    }
    /**
     * 获取用户列表
     * @param
     */
    static public Collection<User> getUsers(){
       return  _userMap.values();
    }
}
