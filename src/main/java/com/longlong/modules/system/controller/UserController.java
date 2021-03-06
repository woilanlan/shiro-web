package com.longlong.modules.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.longlong.modules.system.vo.User;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    
    //produces = "application/json; charset=UTF-8"
    @RequestMapping(value = "/sublogin",method = RequestMethod.POST)
    @ResponseBody
    public String subLogin(User user) {
        JSONObject json = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        
        if(subject.hasRole("admin")){
            log.info("有admin权限");
            json.put("info", "有admin权限");
        }else{
            json.put("info", "没有admin权限");
        }
        return json.toJSONString();
    }

/*     
    //通过注解配置授权
    @RequiresRoles("admin")
    @RequestMapping(value = "/testRole",method = RequestMethod.GET)
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

    @RequiresPermissions("user:delete")
    @RequestMapping(value = "/testRole1",method = RequestMethod.GET)
    @ResponseBody
    public String testRole1() {
        return "testRole1 success1";
    }
 */

    @RequestMapping(value = "/testRole",method = RequestMethod.GET)
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

    @RequestMapping(value = "/testRole1",method = RequestMethod.GET)
    @ResponseBody
    public String testRole1() {
        JSONObject json = new JSONObject();
        json.put("info", "testRole1 success1");
        return json.toJSONString();
    }

    @RequestMapping(value = "/testPerms",method = RequestMethod.GET)
    @ResponseBody
    public String testPerms() {
        return "testPerms success";
    }

    @RequestMapping(value = "/testPerms1",method = RequestMethod.GET)
    @ResponseBody
    public String testPerms1() {
        return "testPerms1 success";
    }

    @RequestMapping(value = "/hello")
    public String hello(HttpServletRequest req) {
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        req.setAttribute("user", "longlong");
        req.setAttribute("list", list);
        return "hello";
    }

    @RequestMapping(value = "/one")
    public String one(HttpServletRequest req) {
        req.setAttribute("user", "longlong");
        return "one";
    }
}