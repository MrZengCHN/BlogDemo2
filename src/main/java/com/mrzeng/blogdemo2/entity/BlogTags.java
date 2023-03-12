package com.mrzeng.blogdemo2.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName t_blog_tags
 */
@TableName(value = "t_blog_tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogTags implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Long blogId;
    /**
     *
     */
    private Long tagId;

}