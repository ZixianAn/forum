package com.an.forum.controller;

import com.an.forum.entity.DiscussPost;
import com.an.forum.entity.Page;
import com.an.forum.entity.User;
import com.an.forum.service.DiscussPostService;
import com.an.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        // 在方法调用前，Spring MVC会自动实例化参数中的Model和Page，并将Page注入Model
        // 因此我们不用把Page添加到Model中，Thymeleaf是可以直接访问到Page中的数据的
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        // 在Map中分别存储用户信息和帖子信息，以便于前端展示
        List<Map<String, Object>> discussPostList = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                System.out.println(user.getHeaderUrl());
                map.put("user", user);
                discussPostList.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPostList);
        return "/index";
    }
}
