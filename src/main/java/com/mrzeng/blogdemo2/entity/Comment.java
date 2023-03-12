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
 * @TableName t_comment
 */
@TableName(value = "t_comment")
@Data
public class Comment implements Serializable {
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
    private String avatar;
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
    private String email;
    /**
     *
     */
    private String nickname;
    /**
     *
     */
    private Long blogId;
    /**
     *
     */
    private Long parentCommentId;
    private int status;
    private String confirmCode;
    @TableField(exist = false)
    private List<Comment> comments;
}