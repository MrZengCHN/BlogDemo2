package com.mrzeng.blogdemo2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName t_user
 */
@TableName(value = "t_user")
@Data
public class User implements Serializable {
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
    private String nickname;
    /**
     *
     */
    private String username;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private String passwordSalt;
    /**
     *
     */
    private String email;
    /**
     *
     */
    private LocalDateTime updateTime;
    /**
     *
     */
    private LocalDateTime createTime;
    /**
     *
     */
    private Integer type;
}