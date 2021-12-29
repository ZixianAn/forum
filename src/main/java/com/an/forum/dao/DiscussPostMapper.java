package com.an.forum.dao;

import com.an.forum.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解作用：
    // 1. 给参数起别名
    // 2. 当方法中仅有一个参数，且该参数会在动态SQL的<if>标签中使用时，必须加Param修饰，否则MySQL会报错
    // 该方法用于统计帖子数量
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(@Param("id")int id);

    int updateCommentCount(@Param("id")int id, @Param("commentCount")int commentCount);

}
