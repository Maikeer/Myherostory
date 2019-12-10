package org.tinygame.herostory.cmdhandle;

import com.google.protobuf.GeneratedMessageV3;
import org.reflections.Reflections;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        //获取该类下所有内部类
        Class<?>[] declaredClasses = GameMsgProtocol.class.getDeclaredClasses();
        //获取ICmdHander同包名下面的子类实现类
        Package aPackage = ICmdHander.class.getPackage();
        //获取该类的包名 并根据包名找到真实的路径
        String packageName = aPackage.getName();
        //---------------------------------------第二种---------------------------------------
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends ICmdHander>> subTypesOf = reflections.getSubTypesOf(ICmdHander.class);
        for (Class<?> clz:subTypesOf) {
            System.err.println(clz.getName());
            Type[] genericInterfaces = clz.getGenericInterfaces();
            if(null==genericInterfaces||genericInterfaces.length==0){
                continue;
            }
            Type genericInterface = genericInterfaces[0];
            ParameterizedType parameterizedType= (ParameterizedType) genericInterface;
            Type type = parameterizedType.getActualTypeArguments()[0];
            try {
                _handerMap.put(Class.forName(type.getTypeName()), (ICmdHander<? extends GeneratedMessageV3>) clz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.err.println(_handerMap);
        //---------------------------------------第一种---------------------------------------
/*        String replacePackage = packageName.replace(".", "/");
        URL realUrl = ICmdHander.class.getClassLoader().getResource(replacePackage);
        String realPath = realUrl.getPath();
        //根据真实路径获取找到子类实现
        File rootFile = new File(realPath);
        ArrayList<Class<?>> endClasses=null;
        if(rootFile.exists()){
            File[] classFiles = rootFile.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    boolean b = pathname.getName().endsWith(".class");
                    if(b){
                        return true;
                    }else{
                        return false;
                    }

                }
            });
            //循环load class文件，检查是否是ICmdHander接口的实现类
             endClasses = new ArrayList<>();
            for (File f:classFiles) {
                try {
                    Class<?> aClass = Class.forName(packageName + "." + f.getName().substring(0, f.getName().length() - 6));
                    if(ICmdHander.class.isAssignableFrom(aClass)&&!aClass.equals(ICmdHander.class)){
                        endClasses.add(aClass);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if(endClasses==null||endClasses.size()==0){
            return;
        }
        //对找到的接口实现类进行handerMap装填
        for (Class c:endClasses) {
            Type[] genericInterfaces = c.getGenericInterfaces();
            Type genericSuperclass = c.getGenericSuperclass();
            if(null==genericInterfaces||genericInterfaces.length==0){
                continue;
            }
            Type genericInterface = genericInterfaces[0];
            ParameterizedType parameterizedType= (ParameterizedType) genericInterface;
            Type type = parameterizedType.getActualTypeArguments()[0];
            try {
                _handerMap.put(Class.forName(type.getTypeName()), (ICmdHander<? extends GeneratedMessageV3>) c.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/
//        _handerMap.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHander());
//        _handerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHander());
//        _handerMap.put(GameMsgProtocol.UserMoveToCmd.class,new UserMoveToCmdHander());
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
