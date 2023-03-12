package com.mrzeng.blogdemo2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @TableName t_type
 */
@TableName(value = "t_type")
@Data
public class Type implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    List<Blog> blogs;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    private String name;
}