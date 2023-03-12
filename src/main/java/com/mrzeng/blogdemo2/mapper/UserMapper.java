package com.mrzeng.blogdemo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mrzeng.blogdemo2.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-22
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT nickname FROM t_user WHERE id=#{id}")
    User selectUserById(@Param("id") Long id);
}
