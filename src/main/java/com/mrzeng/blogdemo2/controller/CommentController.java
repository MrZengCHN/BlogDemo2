package com.mrzeng.blogdemo2.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mrzeng.blogdemo2.entity.Comment;
import com.mrzeng.blogdemo2.mapper.CommentMapper;
import com.mrzeng.blogdemo2.service.impl.MailService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
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
public class CommentController {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private MailService mailService;

    @ResponseBody
    @PostMapping("/getComment")
    public Map<String, Object> getComment(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        Comment comment = commentMapper.selectById(id);
        resultMap.put("comment", comment);
        return resultMap;
    }

    @ResponseBody
    @PostMapping("/createComment")
    public Map<String, Object> createComment(@RequestParam("content") String content,
                                             @RequestParam("nickname") String nickname,
                                             @RequestParam("email") String email,
                                             @RequestParam("replyCommentId") Long replyCommentId,
                                             @RequestParam("blogId") Long blogId) {
        Map<String, Object> resultMap = new HashMap<>();
        Comment comment = new Comment();
        comment.setStatus(0);
        comment.setBlogId(blogId);
        comment.setCreatTime(LocalDateTime.now());
        comment.setContent(content);
        comment.setNickname(nickname);
        comment.setEmail(email);
        String confirmCode = IdUtil.getSnowflake(1, 1).nextIdStr();
        comment.setConfirmCode(confirmCode);
        if (replyCommentId == 0) {
            commentMapper.insert(comment);
            /*邮箱验证*/
            String activationUrl = "http://43.143.207.78:80/commentActive?confirmCode=" + confirmCode;
            mailService.sendMailForActivationComment(activationUrl, email, comment.getContent());
            resultMap.put("code", 200);
            resultMap.put("message", "评论博客成功！" + "请前往邮箱：" + email + " 验证后展示评论");
            return resultMap;
        }
        Comment replyComment = commentMapper.selectById(replyCommentId);
        if (replyComment.getParentCommentId() != null) {
            comment.setParentCommentId(replyComment.getParentCommentId());
        } else {
            comment.setParentCommentId(replyComment.getId());
        }
        comment.setContent("@" + replyComment.getNickname() + ":" + content);
        String activationUrl = "http://43.143.207.78:80/commentActive?confirmCode=" + confirmCode;
        mailService.sendMailForActivationComment(activationUrl, email, comment.getContent());
        int insert = commentMapper.insert(comment);
        if (insert > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "成功回复" + replyComment.getNickname() + "请前往邮箱：" + email + " 验证后展示评论");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "回复失败");
        }
        return resultMap;
    }

    @GetMapping("/commentActive")
    public String commentActive(@RequestParam("confirmCode") String confirmCode, Model model) {
        LambdaUpdateWrapper<Comment> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(Comment::getConfirmCode, confirmCode);
        Comment comment = new Comment();
        comment.setStatus(1);
        int update = commentMapper.update(comment, queryWrapper);
        if (update > 0) {
            model.addAttribute("message", "恭喜你可以查看你的评论了！");
        } else {
            model.addAttribute("message", "发生错误了，请联系管理员，QQ 1164334031");
        }
        return "commentActive";
    }

}
