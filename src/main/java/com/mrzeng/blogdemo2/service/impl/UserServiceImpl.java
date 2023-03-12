package com.mrzeng.blogdemo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mrzeng.blogdemo2.entity.User;
import com.mrzeng.blogdemo2.mapper.UserMapper;
import com.mrzeng.blogdemo2.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
