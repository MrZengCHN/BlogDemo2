package com.mrzeng.blogdemo2.controller;

import com.mrzeng.blogdemo2.service.ITypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-22
 */
@Controller
@RequestMapping("/type")
public class TypeController {
    @Resource
    private ITypeService typeService;

    @GetMapping("/delete")
    public Map<String, Object> deleteType(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean b = typeService.removeById(id);
        if (b) {
            resultMap.put("code", 200);
            resultMap.put("message", "删除成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "删除失败");
        }
        return resultMap;
    }
}
