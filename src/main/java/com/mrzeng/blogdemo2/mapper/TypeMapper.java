package com.mrzeng.blogdemo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mrzeng.blogdemo2.entity.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-22
 */
public interface TypeMapper extends BaseMapper<Type> {
    @Select("select * from t_type limit #{offset} , #{limit}")
    List<Type> selectTypePage(@Param("offset") Long offset, @Param("limit") Long limit);

    @Select("SELECT * FROM t_type WHERE id=#{id}")
    Type selectTypeById(@Param("id") Long id);

    @Select("SELECT * FROM t_type")
    @Results(
            @Result(property = "blogs", column = "id",
                    many = @Many(select = "com.mrzeng.blogdemo2.mapper.BlogMapper.selectBlogsByTypeId"))
    )
    @Result(property = "id", column = "id")
    List<Type> selectTypeWithBlogs();
}
