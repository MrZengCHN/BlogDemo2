package com.mrzeng.blogdemo2.acontroller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mrzeng.blogdemo2.entity.Type;
import com.mrzeng.blogdemo2.entity.User;
import com.mrzeng.blogdemo2.service.ITypeService;
import com.mrzeng.blogdemo2.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
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
public class AdminController {
    @Resource
    private ITypeService typeService;
    @Resource
    private IUserService userService;

    @GetMapping("/login")
    public String login(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "admin/login";
        } else {
            return "admin/blogs";
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public Map<String, Object> adminLogin(HttpSession session,
                                          @RequestParam("username") String username,
                                          @RequestParam("password") String password) {
        Map<String, Object> resultMap = new HashMap<>();
        if (username.isEmpty() | password.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "用户名或密码不能为空！");
            return resultMap;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username).eq("type", 1);
        List<User> users = userService.list(userQueryWrapper);
        if (users.isEmpty() | users.size() == 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号不存在");
            return resultMap;
        } else if (users.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号异常，请联系管理员 qq 1164334031");
            return resultMap;
        } else {
            User user = users.get(0);
            if (user.getPassword().equals(SecureUtil.md5(password + user.getPasswordSalt()))) {
                session.setAttribute("user", user);
                resultMap.put("code", 200);
                resultMap.put("message", "登录成功，欢迎" + user.getNickname() + "!");
            } else {
                resultMap.put("code", 400);
                resultMap.put("message", "账号或密码错误！");
            }
        }
        return resultMap;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "admin/login";
    }

    @GetMapping("/createUser")
    public String createUser() {
        return "admin/createUser";
    }

    @ResponseBody
    @PostMapping("/createUser")
    public Map<String, Object> createUserPost(User user) {
        Map<String, Object> resultMap = new HashMap<>();
        LocalDateTime creatTime = LocalDateTime.now();
        user.setCreateTime(creatTime);
        user.setUpdateTime(creatTime);
        String salt = RandomUtil.randomString(6);
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);
        user.setPasswordSalt(salt);
        user.setPassword(md5Pwd);
        user.setType(0);
        QueryWrapper<User> usernameWrapper = new QueryWrapper<>();
        usernameWrapper.eq("username", user.getUsername());
        List<User> usersUserName = userService.list(usernameWrapper);
        if (usersUserName.size() > 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "用户名已经被注册，请修改用户名！");
            return resultMap;
        }
        QueryWrapper<User> emailWrapper = new QueryWrapper<>();
        emailWrapper.eq("email", user.getEmail());
        List<User> usersEmail = userService.list(emailWrapper);
        if (usersEmail.size() > 0) {
            resultMap.put("code", 400);
            resultMap.put("message", "邮箱已被注册！");
            return resultMap;
        }
        boolean save = userService.save(user);
        if (save) {
            resultMap.put("code", 200);
            resultMap.put("message", "注册成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "注册失败，请联系管理员：qq 1164334031!");
        }
        return resultMap;
    }

    @GetMapping("/types")
    public String types(@RequestParam(value = "page", defaultValue = "1") Long page,
                        @RequestParam(value = "size", defaultValue = "5") Long size,
                        Model model) {
        List<Type> types = new ArrayList<>();
        if (page <= 0) {
            page = 1L;
        }
        types = typeService.selectTypesByPage(page, size);
        if (types.size() == 0) {
            page = page - 1;
            types = typeService.selectTypesByPage(page, size);
        }
        model.addAttribute("types", types);
        model.addAttribute("page", page);
        List<Long> pages = new ArrayList<>();
        Long typeCount = typeService.count();

        for (long i = 1L; i < (typeCount / size) + 2; i++) {
            pages.add(i);
        }

        model.addAttribute("pages", pages);
        return "admin/types";
    }

    @GetMapping("/typesInput")
    public String typesInput() {
        return "admin/types_input";
    }
}
