package org.tinygame.herostory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏消息处理器
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {
    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final Map<Integer, User> _userMap = new HashMap<Integer, User>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _channelGroup.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        _channelGroup.remove(ctx.channel());
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        _userMap.remove(userId);
        GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(userId);
        GameMsgProtocol.UserQuitResult userQuitResult = resultBuilder.build();
        _channelGroup.writeAndFlush(userQuitResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息, msgclass=" + msg.getClass().getName() + " msg = " + msg);
        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            GameMsgProtocol.UserEntryCmd userEntryCmd = (GameMsgProtocol.UserEntryCmd) msg;
            int userId = userEntryCmd.getUserId();
            String heroAvatar = userEntryCmd.getHeroAvatar();
            GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
            resultBuilder.setUserId(userId);
            resultBuilder.setHeroAvatar(heroAvatar);

            User user = new User();
            user.userId = userId;
            user.heroAvatar = heroAvatar;
            _userMap.put(user.userId, user);
            //存储用户id绑定到channel
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
            //构建结构并发送
            GameMsgProtocol.UserEntryResult build = resultBuilder.build();
            _channelGroup.writeAndFlush(build);
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
            for (User currentUser : _userMap.values()) {
                if (null == currentUser) {
                    continue;
                }
                GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
                userInfoBuilder.setUserId(currentUser.userId);
                userInfoBuilder.setHeroAvatar(currentUser.heroAvatar);
                resultBuilder.addUserInfo(userInfoBuilder);
            }
            GameMsgProtocol.WhoElseIsHereResult whoBuilder = resultBuilder.build();

            ctx.writeAndFlush(whoBuilder);
        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
            Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
            if (null == userId) {
                return;
            }
            GameMsgProtocol.UserMoveToCmd cmd = (GameMsgProtocol.UserMoveToCmd) msg;
            GameMsgProtocol.UserMoveToResult.Builder moveBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
            moveBuilder.setMoveUserId(userId);
            moveBuilder.setMoveToPosX(cmd.getMoveToPosX());
            moveBuilder.setMoveToPosY(cmd.getMoveToPosY());
            GameMsgProtocol.UserMoveToResult build = moveBuilder.build();
            _channelGroup.writeAndFlush(build);
        }
    }


}
