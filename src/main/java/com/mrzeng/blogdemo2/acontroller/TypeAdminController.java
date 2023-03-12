package com.mrzeng.blogdemo2.acontroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mrzeng.blogdemo2.entity.Type;
import com.mrzeng.blogdemo2.service.ITypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*管理员*/
@Controller
@RequestMapping("/admin")
public class TypeAdminController {
    @Resource
    private ITypeService typeService;

    @ResponseBody
    @PostMapping("/typeInput")
    public Map<String, Object> input(@RequestParam("name") String name) {
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.eq("name", name);
        List<Type> types = typeService.list(typeQueryWrapper);
        if (types.size() > 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "该类型已经存在");
            return resultMap;
        } else {
            Type type = new Type();
            type.setName(name);
            try {
                typeService.save(type);
                resultMap.put("code", 200);
            } catch (Exception e) {
                resultMap.put("code", 400);
                resultMap.put("message", e.toString());
            }
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/deleteTypeById")
    public Map<String, Object> deleteTypeById(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean remove = typeService.removeById(id);
        if (remove) {
            resultMap.put("code", 200);
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "删除失败");
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/updateTypeById")
    public Map<String, Object> updateTypeById(Type type) {
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.eq("name", type.getName());
        List<Type> types = typeService.list(typeQueryWrapper);
        if (types.size() > 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "类型已存在，修改失败！");
            return resultMap;
        } else {
            boolean b = typeService.saveOrUpdate(type);
            if (b) {
                resultMap.put("code", 200);
            } else {
                resultMap.put("code", 400);
                resultMap.put("message", "修改失败，联系管理员");
            }

        }
        return resultMap;
    }
}
