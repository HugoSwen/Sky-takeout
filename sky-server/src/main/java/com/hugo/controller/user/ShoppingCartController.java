package com.hugo.controller.user;

import com.hugo.dto.ShoppingCartDTO;
import com.hugo.entity.ShoppingCart;
import com.hugo.result.Result;
import com.hugo.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "购物车相关接口")
@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     */
    @ApiOperation(value = "添加购物车")
    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：{}", shoppingCartDTO);

        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }


    /**
     * 查看购物车
     */
    @ApiOperation(value = "查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车...");

        List<ShoppingCart> list = shoppingCartService.list();
        return Result.success(list);
    }

    /**
     * 删除购物车中一个商品
     */
    @ApiOperation(value = "删除购物车中一个商品")
    @PostMapping("sub")
    public Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中的一件商品：{}", shoppingCartDTO);

        shoppingCartService.delete(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 清空购物车
     */
    @ApiOperation(value = "清空购物车")
    @DeleteMapping("/clean")
    public Result clean(){
        log.info("清空购物车...");

        shoppingCartService.clean();
        return Result.success();
    }
}
