package com.an.forum;

import com.an.forum.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class MailTests {

    @Autowired
    MailClient mailClient;

    // Thymeleaf交给Spring管理的模板引擎
    @Autowired
    TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("zixianan@126.com", "Test 2", "Welcome!");
//        mailClient.sendMail("umbrella_company@163.com", "Test 1", "Welcome!");
    }

    @Test
    public void testHTMLMail() {
        Context context = new Context();
        // 对应模板中的变量
        context.setVariable("username", "Gary");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("zixianan@126.com", "HTML Test 1", content);


    }

}
