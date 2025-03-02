package org.solvd.recommendation.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.solvd.recommendation.util.MyBatisUtil;

public abstract class AbstractMyBatis {
    protected final SqlSessionFactory sqlSessionFactory;

    protected AbstractMyBatis() {
        this.sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
    }


}