package com.mrzeng.blogdemo2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mrzeng.blogdemo2.entity.Type;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-22
 */
public interface ITypeService extends IService<Type> {
    List<Type> selectTypesByPage(Long page, Long size);
}
