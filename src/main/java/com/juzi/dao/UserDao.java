package com.juzi.dao;

import com.juzi.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author: juzi
 * @date: 2019/4/4
 * @time: 0:55
 */
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getUserById(@Param("id") int id);
}
