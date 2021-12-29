package com.an.forum.controller;

import com.an.forum.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello, Gary An!";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }

        // 参数
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html; charset=utf-8");
        try (
                // 括号内的流会在finally阶段自动关闭
                PrintWriter writer = response.getWriter();
        ){
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GET请求
    // 获取参数的两种方式

    // 方式一：RequestParam注解
    // 请求示例：/students?current=1&limit=10
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit
    ) {
        System.out.println(current);
        System.out.println(limit);
        return "some students...";
    }

    // 方式二：RequestParam注解
    // 请求示例：/student/777
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent (
            @PathVariable("id") int id
    ) {
        System.out.println(id);
        return "a student...";
    }

    // POST请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent (String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 响应HTML数据

    // 方式一
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "Gary");
        modelAndView.addObject("age", "25");

        // ModelAndView的数据会被返回到resources/templates/demo/view中进行渲染
        modelAndView.setViewName("demo/view");
        return modelAndView;
    }
    // 方式二
    // 方式一的Model和View在同一个对象里，通过setViewName方法来设置View的路径。
    // 而方式二直接获取并设置Model，在返回值中声明View的路径
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "UESTC");
        model.addAttribute("age", "70");
        return "demo/view";
    }

    // 响应JSON数据（异步请求场景）
    // 异步指当前网页并没有刷新，但是系统在后台已经访问了服务器。例如，在网站注册时，若输入的用户名已被注册，会立刻有提示，而此时网页并没有刷新
    // Java对象 -> JSON字符串 -> JS对象
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    // 不加该注解的话默认返回的是HTML
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "Gary");
        emp.put("age", "25");
        emp.put("salary", "30000");
        return emp;
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "Gary");
        emp.put("age", "25");
        emp.put("salary", "30000");
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "Aary");
        emp.put("age", "21");
        emp.put("salary", "304000");
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "Wary");
        emp.put("age", "15");
        emp.put("salary", "10000");
        list.add(emp);

        return list;
    }
}
