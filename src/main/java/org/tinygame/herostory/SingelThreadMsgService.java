package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.cmdhandle.CmdHanderFactory;
import org.tinygame.herostory.cmdhandle.ICmdHander;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单线程处理消息
 */
public final class SingelThreadMsgService {
    private static ExecutorService _singleService= Executors.newSingleThreadExecutor();
    /**
     * 私有化构造方法
     */
    private SingelThreadMsgService(){}

    static public void handerMsg(ChannelHandlerContext ctx, GeneratedMessageV3 msg){
        if(null==msg){
            return;
        }
        _singleService.submit(()->{
            System.err.println("当前线程："+Thread.currentThread().getName());
            ICmdHander<? extends GeneratedMessageV3 > cmdHander=null;
            cmdHander= CmdHanderFactory.createHander(msg.getClass());

            if(null != cmdHander){
                cmdHander.hander(ctx,cast(msg));
            }
        });
    }
    /**
     * 转型消息对象
     * @param msg
     * @param <Tcmd>
     * @return
     */
    private static <Tcmd extends GeneratedMessageV3 >Tcmd cast(Object msg){
        if(null ==msg){
            return null;
        }else{
            return (Tcmd)msg;
        }
    }
}
