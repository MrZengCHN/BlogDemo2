package com.mrzeng.blogdemo2.acontroller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mrzeng.blogdemo2.entity.BlogTags;
import com.mrzeng.blogdemo2.entity.Tag;
import com.mrzeng.blogdemo2.mapper.BlogTagsMapper;
import com.mrzeng.blogdemo2.service.ITagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class TagAdminController {
    @Resource
    private BlogTagsMapper blogTagsMapper;
    @Resource
    private ITagService tagService;

    @GetMapping("/tags")
    public String tags(@RequestParam(value = "page", defaultValue = "1") Long page,
                       @RequestParam(value = "size", defaultValue = "5") Long size,
                       Model model) {
        List<Tag> tags = new ArrayList<>();
        if (page <= 0) {
            page = 1L;
        }
        tags = tagService.selectTagsByPage(page, size);
        if (tags.size() == 0) {
            page = page - 1;
            tags = tagService.selectTagsByPage(page, size);
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", page);
        List<Long> pages = new ArrayList<>();
        Long tagCount = tagService.count();

        for (long i = 1L; i < (tagCount / size) + 2; i++) {
            pages.add(i);
        }

        model.addAttribute("pages", pages);
        return "admin/tags";
    }

    @GetMapping("/tagsInput")
    public String tagsInput() {
        return "admin/tags_input";
    }

    @ResponseBody
    @PostMapping("/tagInput")
    public Map<String, Object> input(@RequestParam("name") String name) {
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("name", name);
        List<Tag> tags = tagService.list(tagQueryWrapper);
        if (tags.size() > 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "该标签已经存在");
            return resultMap;
        } else {
            Tag tag = new Tag();
            tag.setName(name);
            try {
                tagService.save(tag);
                resultMap.put("code", 200);
            } catch (Exception e) {
                resultMap.put("code", 400);
                resultMap.put("message", e.toString());
            }
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/deleteTagById")
    public Map<String, Object> deleteTagById(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        LambdaQueryWrapper<BlogTags> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTags::getTagId, id);
        blogTagsMapper.delete(queryWrapper);
        boolean remove = tagService.removeById(id);
        if (remove) {
            resultMap.put("code", 200);
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "删除失败");
        }
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/updateTagById")
    public Map<String, Object> updateTagById(Tag tag) {
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("name", tag.getName());
        List<Tag> tags = tagService.list(tagQueryWrapper);
        if (tags.size() > 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "标签已存在，修改失败！");
            return resultMap;
        } else {
            boolean b = tagService.saveOrUpdate(tag);
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
