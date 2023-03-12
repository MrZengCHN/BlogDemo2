package com.mrzeng.blogdemo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mrzeng.blogdemo2.entity.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-23
 */
public interface TagMapper extends BaseMapper<Tag> {
    @Select("select * from t_tag limit #{offset} , #{limit}")
    List<Tag> selectTagPage(@Param("offset") Long offset, @Param("limit") Long limit);

    @Select("SELECT * FROM t_tag")
    @Results(
            @Result(property = "blogs", column = "id",
                    many = @Many(select = "com.mrzeng.blogdemo2.mapper.BlogTagsMapper.selectBlogsByTagId"))
    )
    @Result(property = "id", column = "id")
    List<Tag> selectAllTagsWithBlogList();
}
