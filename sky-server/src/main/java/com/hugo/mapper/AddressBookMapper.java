package com.hugo.mapper;

import com.hugo.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    @Insert("insert into address_book (user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label)" +
            "values (#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label})")
    void insert(AddressBook addressBook);

    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> list(Long userId);

    @Select("select * from address_book where user_id = #{userId} and is_default = #{isDefault}")
    AddressBook getDefault(AddressBook addressBook);

    void update(AddressBook addressBook);

    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateDefaultByUserId(AddressBook addressBook);

    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    @Delete("delete from address_book where id = #{id}")
    void delete(Long id);
}
