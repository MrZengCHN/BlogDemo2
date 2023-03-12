package com.mrzeng.blogdemo2.acontroller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrzeng.blogdemo2.entity.*;
import com.mrzeng.blogdemo2.mapper.BlogMapper;
import com.mrzeng.blogdemo2.service.IBlogService;
import com.mrzeng.blogdemo2.service.IBlogTagsService;
import com.mrzeng.blogdemo2.service.ITagService;
import com.mrzeng.blogdemo2.service.ITypeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BlogAdminController {
    @Resource
    private IBlogService blogService;
    @Resource
    private ITypeService typeService;
    @Resource
    private ITagService tagService;
    @Resource
    private BlogMapper blogMapper;
    @Resource
    private IBlogTagsService blogTagsService;

    private Blog blogSet(Blog blog) {
        if (blog.getAppreciation() == null) {
            blog.setAppreciation(false);
        }
        if (blog.getCommentTabled() == null) {
            blog.setCommentTabled(false);
        }
        if (blog.getPublished() == null) {
            blog.setPublished(false);
        }
        if (blog.getRecommend() == null) {
            blog.setRecommend(false);
        }
        if (blog.getShareStatement() == null) {
            blog.setShareStatement(false);
        }
        return blog;
    }

    @GetMapping("/blogs")
    public String blogs(Model model,
                        @RequestParam(value = "title", defaultValue = "") String title,
                        @RequestParam(value = "typeId", defaultValue = "0") Long typeId,
                        @RequestParam(value = "page", defaultValue = "1") Long page,
                        @RequestParam(value = "size", defaultValue = "10") Long size) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        Page<Blog> blogPage = new Page<>(page, size);
        if (typeId == 0) {
            queryWrapper.like("title", title);
            blogMapper.selectBlogWithTypePage(blogPage, title);
        } else {
            queryWrapper.like("title", title).eq("type_id", typeId);
            blogMapper.selectBlogWithTypePageByTypeId(blogPage, title, typeId);
        }

        List<Blog> blogs = blogPage.getRecords();
        /*blogs.forEach(System.out::println);*/
        List<Type> types = typeService.list();
        model.addAttribute("Page", blogPage.getPages());
        model.addAttribute("types", types);
        if (blogs.size() == 0) {
            model.addAttribute("blogs", blogs);
            model.addAttribute("page", 1L);
            return "admin/blogs";
        }
        model.addAttribute("page", page);
        model.addAttribute("blogs", blogs);
        model.addAttribute("typeId", typeId);
        model.addAttribute("title", title);


        return "admin/blogs";
    }

    @GetMapping("/blogsInput")
    public String blogsInput(Model model) {
        List<Type> types = typeService.list();
        List<Tag> tags = tagService.list();
        model.addAttribute("types", types);
        model.addAttribute("tags", tags);
        return "admin/blogs_input";
    }

    @ResponseBody
    @PostMapping("/blogsInput")
    public Map<String, Object> blogInput(Blog blog, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        blog = blogSet(blog);
        User user = (User) session.getAttribute("user");
        LocalDateTime dateTime = LocalDateTime.now();
        blog.setCreatTime(dateTime);
        blog.setUpdateTime(dateTime);
        blog.setUserId(user.getId());
        blog.setViews(0);
        boolean save = blogService.save(blog);
        Long blogId = blog.getId();
        List<Long> tag = blog.getTagId();
        List<BlogTags> blogTagsList = new ArrayList<>();
        for (int i = 0; i < tag.size(); i++) {
            BlogTags blogTags = new BlogTags();
            blogTags.setBlogId(blogId);
            blogTags.setTagId(tag.get(i));
            blogTagsList.add(blogTags);
        }
        boolean b = blogTagsService.saveBatch(blogTagsList);
        if (save) {
            resultMap.put("code", 200);
            resultMap.put("message", "操作成功！");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "操作失败,请重新验证博客信息！");
        }
        return resultMap;
    }


    @ResponseBody
    @PostMapping("/blogRemove")
    public Map<String, Object> blogRemove(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean b = blogTagsService.removeById(id);
        int i = blogMapper.deleteById(id);
        if (i > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "博客删除成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "博客删除失败");
        }
        return resultMap;
    }

    @GetMapping("/blogUpdate")
    public String blogUpdate(@Param("blogId") Long blogId, Model model) {
        Blog blog = blogService.getById(blogId);
        List<Type> types = typeService.list();
        List<Tag> tags = tagService.list();
        LambdaQueryWrapper<BlogTags> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlogTags::getBlogId, blogId);
        List<BlogTags> blogTags = blogTagsService.list(queryWrapper);
        List<Long> tagsOfBlog = new ArrayList<>();
        for (int i = 0; i < blogTags.size(); i++) {
            tagsOfBlog.add(blogTags.get(i).getTagId());
        }
        String tagsOf = tagsOfBlog.toString();
        tagsOf = tagsOf.substring(1, tagsOf.length() - 1);
        String s = tagsOf.replaceAll("\\s", "");
        model.addAttribute("types", types);
        model.addAttribute("tags", tags);
        model.addAttribute("blog", blog);
        model.addAttribute("tagsOfBlog", s);
        return "admin/blogs_edit";
    }

    @ResponseBody
    @PostMapping("/blogUpdate")
    public Map<String, Object> blogUpdate(Blog blog) {
        blog = blogSet(blog);
        Map<String, Object> resultMap = new HashMap<>();
        blog.setUpdateTime(LocalDateTime.now());
        blogTagsService.removeById(blog.getId());
        Long blogId = blog.getId();
        List<Long> tag = blog.getTagId();
        List<BlogTags> blogTagsList = new ArrayList<>();
        for (int i = 0; i < tag.size(); i++) {
            blogTagsList.add(new BlogTags(blogId, tag.get(i)));
        }
        blogTagsService.saveBatch(blogTagsList);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", blog.getId());
        boolean b = blogService.update(blog, queryWrapper);
        if (b) {
            resultMap.put("code", 200);
            resultMap.put("message", "操作成功");
        } else {
            resultMap.put("400", 400);
            resultMap.put("message", "操作失败");
        }
        return resultMap;
    }

}
