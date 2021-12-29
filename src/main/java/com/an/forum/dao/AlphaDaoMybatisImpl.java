package com.an.forum.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary    // 优先使用该类作为AlphaDao接口的实现
public class AlphaDaoMybatisImpl implements AlphaDao {
    @Override
    public String select() {
        return "This is AlphaDaoMybatisImpl.";
    }
}
