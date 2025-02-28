package org.solvd.recommendation.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public class MyBatisUtil {
    private static SqlSessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(
                    Resources.getResourceAsStream("mybatis-config.xml")
            );
        } catch (IOException e) {
            throw new RuntimeException("Error to configure MyBatis", e);
        }
    }


    public static SqlSessionFactory getInstance() {
        return sessionFactory;
    }
}