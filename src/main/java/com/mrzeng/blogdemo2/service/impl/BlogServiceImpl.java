package com.mrzeng.blogdemo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mrzeng.blogdemo2.entity.Blog;
import com.mrzeng.blogdemo2.mapper.BlogMapper;
import com.mrzeng.blogdemo2.service.IBlogService;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
