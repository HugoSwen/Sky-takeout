package com.hugo.mapper;

import com.github.pagehelper.Page;
import com.hugo.dto.OrdersDTO;
import com.hugo.dto.OrdersPageQueryDTO;
import com.hugo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
