package com.mrzeng.blogdemo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mrzeng.blogdemo2.entity.Type;
import com.mrzeng.blogdemo2.mapper.TypeMapper;
import com.mrzeng.blogdemo2.service.ITypeService;
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
 * @since 2023-02-22
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {
    @Resource
    private TypeMapper typeMapper;

    @Override
    public List<Type> selectTypesByPage(Long page, Long size) {
        Long offset = (page - 1) * size;
        List<Type> types = new ArrayList<>();
        try {
            types = typeMapper.selectTypePage(offset, size);
        } catch (Exception e) {
            types = null;
        }
        return types;
    }
}
