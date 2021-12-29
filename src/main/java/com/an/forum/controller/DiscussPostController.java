package com.an.forum.controller;

import com.an.forum.entity.Comment;
import com.an.forum.entity.DiscussPost;
import com.an.forum.entity.Page;
import com.an.forum.entity.User;
import com.an.forum.service.CommentService;
import com.an.forum.service.DiscussPostService;
import com.an.forum.service.UserService;
import com.an.forum.util.ForumConstant;
import com.an.forum.util.ForumUtil;
import com.an.forum.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements ForumConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        System.out.println("-----------------------------------------");
        User user = hostHolder.getUser();
        if (user == null) {
            return ForumUtil.getJSONString(403, "你还没有登录哦!");
        }

        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)){
            return ForumUtil.getJSONString(1, "标题或内容不能为空!");
        }

        System.out.println(title);
        System.out.println(content);

        DiscussPost post = new DiscussPost();
        //其他不设置的属性，默认值为0
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        //这句代码返回的是一个int型，不用拿参数接收也可以执行
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return ForumUtil.getJSONString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    // 方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model.
    // 所以,在thymeleaf中可以直接访问Page对象中的数据.
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 评论分页信息     page.getOffset()从哪一行开始，0,5,15
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表 commentList找到帖子的评论，commentVoList是对帖子评论的具体信息进行查找
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论VO列表   VO是值对象     commentVoList里面放的是一条帖子的多个评论，map对象代表一条评论以及对应回复
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);

        return "/site/discuss-detail";
    }
}
