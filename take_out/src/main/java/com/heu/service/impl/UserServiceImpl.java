package com.heu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heu.common.R;
import com.heu.dao.UserDao;
import com.heu.domin.User;
import com.heu.service.UserService;
import com.heu.utils.SMSUtils;
import com.heu.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisCacheManager cacheManager;


    @Override
    public boolean sendMsg(User user, HttpSession session) {
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            // 随机生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            log.info("code={}",code);
            // 调用阿里云短信服务api
            try {
                SMSUtils.sendMsg(phone,"瑞吉外卖",code);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
//            session.setAttribute(phone,code); // session域中存贮验证码
            // 用redis代替session提升性能
            ValueOperations operations = redisTemplate.opsForValue();
            // 验证码过期时间为5min
            operations.set("code",code,5, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    @Override
    public User login(HttpServletRequest request, Map map) {
        /**
         // 判断输入的验证码是否正确
        // 从redis中获取验证码
        String trueCode = redisTemplate.opsForValue().get("code");
        if(trueCode == null || !trueCode.equals(map.get("code"))){
            // 验证失败
            return null;
        }
        // 验证成功，删除redis中的验证码
        redisTemplate.delete("code");
         */

        // 判断该用户是否存在
        String phone = (String) map.get("phone");
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhone,phone);
        User user = this.getOne(lqw);

        if(user == null){
            user = new User();
            // 创建新用户
            user.setPhone(phone);
            user.setStatus(1);
            this.save(user);
        }
        request.getSession().setAttribute("user",user.getId());
        return user;
    }

    @Override
    public R<String> logout(HttpServletRequest request){
        // 移除session域中的id
        request.getSession().removeAttribute("emp");
        return R.success("退登成功");
    }
}
