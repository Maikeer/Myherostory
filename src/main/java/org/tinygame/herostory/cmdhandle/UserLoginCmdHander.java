package org.tinygame.herostory.cmdhandle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.BoradCaster;
import org.tinygame.herostory.login.LoginService;
import org.tinygame.herostory.login.db.UserEntity;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class UserLoginCmdHander implements ICmdHander<GameMsgProtocol.UserLoginCmd> {


    @Override
    public void hander(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd msg) {
        //进行数据封装
        String userName = msg.getUserName();
        String password = msg.getPassword();
        UserEntity userEntity = LoginService.getInstance().userLogin(userName, password);
        if(null==userEntity){
            return;
        }
        User user = new User();
        user.userId = userEntity.userId;
        user.username=userEntity.userName;
        user.heroAvatar = userEntity.heroAvatar;
        UserManager.addUser( user);
        //存储用户id绑定到channel
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(user.userId);
        ctx.channel().attr(AttributeKey.valueOf("subhp")).set(100);
        GameMsgProtocol.UserLoginResult.Builder builder=GameMsgProtocol.UserLoginResult.newBuilder();
        builder.setUserId(user.userId);
        builder.setUserName(user.username);
        builder.setHeroAvatar(user.heroAvatar);
        GameMsgProtocol.UserLoginResult build = builder.build();
//        BoradCaster.boroadCast(builder);
        ctx.writeAndFlush(build);
    }
}
