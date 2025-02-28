package org.solvd.recommendation.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static SqlSessionFactory sqlSessionFactory;

    // Private constructor to prevent instantiation
    private DatabaseConfig() {}

    public static synchronized SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            try {
                Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                reader.close();
                logger.info("SqlSessionFactory initialized successfully");
            } catch (IOException e) {
                logger.error("Error initializing SqlSessionFactory", e);
                throw new RuntimeException("Error initializing MyBatis SqlSessionFactory", e);
            }
        }
        return sqlSessionFactory;
    }
}