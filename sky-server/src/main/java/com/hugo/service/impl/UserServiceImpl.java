package com.hugo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hugo.constant.MessageConstant;
import com.hugo.constant.WxLoginConstant;
import com.hugo.dto.UserLoginDTO;
import com.hugo.entity.User;
import com.hugo.exception.LoginFailedException;
import com.hugo.mapper.UserMapper;
import com.hugo.properties.WeChatProperties;
import com.hugo.service.UserService;
import com.hugo.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        // 获取openid
        Map<String, String> param = new HashMap<>();
        param.put(WxLoginConstant.APPID, weChatProperties.getAppid());
        param.put(WxLoginConstant.SECRET, weChatProperties.getSecret());
        param.put(WxLoginConstant.JS_CODE, userLoginDTO.getCode());
        param.put(WxLoginConstant.GRANT_TYPE, WxLoginConstant.TYPE);

        String json = HttpClientUtil.doGet(WxLoginConstant.WX_LOGIN, param);

        // 判断openid是否为空
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        if (openid == null)
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        // 查询user表判断是否是新用户, 是则注册
        User user = userMapper.getByOpenId(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }
}
