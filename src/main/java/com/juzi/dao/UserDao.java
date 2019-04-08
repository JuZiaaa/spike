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

    @Select("select * from spike_user where id = #{id}")
    User getUserById(@Param("id") long id);
}
