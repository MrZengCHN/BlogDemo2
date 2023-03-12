package com.mrzeng.blogdemo2.controller;

import com.mrzeng.blogdemo2.configs.MarkdownUtils;
import com.mrzeng.blogdemo2.entity.Blog;
import com.mrzeng.blogdemo2.mapper.BlogMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author MrZengCHN
 * @since 2023-02-22
 */
@Controller
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private BlogMapper blogMapper;

    @GetMapping("")
    public String blogShow(@RequestParam("blogId") Long blogId, Model model) {
        Blog blog = blogMapper.selectBlogUserTypeTagsCommandsByBlogId(blogId);
        blog.setViews(blog.getViews() + 1);
        blogMapper.updateById(blog);
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(blog.getContent()));
        model.addAttribute("blog", blog);
        return "blog";
    }

}
