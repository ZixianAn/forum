package com.an.forum.dao;

import com.an.forum.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

// 另一种写SQL的方式
@Mapper
public interface LoginTicketMapper {

    @Insert({
            // 注意结尾空一格，因为是将字符串拼接为一个SQL语句
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    // 自动生成主键
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(@Param("ticket")String ticket);

    @Update({
            // 动态SQL示例
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(@Param("ticket")String ticket, @Param("status")int status);
}
