package com.mrzeng.blogdemo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mrzeng.blogdemo2.entity.Comment;
import com.mrzeng.blogdemo2.mapper.CommentMapper;
import com.mrzeng.blogdemo2.service.ICommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-22
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
