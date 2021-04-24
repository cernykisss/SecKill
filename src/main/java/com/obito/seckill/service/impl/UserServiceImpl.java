package com.obito.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obito.seckill.exception.GlobalException;
import com.obito.seckill.mapper.UserMapper;
import com.obito.seckill.pojo.User;
import com.obito.seckill.service.IUserService;
import com.obito.seckill.util.CookieUtil;
import com.obito.seckill.util.MD5Util;
import com.obito.seckill.util.UUIDUtil;
import com.obito.seckill.vo.LoginVo;
import com.obito.seckill.vo.RespBean;
import com.obito.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-04-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /*
    * 登录功能
    *
    * */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
////        手机号判空
//        if (mobile.isEmpty() || password.isEmpty()) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
////        手机号正则
//        if (!ValidationUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        User user = userMapper.selectById(mobile);
//        用户是否存在
        if (user == null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
//        判断密码是否正确
        if (!MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();

        //将用户信息存入redis
        //key: user:ticket
        //value: json {class: id: nickname: password: salt: head: registerDate: lastLoginDate: loginCount: }
        redisTemplate.opsForValue().set("user:" + ticket, user);

        //cookie放session
//        request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success();
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (userTicket == null) return null;
        //查询user对象
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    /*
    * 更新密码
    * */
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletResponse response, HttpServletRequest request) {
        User user = getUserByCookie(userTicket, request, response);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_FOUND);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSalt()));
        int i = userMapper.updateById(user);
        if (i == 1) {
            redisTemplate.delete("user " + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAILED);
    }

}
