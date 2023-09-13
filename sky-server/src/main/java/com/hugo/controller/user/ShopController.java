package com.hugo.controller.user;

import com.hugo.constant.FieldConstant;
import com.hugo.constant.StatusConstant;
import com.hugo.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Api(tags = "店铺相关接口")
@Slf4j
@RestController("userShopController")
@RequestMapping("/user/shop")
public class ShopController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取当前营业状态
     */
    @ApiOperation(value = "获取营业状态")
    @GetMapping("/status")
    public Result<Integer> getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(FieldConstant.SHOP_STATUS);
        log.info("获取当前营业状态为：{}", Objects.equals(status, StatusConstant.ENABLE) ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
