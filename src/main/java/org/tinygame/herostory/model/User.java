package org.tinygame.herostory.model;

/**
 * 用户
 */
public class User {
    public int userId;//用户id
    public String username;
    public String password;
    /**
     * 英雄类型
     */
    public String heroAvatar;
    public UserInfo info=new UserInfo();
}
