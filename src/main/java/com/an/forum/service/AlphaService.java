package com.an.forum.service;

import com.an.forum.dao.AlphaDao;
import com.an.forum.dao.DiscussPostMapper;
import com.an.forum.dao.UserMapper;
import com.an.forum.entity.DiscussPost;
import com.an.forum.entity.User;
import com.an.forum.util.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
//@Scope("singleton")   默认值，即单例
//@Scope("prototype")
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserMapper userMapper;


    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    @PostConstruct  // 在实例化之后调用
    public void init() {
        System.out.println("初始化AlphaService");
    }

    @PreDestroy // 在销毁实例前调用
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.A调B，A如果有事务就按A；A没有，B新建事务
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).A调B，B无视A的事务
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
    //声明式事务：当程序发生异常时，不会插入数据
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {
        // 新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(ForumUtil.generateUUID().substring(0, 5));
        user.setPassword(ForumUtil.md5("123" + user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("Hello");
        post.setContent("新人报道!");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");

        return "ok";
    }
}
