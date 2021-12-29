package com.an.forum.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHibernate")   // 括号内指定了Bean的id
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "This is AlphaDaoHibernateImpl.";
    }
}
