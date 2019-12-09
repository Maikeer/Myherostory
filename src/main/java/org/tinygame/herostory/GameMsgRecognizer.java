package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdhandle.ICmdHander;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.lang.reflect.InvocationTargetException;
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
 * 消息识别器
 */
public final class GameMsgRecognizer {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);
    static private final Map<Integer, GeneratedMessageV3 > _msgCodeAndMsgBodyMap=new HashMap<>();
    static private final Map<Class<?>, Integer > _msgClazzAndMsgCodeMap=new HashMap<>();
    private GameMsgRecognizer(){}
    static public void init(){
        //获得所有的内部类
        Class<?>[] declaredClasses = GameMsgProtocol.class.getDeclaredClasses();
        for (Class clazz:declaredClasses) {
            if(!GeneratedMessageV3.class.isAssignableFrom(clazz)){
                continue;
            }
            String simpleName = clazz.getSimpleName();
            simpleName=simpleName.toLowerCase();
//            String s = simpleName.replaceAll("_", "");
            GameMsgProtocol.MsgCode[] values = GameMsgProtocol.MsgCode.values();
            for (GameMsgProtocol.MsgCode msgCode:values) {
                String name = msgCode.name();
                name = name.replaceAll("_", "");
                name=name.toLowerCase();
                if(!name.startsWith(simpleName)){
                    continue;
                }
                Object invoke = null;
                try {
                    invoke = clazz.getDeclaredMethod("getDefaultInstance").invoke(clazz);
                    LOGGER.info("{}------{}",invoke.getClass().getName(),msgCode.getNumber());
                    if(!clazz.getSimpleName().contains("Result")){
                        _msgCodeAndMsgBodyMap.put(msgCode.getNumber(), (GeneratedMessageV3) invoke);
                    }else{
                        _msgClazzAndMsgCodeMap.put(clazz,msgCode.getNumber());
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
//        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE,GameMsgProtocol.UserEntryCmd.getDefaultInstance());
//        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE,GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
//        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE,GameMsgProtocol.UserMoveToCmd.getDefaultInstance());
//
//        _msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class,GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
//        _msgClazzAndMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class,GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
//        _msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class,GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
//        _msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserQuitResult.class,GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
    }
    static public Message.Builder getBuilder(int msgCode){
       if(msgCode<0){
           return null;
       }
        GeneratedMessageV3 generatedMessageV3 = _msgCodeAndMsgBodyMap.get(msgCode);
       if(null ==generatedMessageV3){
           return null;
       }
       return generatedMessageV3.newBuilderForType();
    }
    static  public int getMsgCodeByClazz(Class<?> msgClazz){
        if(null==msgClazz){
            return -1;
        }
        Integer msgCode = _msgClazzAndMsgCodeMap.get(msgClazz);
        if(null !=msgCode){
            return msgCode;
        }else{
            return -1;
        }

    }
}
