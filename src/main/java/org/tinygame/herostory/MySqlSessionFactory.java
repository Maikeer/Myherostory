package org.tinygame.herostory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public final class MySqlSessionFactory {
    private MySqlSessionFactory (){}
    private static MySqlSessionFactory _intacne=new MySqlSessionFactory();
    public static MySqlSessionFactory getInstance(){

        return _intacne;
    }
    private SqlSessionFactory build;
    public void init(){
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder =new SqlSessionFactoryBuilder();
        try {
             build = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("MyBatisConfig.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public SqlSession openSession(){
      return  build.openSession(true);
    }
}
