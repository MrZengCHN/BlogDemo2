package com.mrzeng.blogdemo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mrzeng.blogdemo2.entity.Comment;
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
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT * FROM t_comment WHERE parent_comment_id=#{parentCommentId} and status = 1 ")
    List<Comment> selectCommentsByParent_id(@Param("parentCommentId") Long parentCommentId);

    /*自索引*/
    @Select("SELECT * FROM t_comment WHERE blog_id=#{blog_id} and status = 1")
    @Results({
            @Result(property = "comments", column = "id",
                    many = @Many(select = "com.mrzeng.blogdemo2.mapper.CommentMapper.selectCommentsByParent_id"))
    })
    @Result(property = "id", column = "id")
    List<Comment> selectCommentsByBlogId(@Param("blog_id") Long blogId);

}
