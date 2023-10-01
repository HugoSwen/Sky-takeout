package com.hugo.mapper;

import com.github.pagehelper.Page;
import com.hugo.dto.OrdersDTO;
import com.hugo.dto.OrdersPageQueryDTO;
import com.hugo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    @Select("select * from orders where number = #{number}")
    Orders getByNumber(String number);

    void update(Orders orders);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    Page<Orders> pageQuery(OrdersPageQueryDTO pageQueryDTO);

    @Select("select count(*) from orders where status = #{status}")
    Integer countByStatus(Integer status);

    @Select("select * from orders where status = #{status} and order_time <= #{orderTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    Double sumByMap(Map<String, Object> map);
}
