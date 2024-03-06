package com.heu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.common.R;
import com.heu.domin.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {

    boolean sendMsg(User user, HttpSession session);

    User login(HttpServletRequest request, Map map);

    public R<String> logout(HttpServletRequest request);
}
