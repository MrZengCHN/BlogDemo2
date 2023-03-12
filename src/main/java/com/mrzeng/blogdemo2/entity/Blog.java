package com.mrzeng.blogdemo2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @TableName t_blog
 */
@TableName(value = "t_blog")
@Data
public class Blog implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    private Boolean appreciation;
    /**
     *
     */
    private Boolean commentTabled;
    /**
     *
     */
    private String content;
    /**
     *
     */
    private LocalDateTime creatTime;
    /**
     *
     */
    private String firstPicture;
    /**
     *
     */
    private String flag;
    /**
     *
     */
    private Boolean published;
    /**
     *
     */
    private Boolean recommend;
    /**
     *
     */
    private Boolean shareStatement;
    /**
     *
     */
    private String title;
    /**
     *
     */
    private LocalDateTime updateTime;
    /**
     *
     */
    private Integer views;
    /**
     *
     */
    private Long typeId;
    /*
     * 辅助变量*/
    @TableField(exist = false)
    private Type type;
    @TableField(exist = false)
    private List<Long> tagId;
    @TableField(exist = false)
    private List<Tag> tag;
    /**
     *
     */
    private Long userId;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private List<Tag> tags;
    @TableField(exist = false)
    private List<Comment> comments;
}