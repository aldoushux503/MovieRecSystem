package org.solvd.recommendation.repository.mybatisImpl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.solvd.recommendation.util.MyBatisUtil;

public abstract class RepositoryMyBatis {
    protected final SqlSessionFactory sqlSessionFactory;

    protected RepositoryMyBatis() {
        this.sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
    }

    protected <T> T withSession(Function<SqlSession, T> function) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            T result = function.apply(session);
            session.commit();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error in database operation", e);
        }
    }

    @FunctionalInterface
    protected interface Function<T, R> {
        R apply(T t);
    }
}