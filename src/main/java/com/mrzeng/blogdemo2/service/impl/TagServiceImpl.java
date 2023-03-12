package com.mrzeng.blogdemo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mrzeng.blogdemo2.entity.Tag;
import com.mrzeng.blogdemo2.mapper.TagMapper;
import com.mrzeng.blogdemo2.service.ITagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-23
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Resource
    private TagMapper tagMapper;

    @Override
    public List<Tag> selectTagsByPage(Long page, Long size) {
        Long offset = (page - 1) * size;
        List<Tag> tags = new ArrayList<>();
        try {
            tags = tagMapper.selectTagPage(offset, size);
        } catch (Exception e) {
            tags = null;
        }
        return tags;
    }
}
