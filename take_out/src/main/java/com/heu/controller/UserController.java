package com.heu.controller;

import com.heu.common.R;
import com.heu.domin.User;
import com.heu.service.UserService;
import com.heu.utils.SMSUtils;
import com.heu.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        boolean res = userService.sendMsg(user, session);
        return res ? R.success("发送成功") : R.error("发送失败");
    }

    @PostMapping("/login")
    public R<User> login(HttpServletRequest request,@RequestBody Map map){
        User user = userService.login(request,map);
        return user != null ? R.success(user) : R.error("登录失败");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request) {
        R<String> logout = userService.logout(request);
        return logout;
    }
}
