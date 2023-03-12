package com.mrzeng.blogdemo2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mrzeng.blogdemo2.entity.Tag;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-23
 */
public interface ITagService extends IService<Tag> {
    List<Tag> selectTagsByPage(Long page, Long size);
}
