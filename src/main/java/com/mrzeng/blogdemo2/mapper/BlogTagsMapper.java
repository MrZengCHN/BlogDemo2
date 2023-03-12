package com.mrzeng.blogdemo2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrzeng.blogdemo2.entity.Blog;
import com.mrzeng.blogdemo2.entity.BlogTags;
import com.mrzeng.blogdemo2.entity.Tag;
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
public interface BlogTagsMapper extends BaseMapper<BlogTags> {
    @Select("select t_tag.* " +
            "from t_blog_tags " +
            "left outer join t_tag on t_tag.id = t_blog_tags.tag_id " +
            "where blog_id = #{id}")
    List<Tag> selectTagByBlogId(@Param("id") Long id);

    @Select("select t_blog.id,t_blog.title " +
            "from t_blog_tags " +
            "left outer join t_blog on t_blog_tags.blog_id = t_blog.id " +
            "where tag_id = #{id}")
    List<Blog> selectBlogsByTagId(@Param("id") Long id);

    /**
     * @param page blogPage
     * @param id   tagId
     * @param <P>  Blog
     * @return page<Blog { user, type, tags [ . . . . ] }>
     */
    @Select("select t_blog.* " +
            "from t_blog_tags " +
            "left outer join t_blog on t_blog_tags.blog_id = t_blog.id " +
            "where tag_id = #{id}")
    @Results({
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.UserMapper.selectUserById")),
            @Result(property = "type", column = "type_id",
                    one = @One(select = "com.mrzeng.blogdemo2.mapper.TypeMapper.selectTypeById")),
            @Result(property = "tags", column = "id",
                    many = @Many(select = "com.mrzeng.blogdemo2.mapper.BlogTagsMapper.selectTagByBlogId"))
    })
    @Result(property = "id", column = "id")
    <P extends IPage<Blog>> P selectBlogPageByTagId(P page, @Param("id") Long id);

    /**
     * @return tags{blogs=[......]}
     */
    @Select("SELECT * FROM t_tag")
    @Results({
            @Result(property = "blogs", column = "id",
                    many = @Many(select = "com.mrzeng.blogdemo2.mapper.BlogTagsMapper.selectBlogsByTagId"))
    })
    @Result(property = "id", column = "id")
    List<Tag> selectAllTagWithBlogs();
}
