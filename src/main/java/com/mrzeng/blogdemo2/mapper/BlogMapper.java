package com.mrzeng.blogdemo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrzeng.blogdemo2.entity.Blog;
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
public interface BlogMapper extends BaseMapper<Blog> {
    @Select("SELECT * FROM t_blog WHERE title LIKE CONCAT('%', #{title}, '%') ")
    @Results({
            @Result(property = "type", column = "type_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.TypeMapper.selectTypeById"))
    })
    @Result(property = "id", column = "id")
    <P extends IPage<Blog>> P selectBlogWithTypePage(P page, @Param("title") String title);

    @Select("SELECT * FROM t_blog WHERE title LIKE CONCAT('%', #{title}, '%') AND type_id = #{typeId}")
    @Results({
            @Result(property = "type", column = "type_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.TypeMapper.selectTypeById"))
    })
    @Result(property = "id", column = "id")
    <P extends IPage<Blog>> P selectBlogWithTypePageByTypeId(P page, @Param("title") String title, @Param("typeId") Long typeId);

    /*首页查询*/
    @Select("SELECT * FROM t_blog WHERE title LIKE CONCAT('%', #{title}, '%') AND published=true")
    @Results({
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.UserMapper.selectUserById")),
            @Result(property = "type", column = "type_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.TypeMapper.selectTypeById")),
            @Result(property = "tags", column = "id", many = @Many(select = "com.mrzeng.blogdemo2.mapper.BlogTagsMapper.selectTagByBlogId"))
    })
    @Result(property = "id", column = "id")
    <P extends IPage<Blog>> P selectAllBlog(P page, @Param("title") String title);

    @Select("SELECT id,title FROM t_blog WHERE type_id = #{type_id}")
    List<Blog> selectBlogsByTypeId(@Param("type_id") Long typeId);

    /*分类博客*/
    @Select("SELECT * FROM t_blog WHERE type_id = #{type_id} AND published=true")
    @Results({
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.UserMapper.selectUserById")),
            @Result(property = "type", column = "type_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.TypeMapper.selectTypeById")),
            @Result(property = "tags", column = "id", many = @Many(select = "com.mrzeng.blogdemo2.mapper.BlogTagsMapper.selectTagByBlogId"))
    })
    @Result(property = "id", column = "id")
    <P extends IPage<Blog>> P selectBlogsWithUserAndTagsByTypeId(P page, @Param("type_id") Long typeId);

    /**
     * 根据id获取博客
     * 包括 用户，type，tags，comments
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM t_blog WHERE id = #{id}")
    @Results({
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.UserMapper.selectUserById")),
            @Result(property = "type", column = "type_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.TypeMapper.selectTypeById")),
            @Result(property = "tags", column = "id", many = @Many(select = "com.mrzeng.blogdemo2.mapper.BlogTagsMapper.selectTagByBlogId")),
            @Result(property = "comments", column = "id",
                    many = @Many(select = "com.mrzeng.blogdemo2.mapper.CommentMapper.selectCommentsByBlogId"))
    })
    @Result(property = "id", column = "id")
    Blog selectBlogUserTypeTagsCommandsByBlogId(@Param("id") Long id);

    /**
     * 查询所有博客
     *
     * @return AllBlogs{user,type,tags[]}
     */
    @Select("SELECT * FROM t_blog")
    @Results({
            @Result(property = "type", column = "type_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.TypeMapper.selectTypeById")),
            @Result(property = "tags", column = "id",
                    many = @Many(select = "com.mrzeng.blogdemo2.mapper.BlogTagsMapper.selectTagByBlogId")),
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.UserMapper.selectUserById"))
    })
    @Result(property = "id", column = "id")
    List<Blog> selectAllBlogWithUserTypeAndTags();

}
