package com.hugo.controller.user;

import com.hugo.context.BaseContext;
import com.hugo.entity.AddressBook;
import com.hugo.result.Result;
import com.hugo.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "地址相关接口")
@Slf4j
@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增收货地址
     */
    @ApiOperation(value = "新增收货地址")
    @PostMapping
    public Result addAddress(@RequestBody AddressBook addressBook){
        log.info("新增收货地址：{}", addressBook);

        addressBookService.addAddress(addressBook);
        return Result.success();
    }

    /**
     * 查询用户所有地址信息
     */
    @ApiOperation(value = "查询用户所有地址信息")
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        log.info("查询当前用户所有的地址信息：{}", BaseContext.getCurrentId());

        List<AddressBook> list = addressBookService.list();
        return Result.success(list);
    }

    /**
     * 查询默认收货地址
     */
    @ApiOperation(value = "查询默认收货地址")
    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        log.info("查询用户默认地址：{}", BaseContext.getCurrentId());

        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(addressBook);
    }

    /**
     * 修改地址信息
     */
    @ApiOperation(value = "修改地址信息")
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("修改地址信息：{}", addressBook);

        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     */
    @ApiOperation(value = "设置默认地址")
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){
        log.info("设置默认地址, 用户id：{}，地址id：{}", BaseContext.getCurrentId(), addressBook.getId());

        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址
     */
    @ApiOperation(value = "根据id查询地址")
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id){
        log.info("根据id查询地址：{}", id);

        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 删除地址
     */
    @ApiOperation(value = "删除地址")
    @DeleteMapping
    public Result delete(Long id){
        log.info("根据id删除地址：{}", id);

        addressBookService.delete(id);
        return Result.success();
    }

}
