package com.mrzeng.blogdemo2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrzeng.blogdemo2.configs.MarkdownUtils;
import com.mrzeng.blogdemo2.entity.Blog;
import com.mrzeng.blogdemo2.entity.Tag;
import com.mrzeng.blogdemo2.entity.Type;
import com.mrzeng.blogdemo2.entity.Year;
import com.mrzeng.blogdemo2.mapper.BlogMapper;
import com.mrzeng.blogdemo2.mapper.BlogTagsMapper;
import com.mrzeng.blogdemo2.mapper.TagMapper;
import com.mrzeng.blogdemo2.mapper.TypeMapper;
import com.mrzeng.blogdemo2.service.IBlogService;
import com.mrzeng.blogdemo2.service.ITagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller

public class SystemController {
    @Resource
    private BlogMapper blogMapper;
    @Resource
    private IBlogService blogService;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private BlogTagsMapper blogTagsMapper;
    @Resource
    private ITagService tagService;

    @GetMapping({"/", "/index"})
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "1") Long page,
                        @RequestParam(value = "title", defaultValue = "") String title) {
        Page<Blog> Page = new Page<>(page, 4);
        List<Blog> blogs = blogMapper.selectAllBlog(Page, title).getRecords();
        for (int i = 0; i < blogs.size(); i++) {
            try {
                blogs.get(i).setContent(MarkdownUtils.markdownToHtml(blogs.get(i).getContent().substring(0, 200)));
            } catch (Exception e) {

            }
        }
        List<Type> types = typeMapper.selectTypeWithBlogs();
        List<Tag> tags = tagMapper.selectAllTagsWithBlogList();
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        /*推荐最新30天内的博客*/
        LocalDateTime dateTime = LocalDateTime.now().minusDays(30);
        queryWrapper.gt(Blog::getUpdateTime, dateTime);
        queryWrapper.eq(Blog::getRecommend, true);
        model.addAttribute("newBlogs", blogMapper.selectList(queryWrapper));
        model.addAttribute("blogs", blogs);
        model.addAttribute("types", types);
        model.addAttribute("counts", Page.getTotal());
        model.addAttribute("tags", tags);
        model.addAttribute("Page", Page.getPages());
        model.addAttribute("page", page);
        System.out.println("你好张三");
        return "index";
    }

    @GetMapping("/types")
    public String types(Model model,
                        @RequestParam(value = "typeId", defaultValue = "0") Long typeId,
                        @RequestParam(value = "page", defaultValue = "1") Long page) {
        Page<Blog> blogPage = new Page<>(page, 4);
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        List<Blog> blogs = new ArrayList<>();
        List<Type> types = typeMapper.selectTypeWithBlogs();
        if (typeId == 0) {
            blogs = blogMapper.selectAllBlog(blogPage, "").getRecords();
        } else {
            blogs = blogMapper.selectBlogsWithUserAndTagsByTypeId(blogPage, typeId).getRecords();
        }
        for (int i = 0; i < blogs.size(); i++) {
            try {
                blogs.get(i).setContent(MarkdownUtils.markdownToHtml(blogs.get(i).getContent().substring(0, 200)));
            } catch (Exception e) {

            }
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("types", types);
        model.addAttribute("counts", blogPage.getTotal());
        model.addAttribute("Page", blogPage.getPages());
        model.addAttribute("typeId", typeId);
        model.addAttribute("page", page);
        return "types";
    }

    /*标签页面*/
    @GetMapping("/tags")
    public String tags(@RequestParam(value = "page", defaultValue = "1") Long page,
                       @RequestParam(value = "size", defaultValue = "3") Long size,
                       @RequestParam(value = "tagId", defaultValue = "0") Long tagId,
                       Model model) {
        Page<Blog> blogPage = new Page<>(page, size);
        List<Blog> blogs = new ArrayList<>();
        List<Tag> tags = blogTagsMapper.selectAllTagWithBlogs();
        if (tagId == 0) {
            blogs = blogMapper.selectAllBlog(blogPage, "").getRecords();
        } else {
            blogs = blogTagsMapper.selectBlogPageByTagId(blogPage, tagId).getRecords();
        }
        for (int i = 0; i < blogs.size(); i++) {
            try {
                blogs.get(i).setContent(MarkdownUtils.markdownToHtml(blogs.get(i).getContent().substring(0, 200)));
            } catch (Exception e) {

            }
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("tags", tags);
        model.addAttribute("counts", blogPage.getTotal());
        model.addAttribute("Page", blogPage.getPages());
        model.addAttribute("page", page);
        model.addAttribute("tagId", tagId);
        return "tags";
    }

    @GetMapping("/achieves")
    public String achieves(Model model) {
        List<Year> years = new ArrayList<>();
        List<Blog> blogs = blogMapper.selectAllBlogWithUserTypeAndTags();
        for (int i = 0; i < 4; i++) {
            LocalDateTime dateTime = LocalDateTime.now().minusYears(i);
            years.add(new Year(dateTime.getYear(), new ArrayList<>()));
        }
        for (int i = 0; i < blogs.size(); i++) {
            for (int y = 0; y < years.size(); y++) {
                if (years.get(y).getYear() == blogs.get(i).getUpdateTime().getYear()) {
                    years.get(y).getBlogs().add(blogs.get(i));
                }
            }
        }
        model.addAttribute("years", years);
        model.addAttribute("blogCount", blogs.size());
        return "achieves";
    }

    @GetMapping("/aboutMe")
    public String aboutMe() {
        return "aboutMe";
    }
}
